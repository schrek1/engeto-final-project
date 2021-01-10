package com.engeto.bus_reservation.ui.forms;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.util.concurrent.ForkJoinPool;

public class FormUtils {

    private FormUtils() {
        // utility class
    }

    public static JFileChooser getCsvFileChooser(String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV File", "csv"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (dialogTitle != null) {
            fileChooser.setDialogTitle(dialogTitle);
        }
        return fileChooser;
    }

    public static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
