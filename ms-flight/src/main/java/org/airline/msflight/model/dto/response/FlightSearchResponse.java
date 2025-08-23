package org.airline.msflight.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightSearchResponse {
    private Long id;
    private String flightNumber;
    private String originCode;
    private String originCity;
    private String destinationCode;
    private String destinationCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String duration;
    private BigDecimal price;
    private Integer availableSeats;
    private String aircraftType;
}