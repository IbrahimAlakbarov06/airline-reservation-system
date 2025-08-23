package org.airline.msflight.exception;

public class AircraftNotFoundException extends RuntimeException {
    public AircraftNotFoundException(String message) {
        super(message);
    }
}
