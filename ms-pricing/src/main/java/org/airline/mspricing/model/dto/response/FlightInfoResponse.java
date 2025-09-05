package org.airline.mspricing.model.dto.response;

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
public class FlightInfoResponse {
    private Long id;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal basePrice;
    private BigDecimal economyPrice;
    private BigDecimal businessPrice;
    private BigDecimal firstClassPrice;
    private Integer totalSeats;
    private Integer bookedSeats;
    private Double occupancyRate;
}