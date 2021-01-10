package com.engeto.bus_reservation.ui.forms;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProgressBarManager {

    private final Map<UUID, Double> processes = new HashMap<>();

    private final JProgressBar progressBar;

    public ProgressBarManager(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public synchronized UUID createNewProcess() {
        UUID id = UUID.randomUUID();
        processes.put(id, 0.0);
        return id;
    }

    public synchronized void setProgress(UUID processId, double progress) {
        if (processes.containsKey(processId)) {
            processes.put(processId, progress);

            if (!progressBar.isVisible()) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setString(null);
                    progressBar.setVisible(true);
                    progressBar.setStringPainted(true);
                });
            }

            SwingUtilities.invokeLater(() -> progressBar.setValue(calculateProgress()));
        }
    }

    public synchronized void setCompleted(UUID processId) {
        processes.remove(processId);
        if (processes.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                progressBar.setString("COMPLETED");
                progressBar.setVisible(false);
                progressBar.setStringPainted(false);
            });
        }
    }


    private int calculateProgress() {
        int countOfProcesses = processes.keySet().size();
        if (countOfProcesses != 0) {
            double sum = processes.values().stream().mapToDouble(Double::doubleValue).sum();
            return (int) Math.round(sum / countOfProcesses);
        } else {
            return 0;
        }
    }
}
