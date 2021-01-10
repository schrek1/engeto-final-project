package com.engeto.bus_reservation.ui.forms;

import com.engeto.bus_reservation.model.Reservation;
import com.engeto.bus_reservation.model.TimetableEntry;
import com.engeto.bus_reservation.service.TimetableHolder;

import javax.swing.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewReservationDialog extends JDialog {

    private final TimetableHolder timetableHolder = new TimetableHolder();
    private JPanel rootPanel;
    private JButton btnOK;
    private JButton btnCancel;
    private JComboBox<String> cmbCityFrom;
    private JComboBox<String> cmbCityTo;
    private JTextField txtName;
    private JFormattedTextField txtDate;
    private JComboBox<String> cmbTime;
    private JPanel pnlSeats;
    private Reservation reservation = null;


    public NewReservationDialog(List<TimetableEntry> timetableEntries) {
        setModal(true);
        getRootPane().setDefaultButton(btnOK);
        setMinimumSize(new Dimension(600, 400));
        setTitle("Make new reservation");
        setLocationRelativeTo(getParent());
        setLocation(100, 100);
        add(rootPanel);

        timetableHolder.setTimetable(timetableEntries);

        btnOK.addActionListener(e -> onOK());
        btnCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        rootPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        cmbCityFrom.setModel(cmbCityFromInit());
        cmbCityFrom.addActionListener(this::cityFromSelected);
        cityFromSelected(null); // little hack for setting cmbCityTo after cmbCityFrom initialized
        cmbCityTo.addActionListener(this::cityToSelected);
        cityToSelected(null); // little hack for setting cmbTime after cmbCityTo and From initialized
    }

    public Reservation makeReservation() {
        setVisible(true);
        return reservation;
    }

    private void cityFromSelected(ActionEvent e) {
        String cityNameFrom = (String) cmbCityFrom.getModel().getSelectedItem();
        if (cityNameFrom != null) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            timetableHolder.findTargetsFromCity(cityNameFrom).forEach(model::addElement);
            cmbCityTo.setModel(model);
            updateCmbTimes();
        }
    }

    private void cityToSelected(ActionEvent e) {
        updateCmbTimes();
    }

    private void updateCmbTimes() {
        String cityNameFrom = (String) cmbCityFrom.getModel().getSelectedItem();
        String cityNameTo = (String) cmbCityTo.getModel().getSelectedItem();

        if (cityNameFrom != null && cityNameTo != null) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            timetableHolder.findTimesOfDeparturesForLine(cityNameFrom, cityNameTo).forEach(model::addElement);
            cmbTime.setModel(model);
        }
    }

    private void onOK() {
        List<Integer> reservedSeats = new ArrayList<>();
        int i = 1;
        for (Component seatBox : pnlSeats.getComponents()) {
            if (((JCheckBox) seatBox).isSelected()) {
                reservedSeats.add(i);
            }
            i++;
        }

        reservation = new Reservation(
                LocalDate.parse(txtDate.getText()),
                (String) cmbTime.getSelectedItem(),
                (String) cmbCityFrom.getSelectedItem(),
                (String) cmbCityTo.getSelectedItem(),
                txtName.getText(),
                reservedSeats
        );

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private ComboBoxModel<String> cmbCityFromInit() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        timetableHolder.getTimetable().stream()
                .map(entry -> entry.from)
                .distinct()
                .forEach(model::addElement);
        return model;
    }

}
