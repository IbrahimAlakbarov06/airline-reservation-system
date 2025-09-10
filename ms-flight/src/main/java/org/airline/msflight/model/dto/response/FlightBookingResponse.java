package org.airline.msflight.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msflight.model.enums.City;
import org.airline.msflight.model.enums.FlightStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightBookingResponse {
    private Long id;
    private String flightNumber;
    private String originCode;
    private City originCity;
    private String destinationCode;
    private City destinationCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String aircraftType;
    private FlightStatus status;
    private String gateNumber;
    private String terminal;
}