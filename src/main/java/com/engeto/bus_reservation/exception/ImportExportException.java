package com.engeto.bus_reservation.exception;

public class ImportExportException extends RuntimeException {
    public ImportExportException(Throwable cause) {
        super("Import/Export failed", cause);
    }
}
