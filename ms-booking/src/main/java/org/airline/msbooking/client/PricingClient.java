package org.airline.msbooking.client;

import org.airline.msbooking.model.dto.request.PricingRequest;
import org.airline.msbooking.model.dto.response.PricingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "ms-pricing", path = "/api/pricing")
public interface PricingClient {

    @PostMapping("/calculate")
    PricingResponse calculatePrice(@RequestBody PricingRequest request);

    @GetMapping("/flight/{flightId}")
    BigDecimal getSimplePrice(@PathVariable("flightId") Long flightId,
                              @RequestParam("flightClass") String flightClass,
                              @RequestParam("passengerCount") Integer passengerCount,
                              @RequestParam(value = "currency", defaultValue = "USD") String currency);
}