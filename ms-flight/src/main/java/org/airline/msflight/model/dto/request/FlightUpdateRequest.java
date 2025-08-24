package org.airline.msflight.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msflight.model.enums.FlightStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightUpdateRequest {
    private String flightNumber;
    private Long aircraftId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private FlightStatus status;
    private BigDecimal basePrice;
    private String gateNumber;
    private String terminal;
}