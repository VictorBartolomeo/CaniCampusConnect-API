package org.example.canicampusconnectapi.common.exception;

public class EmailConstraintRequests extends RuntimeException {
    public EmailConstraintRequests(String message) {
        super(message);
    }
    public EmailConstraintRequests(String message, Throwable cause) {
        super(message, cause);
    }

}
