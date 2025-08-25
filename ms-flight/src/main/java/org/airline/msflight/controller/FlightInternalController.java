package org.airline.msflight.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.msflight.model.dto.request.FlightSearchRequest;
import org.airline.msflight.model.dto.response.FlightBookingResponse;
import org.airline.msflight.model.dto.response.FlightPricingResponse;
import org.airline.msflight.model.dto.response.FlightSearchResponse;
import org.airline.msflight.model.enums.City;
import org.airline.msflight.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/flights/internal")
@RequiredArgsConstructor
public class FlightInternalController {

    private final FlightService flightService;

    @PostMapping("/search")
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(
            @Valid @RequestBody FlightSearchRequest request) {
        List<FlightSearchResponse> flights = flightService.searchFlights(request);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<FlightSearchResponse> getFlightDetails(
            @PathVariable Long id,
            @RequestParam(defaultValue = "ECONOMY") String flightClass) {
        FlightSearchResponse flight = flightService.getFlightForSearch(id, flightClass);
        return ResponseEntity.ok(flight);
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

    @GetMapping("/{flightId}/booking")
    public ResponseEntity<FlightBookingResponse> getFlightForBooking(@PathVariable Long flightId) {
        FlightBookingResponse flight = flightService.getFlightForBooking(flightId);
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/{flightId}/availability")
    public ResponseEntity<Boolean> checkSeatAvailability(
            @PathVariable Long flightId,
            @RequestParam String flightClass,
            @RequestParam Integer seatCount) {
        boolean available = flightService.checkSeatAvailability(flightId, flightClass, seatCount);
        return ResponseEntity.ok(available);
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

    @GetMapping("/{flightId}/pricing")
    public ResponseEntity<FlightPricingResponse> getFlightForPricing(@PathVariable Long flightId) {
        FlightPricingResponse flight = flightService.getFlightForPricing(flightId);
        return ResponseEntity.ok(flight);
    }
}
