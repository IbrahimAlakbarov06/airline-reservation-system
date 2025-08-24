package org.airline.msflight.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.msflight.model.dto.request.AirportCreateRequest;
import org.airline.msflight.model.dto.request.AirportUpdateRequest;
import org.airline.msflight.model.dto.response.AirportResponse;
import org.airline.msflight.service.AirportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(
            @Valid @RequestBody AirportCreateRequest request) {
        AirportResponse airport = airportService.createAirport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(airport);
    }

    @GetMapping
    public ResponseEntity<List<AirportResponse>> getAllAirports() {
        List<AirportResponse> airports = airportService.getAllAirports();
        return ResponseEntity.ok(airports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> getAirportById(@PathVariable Long id) {
        AirportResponse airport = airportService.getAirportById(id);
        return ResponseEntity.ok(airport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportResponse> updateAirport(
            @PathVariable Long id,
            @Valid @RequestBody AirportUpdateRequest request) {
        AirportResponse updatedAirport = airportService.updateAirport(id, request);
        return ResponseEntity.ok(updatedAirport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateAirport(@PathVariable Long id) {
        airportService.deactivateAirport(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<AirportResponse>> getActiveAirports() {
        List<AirportResponse> airports = airportService.getActiveAirports();
        return ResponseEntity.ok(airports);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<AirportResponse>> getInActiveAirports() {
        List<AirportResponse> airports = airportService.getInactiveAirports();
        return ResponseEntity.ok(airports);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<AirportResponse>> getAirportsByCity(@PathVariable String city) {
        List<AirportResponse> airports = airportService.getAirportsByCity(city);
        return ResponseEntity.ok(airports);
    }
}