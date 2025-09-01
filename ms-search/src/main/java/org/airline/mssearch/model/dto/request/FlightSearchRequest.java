package org.airline.mssearch.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.mssearch.model.enums.City;
import org.airline.mssearch.model.enums.FlightClass;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightSearchRequest {

    @NotNull(message = "Origin city is required")
    private City originCity;

    @NotNull(message = "Destination city is required")
    private City destinationCity;

    @NotNull(message = "Departure date is required")
    @Future(message = "Departure date must be in future")
    private LocalDate departureDate;

    @Positive(message = "Passenger count must be positive")
    private Integer passengerCount = 1;

    private FlightClass flightClass = FlightClass.ECONOMY;

    private LocalDate returnDate;
}
