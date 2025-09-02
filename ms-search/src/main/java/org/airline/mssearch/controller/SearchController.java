package org.airline.mssearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.mssearch.model.dto.request.FlightSearchRequest;
import org.airline.mssearch.model.dto.response.CityResponse;
import org.airline.mssearch.model.dto.response.FlightSearchResponse;
import org.airline.mssearch.model.dto.response.PopularDestinationResponse;
import org.airline.mssearch.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/flights")
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(
            @Valid @RequestBody FlightSearchRequest request,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "false") boolean cheapestOnly,
            @RequestParam(defaultValue = "false") boolean fastestOnly) {

        List<FlightSearchResponse> flights = searchService.searchFlightsWithFilters(
                request, maxPrice, minPrice, sortBy, cheapestOnly, fastestOnly);

        return ResponseEntity.ok(flights);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightSearchResponse> getFlightDetails(
            @PathVariable Long id,
            @RequestParam(defaultValue = "ECONOMY") String flightClass) {
        FlightSearchResponse flight = searchService.getFlightDetails(id, flightClass);
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityResponse>> getAllCities() {
        List<CityResponse> cities = searchService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/popular-destinations")
    public ResponseEntity<List<PopularDestinationResponse>> getPopularDestinations() {
        List<PopularDestinationResponse> destinations = searchService.getPopularDestinations();
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/flight-availability")
    public ResponseEntity<Boolean> checkFlightAvailability(
            @RequestParam Long flightId,
            @RequestParam String flightClass,
            @RequestParam Integer passengers) {
        boolean available = searchService.checkFlightAvailability(flightId, flightClass, passengers);
        return ResponseEntity.ok(available);
    }
}