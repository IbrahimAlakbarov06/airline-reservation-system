package org.airline.msflight.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.msflight.model.dto.request.FlightCreateRequest;
import org.airline.msflight.model.dto.request.FlightUpdateRequest;
import org.airline.msflight.model.dto.response.FlightResponse;
import org.airline.msflight.model.enums.FlightStatus;
import org.airline.msflight.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(
            @Valid @RequestBody FlightCreateRequest request) {
        FlightResponse flight = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(flight);
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        List<FlightResponse> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        FlightResponse flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightUpdateRequest request) {
        FlightResponse updatedFlight = flightService.updateFlight(id, request);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FlightResponse> updateFlightStatus(
            @PathVariable Long id,
            @RequestParam FlightStatus status) {
        FlightResponse updatedFlight = flightService.updateFlightStatus(id, status);
        return ResponseEntity.ok(updatedFlight);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<FlightResponse>> getFlightsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<FlightResponse> flights = flightService.getFlightsByDateRange(startDate, endDate);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/todays")
    public ResponseEntity<List<FlightResponse>> getTodaysFlights() {
        List<FlightResponse> flights = flightService.todaysFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<FlightResponse>> getUpcomingFlights() {
        List<FlightResponse> flights = flightService.upcomingFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/flight-number/{flightNumber}")
    public ResponseEntity<FlightResponse> getByFlightNumber(@PathVariable String flightNumber) {
        FlightResponse flights = flightService.getByFlightNumber(flightNumber);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FlightResponse>> getFlightsByStatus(
            @PathVariable FlightStatus status) {
        List<FlightResponse> flights = flightService.getFlightByStatus(status);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/route")
    public ResponseEntity<List<FlightResponse>> getFlightsByRoute(
            @RequestParam(name = "originAirportId") Long originAirportId,
            @RequestParam(name = "destinationAirportId") Long destinationAirportId) {
        List<FlightResponse> flights = flightService.getFlightsByRoute(originAirportId, destinationAirportId);
        return ResponseEntity.ok(flights);
    }
}