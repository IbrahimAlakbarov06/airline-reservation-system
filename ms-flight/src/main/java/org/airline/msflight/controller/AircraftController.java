package org.airline.msflight.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.msflight.model.dto.request.AircraftCreateRequest;
import org.airline.msflight.model.dto.request.AircraftUpdateRequest;
import org.airline.msflight.model.dto.response.AircraftResponse;
import org.airline.msflight.service.AircraftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/aircrafts")
@RequiredArgsConstructor
public class AircraftController {

    private final AircraftService aircraftService;

    @PostMapping
    public ResponseEntity<AircraftResponse> createAircraft(
            @Valid @RequestBody AircraftCreateRequest request) {
        AircraftResponse aircraft = aircraftService.createAircraft(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(aircraft);
    }

    @GetMapping
    public ResponseEntity<List<AircraftResponse>> getAllAircrafts() {
        List<AircraftResponse> aircrafts = aircraftService.getAllAircrafts();
        return ResponseEntity.ok(aircrafts);
    }

    @GetMapping("/{aircraftCode}")
    public ResponseEntity<AircraftResponse> getAircraft(@PathVariable String aircraftCode) {
        AircraftResponse aircraftResponse = aircraftService.getAircraftByCode(aircraftCode);
        return ResponseEntity.ok(aircraftResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AircraftResponse> getAircraftById(@PathVariable Long id) {
        AircraftResponse aircraft = aircraftService.getAircraftById(id);
        return ResponseEntity.ok(aircraft);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AircraftResponse> updateAircraft(
            @PathVariable Long id,
            @Valid @RequestBody AircraftUpdateRequest request) {
        AircraftResponse updatedAircraft = aircraftService.updateAircraft(id, request);
        return ResponseEntity.ok(updatedAircraft);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateAircraft(@PathVariable Long id) {
        aircraftService.deactivateAircraft(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<AircraftResponse>> getActiveAircrafts() {
        List<AircraftResponse> aircrafts = aircraftService.getActiveAircraft();
        return ResponseEntity.ok(aircrafts);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<AircraftResponse>> getInActiveAircrafts() {
        List<AircraftResponse> aircrafts = aircraftService.getInActiveAircraft();
        return ResponseEntity.ok(aircrafts);
    }
}