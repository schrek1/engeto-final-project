package com.engeto.bus_reservation.ui.forms;

import com.engeto.bus_reservation.exception.ImportExportException;
import com.engeto.bus_reservation.model.Reservation;
import com.engeto.bus_reservation.service.ImportExportService;
import com.engeto.bus_reservation.service.ReservationsHolder;
import com.engeto.bus_reservation.service.TimetableHolder;
import com.engeto.bus_reservation.ui.model.ReservationsTableModel;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Main window of the application. Displays the list of reservations.
 */
public class ReservationsListFrame extends JFrame {

    private JPanel rootPanel;
    private JButton btnCreateReservation;
    private JButton btnImportReservations;
    private JButton btnExportReservations;
    private JTable tblReservations;
    private JButton btnLoadTimetable;
    private JProgressBar progressBar;

    private final ReservationsHolder reservationsHolder = new ReservationsHolder();
    private final TimetableHolder timetableHolder = new TimetableHolder();

    private final ProgressBarManager progressBarManager = new ProgressBarManager(progressBar);

    public ReservationsListFrame(List<Reservation> includedReservations) {
        super("Engeto Bus Company Reservations");

        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(rootPanel);

        btnImportReservations.addActionListener(this::importReservations);
        btnExportReservations.addActionListener(this::exportReservations);
        btnLoadTimetable.addActionListener(this::openTimetable);
        btnCreateReservation.addActionListener(this::createReservation);

        tblReservations.setModel(new ReservationsTableModel());

        if (includedReservations != null && !includedReservations.isEmpty()) {
            setReservations(includedReservations);
        }
    }

    private void createReservation(ActionEvent e) {
        NewReservationDialog dialog = new NewReservationDialog(timetableHolder.getTimetable());
        Reservation newReservation = dialog.makeReservation();
        if (newReservation != null) {
            reservationsHolder.addNewReservation(newReservation);
            ((ReservationsTableModel) tblReservations.getModel()).addNewReservation(newReservation);
        }
    }

    private void exportReservations(ActionEvent e) {
        JFileChooser fileChooser = FormUtils.getCsvFileChooser("Save reservations");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            btnExportReservations.setEnabled(false);

            File fileFromChooser = fileChooser.getSelectedFile();
            File outputFile = !fileFromChooser.getAbsolutePath().endsWith(".csv") ? new File(fileFromChooser.getPath() + ".csv") : fileFromChooser;

            UUID processId = progressBarManager.createNewProcess();

            new SwingWorker<Boolean, Integer>() { // run out of EDT
                @Override
                protected Boolean doInBackground() {
                    try {
                        ImportExportService.exportReservationsToFile(reservationsHolder.getAllReservations(), outputFile, this::publish);
                    } catch (ImportExportException ioException) {
                        SwingUtilities.invokeLater(() -> FormUtils.showErrorDialog(ReservationsListFrame.this, "Could not save the reservations to " + fileFromChooser.getAbsolutePath()));
                    }
                    return true;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    for (Integer progress : chunks) {
                        progressBarManager.setProgress(processId, progress);
                    }
                }

                @Override
                protected void done() {
                    progressBarManager.setCompleted(processId);
                    btnExportReservations.setEnabled(true);
                }
            }.execute();
        }
    }

    private void importReservations(ActionEvent e) {
        JFileChooser fileChooser = FormUtils.getCsvFileChooser("Import reservations");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            btnImportReservations.setEnabled(false);

            File sourceFile = fileChooser.getSelectedFile();

            UUID processId = progressBarManager.createNewProcess();

            new SwingWorker<Boolean, Integer>() { // run out of EDT
                @Override
                protected Boolean doInBackground() {
                    try {
                        setReservations(ImportExportService.importReservationsFromFile(sourceFile, this::publish));
                    } catch (ImportExportException ioException) {
                        SwingUtilities.invokeLater(() -> FormUtils.showErrorDialog(ReservationsListFrame.this, "Could load reservations from " + sourceFile.getAbsolutePath()));
                    }
                    return true;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    for (Integer progress : chunks) {
                        progressBarManager.setProgress(processId, progress);
                    }
                }

                @Override
                protected void done() {
                    progressBarManager.setCompleted(processId);
                    btnImportReservations.setEnabled(true);
                }
            }.execute();

        }
    }

    private void openTimetable(ActionEvent e) {
        JFileChooser fileChooser = FormUtils.getCsvFileChooser("Import timetable");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            btnLoadTimetable.setEnabled(false);

            File sourceFile = fileChooser.getSelectedFile();

            UUID processId = progressBarManager.createNewProcess();

            new SwingWorker<Boolean, Integer>() { // run out of EDT
                @Override
                protected Boolean doInBackground() {
                    try {
                        timetableHolder.setTimetable(ImportExportService.loadTimetableFromFile(sourceFile, this::publish));
                    } catch (ImportExportException ioException) {
                        SwingUtilities.invokeLater(() -> FormUtils.showErrorDialog(ReservationsListFrame.this, "Could load timetable from " + sourceFile.getAbsolutePath()));
                    }
                    return true;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    for (Integer progress : chunks) {
                        progressBarManager.setProgress(processId, progress);
                    }
                }

                @Override
                protected void done() {
                    btnCreateReservation.setEnabled(true);
                    progressBarManager.setCompleted(processId);
                }
            }.execute();

        }
    }

    private void setReservations(List<Reservation> reservations) {
        reservationsHolder.replaceAllReservationsBy(reservations);
        ((ReservationsTableModel) tblReservations.getModel()).replaceAllReservations(reservations);
    }

}
