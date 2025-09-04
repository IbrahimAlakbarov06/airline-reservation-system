package org.airline.msbooking.exception;

public class FlightNotAvailableException extends RuntimeException {
    public FlightNotAvailableException(String message) {
        super(message);
    }
}
