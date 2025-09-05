package org.airline.mspricing.client;

import org.airline.mspricing.model.dto.response.FlightInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-flight", path = "api/flights/internal")
public interface FlightClient {

    @GetMapping("/{flightId}/pricing")
    FlightInfoResponse getFlightPricing(@PathVariable Long flightId);
}
