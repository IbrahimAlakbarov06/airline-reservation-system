package org.airline.msflight.mapper;

import org.airline.msflight.domain.entity.Airport;
import org.airline.msflight.model.dto.request.AirportCreateRequest;
import org.airline.msflight.model.dto.request.AirportUpdateRequest;
import org.airline.msflight.model.dto.response.AirportResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AirportMapper {

    public Airport toEntity(AirportCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Airport.builder()
                .airportCode(request.getAirportCode())
                .airportName(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .timezone(request.getTimezone())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isActive(request.getIsActive())
                .build();
    }

    public AirportResponse toResponse(Airport airport) {
        if (airport == null) {
            return null;
        }

        return AirportResponse.builder()
                .id(airport.getId())
                .airportCode(airport.getAirportCode())
                .airportName(airport.getAirportName())
                .city(airport.getCity())
                .country(airport.getCountry())
                .timezone(airport.getTimezone())
                .fullName(airport.getFullName())
                .location(airport.getLocation())
                .build();
    }

    public List<AirportResponse> toResponseList(List<Airport> airports) {
        return airports.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(Airport airport, AirportUpdateRequest request) {
        if (request == null || airport == null) return;

        if (request.getAirportCode() != null) {
            airport.setAirportCode(request.getAirportCode());
        }
        if (request.getName() != null) {
            airport.setAirportName(request.getName());
        }
        if (request.getCity() != null) {
            airport.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            airport.setCountry(request.getCountry());
        }
        if (request.getTimezone() != null) {
            airport.setTimezone(request.getTimezone());
        }
        if (request.getLatitude() != null) {
            airport.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            airport.setLongitude(request.getLongitude());
        }
        if (request.getIsActive() != null) {
            airport.setIsActive(request.getIsActive());
        }
    }
}
