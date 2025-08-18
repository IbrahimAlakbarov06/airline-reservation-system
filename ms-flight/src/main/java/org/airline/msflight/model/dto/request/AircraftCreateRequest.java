package org.airline.msflight.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AircraftCreateRequest {

    @NotBlank(message = "Aircraft code is required")
    private String aircraftCode;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;

    @Positive(message = "Economy seats must be positive")
    private Integer economySeats;

    @Positive(message = "Business seats must be positive")
    private Integer businessSeats;

    @Positive(message = "First class seats must be positive")
    private Integer firstClassSeats;

    @Positive(message = "Max range must be positive")
    private Integer maxRangeKm;

    @Positive(message = "Cruise speed must be positive")
    private Integer cruiseSpeedKmh;

    private Boolean isActive = true;
}