package org.airline.mspricing.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PricingRequest {
    private Long flightId;
    private String flightClass;
    private List<PassengerRequest> passengers;
    private String currency;

}
