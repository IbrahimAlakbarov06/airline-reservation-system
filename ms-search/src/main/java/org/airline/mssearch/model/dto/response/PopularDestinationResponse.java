package org.airline.mssearch.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PopularDestinationResponse {
    private String cityName;
    private String country;
    private String countryCode;
    private Integer flightCount;
    private String fullLocation;
}
