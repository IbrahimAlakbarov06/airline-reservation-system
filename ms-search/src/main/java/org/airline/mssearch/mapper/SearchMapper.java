package org.airline.mssearch.mapper;

import org.airline.mssearch.model.dto.response.CityResponse;
import org.airline.mssearch.model.dto.response.PopularDestinationResponse;
import org.airline.mssearch.model.enums.City;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        City city = City.fromCityName(cityName);

        if (city != null) {
            return PopularDestinationResponse.builder()
                    .cityName(city.getCityName())
                    .country(city.getCountryName())
                    .countryCode(city.getCountryCode())
                    .flightCount(flightCount)
                    .fullLocation(city.getFullLocation())
                    .build();
        }

        return PopularDestinationResponse.builder()
                .cityName(cityName)
                .country("Unknown")
                .countryCode("XX")
                .flightCount(flightCount)
                .fullLocation(cityName)
                .build();
    }

    public List<PopularDestinationResponse> mapToPopularDestinationList(List<String> cityNames) {
        return cityNames.stream()
                .map(cityName -> toPopularDestinationResponse(cityName, 0))
                .collect(Collectors.toList());
    }
}