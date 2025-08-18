package org.airline.msflight.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirportUpdateRequest {

    private String airportCode;
    private String name;
    private String city;
    private String country;
    private String countryCode;
    private String timezone;
    private Double latitude;
    private Double longitude;
    private Boolean isActive;
}