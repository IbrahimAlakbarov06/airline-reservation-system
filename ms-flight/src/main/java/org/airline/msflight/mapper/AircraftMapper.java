package org.airline.msflight.mapper;

import org.airline.msflight.domain.entity.Aircraft;
import org.airline.msflight.model.dto.request.AircraftCreateRequest;
import org.airline.msflight.model.dto.request.AircraftUpdateRequest;
import org.airline.msflight.model.dto.response.AircraftResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AircraftMapper {

    public Aircraft toEntity(AircraftCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Aircraft.builder()
                .aircraftCode(request.getAircraftCode())
                .model(request.getModel())
                .manufacturer(request.getManufacturer())
                .totalSeats(request.getTotalSeats())
                .economySeats(request.getEconomySeats())
                .businessSeats(request.getBusinessSeats())
                .firstClassSeats(request.getFirstClassSeats())
                .maxRangeKm(request.getMaxRangeKm())
                .cruiseSpeedKmh(request.getCruiseSpeedKmh())
                .isActive(request.getIsActive())
                .build();

    }

    public AircraftResponse toResponse(Aircraft aircraft) {
        if (aircraft == null) return null;

        return AircraftResponse.builder()
                .id(aircraft.getId())
                .aircraftCode(aircraft.getAircraftCode())
                .model(aircraft.getModel())
                .manufacturer(aircraft.getManufacturer())
                .totalSeats(aircraft.getTotalSeats())
                .economySeats(aircraft.getEconomySeats())
                .businessSeats(aircraft.getBusinessSeats())
                .firstClassSeats(aircraft.getFirstClassSeats())
                .fullName(aircraft.getFullName())
                .build();
    }


    public List<AircraftResponse> toResponseList(List<Aircraft> aircrafts) {
        return aircrafts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(Aircraft aircraft, AircraftUpdateRequest request) {
        if (request == null || aircraft == null) {
            return;
        }

        if (request.getAircraftCode() != null) {
            aircraft.setAircraftCode(request.getAircraftCode());
        }
        if (request.getModel() != null) {
            aircraft.setModel(request.getModel());
        }
        if (request.getManufacturer() != null) {
            aircraft.setManufacturer(request.getManufacturer());
        }
        if (request.getTotalSeats() != null) {
            aircraft.setTotalSeats(request.getTotalSeats());
        }
        if (request.getEconomySeats() != null) {
            aircraft.setEconomySeats(request.getEconomySeats());
        }
        if (request.getBusinessSeats() != null) {
            aircraft.setBusinessSeats(request.getBusinessSeats());
        }
        if (request.getFirstClassSeats() != null) {
            aircraft.setFirstClassSeats(request.getFirstClassSeats());
        }
        if (request.getMaxRangeKm() != null) {
            aircraft.setMaxRangeKm(request.getMaxRangeKm());
        }
        if (request.getCruiseSpeedKmh() != null) {
            aircraft.setCruiseSpeedKmh(request.getCruiseSpeedKmh());
        }
        if (request.getIsActive() != null) {
            aircraft.setIsActive(request.getIsActive());
        }
    }
}
