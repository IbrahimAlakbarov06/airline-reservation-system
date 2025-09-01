package org.airline.mssearch.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityResponse {
    private String code;
    private String name;
    private String country;
    private String countryCode;
    private String fullLocation;
}