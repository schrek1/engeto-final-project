package com.engeto.bus_reservation.service;

import com.engeto.bus_reservation.exception.ImportExportException;
import com.engeto.bus_reservation.model.Reservation;
import com.engeto.bus_reservation.model.TimetableEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utilities for serialization and deserialization of the application data.
 */
public class ImportExportService {

    public static final Pattern COLUMN_SEPARATOR = Pattern.compile(";");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_DATE;

    private ImportExportService() {
    }

    public static void exportReservationsToFile(List<Reservation> reservations, File outputFile, IntConsumer progressCallback) {
        if (outputFile.exists()) {
            outputFile.delete();
        }

        try (final OutputStream output = new FileOutputStream(outputFile);
             final PrintWriter writer = new PrintWriter(output)
        ) {
            outputFile.createNewFile();

            int totalRecords = reservations.size();
            int processedRecords = 0;

            writer.println("DATE;TIME;FROM;TO;CUSTOMER;SEATS");
            for (Reservation reservation : reservations) {
                writer.print(DATE_FORMAT.format(reservation.day));
                writer.print(COLUMN_SEPARATOR);
                writer.print(reservation.time);
                writer.print(COLUMN_SEPARATOR);
                writer.print(reservation.from);
                writer.print(COLUMN_SEPARATOR);
                writer.print(reservation.to);
                writer.print(COLUMN_SEPARATOR);
                writer.print(reservation.customerName);
                writer.print(COLUMN_SEPARATOR);
                writer.println(reservation.seats.stream().map(String::valueOf).collect(Collectors.joining(",")));

                processedRecords++;
                progressCallback.accept(((int) ((double) processedRecords / totalRecords * 100)));

                Thread.sleep(200);
            }
        } catch (Exception e) {
            throw new ImportExportException(e);
        }
    }

    public static List<Reservation> importReservationsFromFile(File inputFile, IntConsumer progressCallback) {
        try (InputStream input = new FileInputStream(inputFile);
             Stream<String> lines = Files.lines(inputFile.toPath())
        ) {
            return importReservations(input, lines.count(), progressCallback);
        } catch (IOException e) {
            throw new ImportExportException(e);
        }
    }

    public static List<Reservation> importReservations(InputStream input, long linesCount, IntConsumer progressCallback) {
        return loadFromStream(input, linesCount, progressCallback, lineParts -> {
            try {
                return new Reservation(
                        LocalDate.parse(lineParts.get(0), DATE_FORMAT),
                        lineParts.get(1),
                        lineParts.get(2),
                        lineParts.get(3),
                        lineParts.get(4),
                        Arrays.stream(lineParts.get(5).split(","))
                                .map(Integer::valueOf)
                                .collect(Collectors.toList())
                );
            } catch (NumberFormatException e) {
                throw new ImportExportException(e);
            }
        });
    }

    public static List<TimetableEntry> loadTimetableFromFile(File inputFile, IntConsumer progressCallback) {
        try (InputStream input = new FileInputStream(inputFile);
             Stream<String> lines = Files.lines(inputFile.toPath())
        ) {
            return loadTimetable(input, lines.count(), progressCallback);
        } catch (IOException e) {
            throw new ImportExportException(e);
        }
    }

    public static List<TimetableEntry> loadTimetable(InputStream input, long linesCount, IntConsumer progressCallback) {
        return loadFromStream(input, linesCount, progressCallback, lineParts ->
                new TimetableEntry(
                        lineParts.get(0),
                        lineParts.get(1),
                        lineParts.get(2),
                        Integer.parseInt(lineParts.get(3))
                )
        );
    }

    public static <E> List<E> loadFromStream(InputStream input, long linesCount, IntConsumer progressCallback, Function<List<String>, E> convertor) {
        List<E> entities = new ArrayList<>();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
        int actualRead = 0;
        try {
            if (inputReader.ready()) inputReader.readLine(); // Skip the headers line.
            while (inputReader.ready()) {
                String line = inputReader.readLine();
                if (line == null || line.isEmpty()) continue;
                entities.add(convertor.apply(Arrays.asList(COLUMN_SEPARATOR.split(line))));
                actualRead++;

                progressCallback.accept((int) ((double) actualRead / linesCount * 100));

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (RuntimeException | IOException e) {
            throw new ImportExportException(e);
        }
        return entities;
    }
}
