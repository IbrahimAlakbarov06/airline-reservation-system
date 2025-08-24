package org.airline.msflight.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.msflight.model.dto.request.FlightCreateRequest;
import org.airline.msflight.model.dto.request.FlightSearchRequest;
import org.airline.msflight.model.dto.request.FlightUpdateRequest;
import org.airline.msflight.model.dto.response.FlightResponse;
import org.airline.msflight.model.dto.response.FlightSearchResponse;
import org.airline.msflight.model.dto.response.FlightBookingResponse;
import org.airline.msflight.model.dto.response.FlightPricingResponse;
import org.airline.msflight.model.enums.City;
import org.airline.msflight.model.enums.FlightStatus;
import org.airline.msflight.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/flights")
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


    @PostMapping("/{flightId}/reserve-seats")
    public ResponseEntity<Boolean> reserveSeats(
            @PathVariable Long flightId,
            @RequestParam String flightClass,
            @RequestParam Integer seatCount) {
        boolean success = flightService.reserveSeats(flightId, flightClass, seatCount);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/{flightId}/release-seats")
    public ResponseEntity<Void> releaseSeats(
            @PathVariable Long flightId,
            @RequestParam String flightClass,
            @RequestParam Integer seatCount) {
        flightService.releaseSeats(flightId, flightClass, seatCount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{flightId}/booking")
    public ResponseEntity<FlightBookingResponse> getFlightForBooking(@PathVariable Long flightId) {
        FlightBookingResponse flight = flightService.getFlightForBooking(flightId);
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/{flightId}/pricing")
    public ResponseEntity<FlightPricingResponse> getFlightForPricing(@PathVariable Long flightId) {
        FlightPricingResponse flight = flightService.getFlightForPricing(flightId);
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/internal/{flightId}/availability")
    public ResponseEntity<Boolean> checkSeatAvailability(
            @PathVariable Long flightId,
            @RequestParam String flightClass,
            @RequestParam Integer seatCount) {
        boolean available = flightService.checkSeatAvailability(flightId, flightClass, seatCount);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/search")
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(
            @Valid @RequestBody FlightSearchRequest request) {
        List<FlightSearchResponse> flights = flightService.searchFlights(request);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/popular-destinations")
    public ResponseEntity<List<String>> getPopularDestinations() {
        List<String> destinations = flightService.getPopularDestinations();
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = Arrays.asList(City.values());
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<FlightSearchResponse> getFlightDetails(
            @PathVariable Long id,
            @RequestParam(defaultValue = "ECONOMY") String flightClass) {
        FlightSearchResponse flight = flightService.getFlightForSearch(id, flightClass);
        return ResponseEntity.ok(flight);
    }
}