package org.airline.mssearch.client;

import org.airline.mssearch.model.dto.request.FlightSearchRequest;
import org.airline.mssearch.model.dto.response.FlightSearchResponse;
import org.airline.mssearch.model.enums.City;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-flight", path = "/api/flights/internal")
public interface FlightClient {

    @PostMapping("/search")
    List<FlightSearchResponse> searchFlights(@RequestBody FlightSearchRequest request);

    @GetMapping("/{id}/details")
    FlightSearchResponse getFlightDetails(@PathVariable("id") Long id,
                                          @RequestParam("flightClass") String flightClass);

    @GetMapping("/popular-destinations")
    List<String> getPopularDestinations();

    @GetMapping("/cities")
    List<City> getAllCities();

    @GetMapping("/{flightId}/availability")
    Boolean checkSeatAvailability(@PathVariable("flightId") Long flightId,
                                  @RequestParam("flightClass") String flightClass,
                                  @RequestParam("seatCount") Integer seatCount);
}