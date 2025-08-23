package org.airline.msflight.exception;

import org.airline.msflight.domain.entity.Airport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFlightNotFoundException(FlightNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .message("NOT_FOUND")
                .error(e.getMessage())
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(AircraftNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAirCraftNotFoundException(AircraftNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .message("NOT_FOUND")
                .error(e.getMessage())
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }
    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAirportNotFoundException(AirportNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .message("NOT_FOUND")
                .error(e.getMessage())
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .message("ALREADY_EXISTS")
                .error(e.getMessage())
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .error("METHOD_ARGUMENT_NOT_VALID")
                .message("MethodArgumentNotValidException happened")
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
                .error("INTERNAL_SERVER_ERROR")
                .message(exception.getMessage())
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
