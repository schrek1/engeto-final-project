package com.engeto.bus.io;

import com.engeto.bus_reservation.model.Reservation;
import com.engeto.bus_reservation.model.TimetableEntry;
import com.engeto.bus_reservation.service.ImportExportService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class ImportExportServiceTest {

    @Test
    void loadEmptyStream() {
        final List<Object> loadedTimetable = ImportExportService.loadFromStream(
                toInputStream(""),
                0L,
                i -> {
                },
                lineParts -> fail("There should be no line in empty stream.")
        );

        assertThat(loadedTimetable).isEmpty();
    }

    @Test
    void loadOnlyHeaders() {
        final String TIMETABLE = "TIME;FROM;TO;CAPACITY\n"; // Just the headers line.

        final List<TimetableEntry> loadedTimetable = ImportExportService.loadFromStream(
                toInputStream(TIMETABLE),
                1L,
                i -> {
                },
                lineParts -> fail("There should be no line with data when loading just headers.")
        );
        assertThat(loadedTimetable).isEmpty();
    }

    @Test
    void loadBlankLines() {
        final String TIMETABLE = "\n\n\n\n\n";

        final List<TimetableEntry> loadedTimetable = ImportExportService.loadFromStream(
                toInputStream(TIMETABLE),
                0L,
                i -> {
                },
                lineParts -> fail("Empty lines should be ignored.")
        );
        assertThat(loadedTimetable).isEmpty();
    }

    @Test
    void loadTimetable() {
        final String TIMETABLE =
                "TIME;FROM;TO;CAPACITY\n" +
                        "7:00;Ostrava;Brno;50";

        final List<TimetableEntry> loadedTimetable = ImportExportService.loadTimetable(toInputStream(TIMETABLE), 2L,
                i -> {
                }
        );
        assertThat(loadedTimetable).hasSize(1);

        TimetableEntry timetableEntry = loadedTimetable.get(0);
        assertThat(timetableEntry.time).isEqualTo("7:00");
        assertThat(timetableEntry.from).isEqualTo("Ostrava");
        assertThat(timetableEntry.to).isEqualTo("Brno");
        assertThat(timetableEntry.capacity).isEqualTo(50);
    }

    @Test
    void loadReservations() {
        final String RESERVATIONS =
                "DATE;TIME;FROM;TO;CUSTOMER;SEATS\n" +
                        "2020-09-01;7:00;Ostrava;Brno;John Doe;1,2,8";

        final List<Reservation> loadedReservations = ImportExportService.importReservations(toInputStream(RESERVATIONS), 2L,
                i -> {
                });
        assertThat(loadedReservations).hasSize(1);

        Reservation reservation = loadedReservations.get(0);
        assertThat(reservation.time).isEqualTo("7:00");
        assertThat(reservation.from).isEqualTo("Ostrava");
        assertThat(reservation.to).isEqualTo("Brno");
        assertThat(reservation.customerName).isEqualTo("John Doe");
        assertThat(reservation.seats).containsExactly(1, 2, 8);
    }


    private InputStream toInputStream(String text) {
        return new ByteArrayInputStream(text.getBytes());
    }
}
