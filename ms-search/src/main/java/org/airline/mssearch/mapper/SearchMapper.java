package org.airline.mssearch.mapper;

import lombok.extern.slf4j.Slf4j;
import org.airline.mssearch.model.dto.response.CityResponse;
import org.airline.mssearch.model.dto.response.PopularDestinationResponse;
import org.airline.mssearch.model.enums.City;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SearchMapper {

    public CityResponse toCityResponse(City city) {
        return CityResponse.builder()
                .code(city.name())
                .name(city.getCityName())
                .country(city.getCountryName())
                .countryCode(city.getCountryCode())
                .fullLocation(city.getFullLocation())
                .build();
    }

    public List<CityResponse> mapCitiesToResponseList(List<City> cities) {
        return cities.stream()
                .map(this::toCityResponse)
                .collect(Collectors.toList());
    }

    public PopularDestinationResponse toPopularDestinationResponse(String cityName, Integer flightCount) {
        String cleanCityName = cleanCityName(cityName);
        Integer actualFlightCount = extractFlightCount(cityName);
        City city = City.fromCityName(cleanCityName);

        if (city != null) {
            return PopularDestinationResponse.builder()
                    .cityName(city.getCityName())
                    .country(city.getCountryName())
                    .countryCode(city.getCountryCode())
                    .flightCount(actualFlightCount)
                    .fullLocation(city.getFullLocation())
                    .build();
        }

        return PopularDestinationResponse.builder()
                .cityName(cleanCityName)
                .country("Unknown")
                .countryCode("XX")
                .flightCount(actualFlightCount)
                .fullLocation(cleanCityName)
                .build();
    }

    public List<PopularDestinationResponse> mapToPopularDestinationList(List<String> cityNames) {
        return cityNames.stream()
                .map(cityName -> toPopularDestinationResponse(cityName, 0))
                .collect(Collectors.toList());
    }

    private String cleanCityName(String cityName) {
        if (cityName == null) return "";

        return cityName.replaceAll("[0-9,]", "")
                .trim()
                .replaceAll("\\s+", " ");
    }

    private Integer extractFlightCount(String cityName) {
        if (cityName == null || !cityName.contains(",")) {
            return 0;
        }

        try {
            String[] parts = cityName.split(",");
            if (parts.length > 1) {
                String numberPart = parts[1].trim();
                return Integer.parseInt(numberPart);
            }
        } catch (NumberFormatException e) {
            log.warn(e.getMessage());
        }

        return 0;
    }
}