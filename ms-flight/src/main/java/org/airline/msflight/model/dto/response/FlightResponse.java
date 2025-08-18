package org.airline.msflight.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msflight.model.enums.FlightStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightResponse {
    private Long id;
    private String flightNumber;
    private AircraftResponse aircraft;
    private AirportResponse originAirport;
    private AirportResponse destinationAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private FlightStatus status;
    private BigDecimal basePrice;
    private BigDecimal economyPrice;
    private BigDecimal businessPrice;
    private BigDecimal firstClassPrice;
    private Integer availableEconomySeats;
    private Integer availableBusinessSeats;
    private Integer availableFirstClassSeats;
    private String gateNumber;
    private String terminal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
