package org.airline.msbooking.client;

import org.airline.msbooking.model.dto.response.FlightInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-flight", path = "api/flights/internal")
public interface FlightClient {

    @GetMapping("/{flightId}/booking")
    FlightInfoResponse getFlightForBooking(@PathVariable("flightId") Long flightId);

    @GetMapping("/{flightId}/availability")
    Boolean checkSeatAvailability(@PathVariable("flightId") Long flightId,
                                  @RequestParam("flightClass") String flightClass,
                                  @RequestParam("seatCount") Integer seatCount);

    @PostMapping("/{flightId}/reserve-seats")
    Boolean reserveSeats(@PathVariable("flightId") Long flightId,
                         @RequestParam("flightClass") String flightClass,
                         @RequestParam("seatCount") Integer seatCount);

    @PostMapping("/{flightId}/release-seats")
    void releaseSeats(@PathVariable("flightId") Long flightId,
                      @RequestParam("flightClass") String flightClass,
                      @RequestParam("seatCount") Integer seatCount);
}
