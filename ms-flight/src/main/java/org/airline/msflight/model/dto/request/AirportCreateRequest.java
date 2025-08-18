package org.airline.msflight.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirportCreateRequest {

    @NotBlank(message = "Airport code is required")
    @Size(min = 3, max = 3, message = "Airport code must be 3 characters")
    private String airportCode;

    @NotBlank(message = "Airport name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    @Size(min = 2, max = 2, message = "Country code must be 2 characters")
    private String countryCode;

    private String timezone;
    private Double latitude;
    private Double longitude;
    private Boolean isActive = true;
}