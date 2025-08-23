package org.airline.msflight.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightSearchRequest {

    @NotBlank(message = "Origin city is required")
    private String originCity;

    @NotBlank(message = "Destination city is required")
    private String destinationCity;

    @NotNull(message = "Departure date is required")
    @Future(message = "Departure date must be in future")
    private LocalDate departureDate;

    @Positive(message = "Passenger count must be positive")
    private Integer passengerCount = 1;

    private String flightClass = "ECONOMY";

    private LocalDate returnDate;
}