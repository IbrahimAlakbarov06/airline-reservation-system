package org.airline.msflight.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AircraftUpdateRequest {

    private String aircraftCode;
    private String model;
    private String manufacturer;
    private Integer totalSeats;
    private Integer economySeats;
    private Integer businessSeats;
    private Integer firstClassSeats;
    private Integer maxRangeKm;
    private Integer cruiseSpeedKmh;
    private Boolean isActive;
}