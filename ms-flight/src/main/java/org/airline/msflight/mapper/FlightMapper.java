package org.airline.msflight.mapper;

import org.airline.msflight.domain.entity.Aircraft;
import org.airline.msflight.domain.entity.Airport;
import org.airline.msflight.domain.entity.Flight;
import org.airline.msflight.model.dto.request.FlightCreateRequest;
import org.airline.msflight.model.dto.request.FlightUpdateRequest;
import org.airline.msflight.model.dto.response.FlightResponse;
import org.airline.msflight.model.dto.response.FlightBookingResponse;
import org.airline.msflight.model.dto.response.FlightPricingResponse;
import org.airline.msflight.model.dto.response.FlightSearchResponse;
import org.airline.msflight.model.enums.City;
import org.airline.msflight.model.enums.FlightClass;
import org.airline.msflight.model.enums.FlightStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlightMapper {

    @Autowired
    private AircraftMapper aircraftMapper;

    @Autowired
    private AirportMapper airportMapper;


    public Flight toEntity(FlightCreateRequest request, Aircraft aircraft, Airport originAirport, Airport destinationAirport) {
        if (request == null) {
            return null;
        }

        Flight flight = Flight.builder()
                .flightNumber(request.getFlightNumber())
                .aircraft(aircraft)
                .originAirport(originAirport)
                .destinationAirport(destinationAirport)
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .basePrice(request.getBasePrice())
                .status(FlightStatus.SCHEDULED)
                .gateNumber(request.getGateNumber())
                .terminal(request.getTerminal())
                .build();

        calculateClassPrices(flight, request.getBasePrice());

        return flight;
    }


    public FlightResponse toResponse(Flight flight) {
        if (flight == null) return null;

        return FlightResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .aircraft(aircraftMapper.toResponse(flight.getAircraft()))
                .originAirport(airportMapper.toResponse(flight.getOriginAirport()))
                .destinationAirport(airportMapper.toResponse(flight.getDestinationAirport()))
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .status(flight.getStatus())
                .basePrice(flight.getBasePrice())
                .economyPrice(flight.getEconomyPrice())
                .businessPrice(flight.getBusinessPrice())
                .firstClassPrice(flight.getFirstClassPrice())
                .availableEconomySeats(flight.getAvailableEconomySeats())
                .availableBusinessSeats(flight.getAvailableBusinessSeats())
                .availableFirstClassSeats(flight.getAvailableFirstClassSeats())
                .gateNumber(flight.getGateNumber())
                .terminal(flight.getTerminal())
                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())
                .build();
    }

    public FlightSearchResponse toSearchResponse(Flight flight, FlightClass flightClass) {
        if (flight == null) return null;

        BigDecimal price = getPriceByClass(flight, flightClass);
        Integer availableSeats = getAvailableSeatsByClass(flight, flightClass);

        return FlightSearchResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .originCode(flight.getOriginAirport().getAirportCode())
                .originCity(flight.getOriginAirport().getCity())
                .destinationCode(flight.getDestinationAirport().getAirportCode())
                .destinationCity(flight.getDestinationAirport().getCity())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .duration(calculateDuration(flight.getDepartureTime(), flight.getArrivalTime()))
                .price(price)
                .availableSeats(availableSeats)
                .aircraftType(flight.getAircraft().getModel())
                .build();
    }

    public FlightBookingResponse toBookingResponse(Flight flight) {
        if (flight == null) return null;

        return FlightBookingResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .originCode(flight.getOriginAirport().getAirportCode())
                .originCity(City.fromCityName(flight.getOriginAirport().getCity()))
                .destinationCode(flight.getDestinationAirport().getAirportCode())
                .destinationCity(City.fromCityName(flight.getDestinationAirport().getCity()))
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .aircraftType(flight.getAircraft().getModel())
                .status(flight.getStatus())
                .gateNumber(flight.getGateNumber())
                .terminal(flight.getTerminal())
                .build();
    }

    public FlightPricingResponse toPricingResponse(Flight flight) {
        if (flight == null) return null;

        Integer totalSeats = flight.getAircraft().getTotalSeats();
        Integer bookedSeats = totalSeats - flight.getTotalAvailableSeats();
        Double occupancyRate = totalSeats > 0 ? (double) bookedSeats / totalSeats * 100 : 0.0;

        return FlightPricingResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .basePrice(flight.getBasePrice())
                .economyPrice(flight.getEconomyPrice())
                .businessPrice(flight.getBusinessPrice())
                .firstClassPrice(flight.getFirstClassPrice())
                .totalSeats(totalSeats)
                .bookedSeats(bookedSeats)
                .occupancyRate(occupancyRate)
                .build();
    }


    public List<FlightResponse> toResponseList(List<Flight> flights) {
        if (flights == null) return null;
        return flights.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<FlightBookingResponse> toBookingResponseList(List<Flight> flights) {
        if (flights == null) return null;
        return flights.stream().map(this::toBookingResponse).collect(Collectors.toList());
    }

    public List<FlightPricingResponse> toPricingResponseList(List<Flight> flights) {
        if (flights == null) return null;
        return flights.stream().map(this::toPricingResponse).collect(Collectors.toList());
    }

    public void updateEntityFromRequest(Flight flight, FlightUpdateRequest request) {
        if (request == null || flight == null) return;

        if (request.getFlightNumber() != null) {
            flight.setFlightNumber(request.getFlightNumber());
        }
        if (request.getDepartureTime() != null) {
            flight.setDepartureTime(request.getDepartureTime());
        }
        if (request.getArrivalTime() != null) {
            flight.setArrivalTime(request.getArrivalTime());
        }
        if (request.getStatus() != null) {
            flight.setStatus(request.getStatus());
        }
        if (request.getBasePrice() != null) {
            flight.setBasePrice(request.getBasePrice());
            recalculateClassPrices(flight);
        }
        if (request.getGateNumber() != null) {
            flight.setGateNumber(request.getGateNumber());
        }
        if (request.getTerminal() != null) {
            flight.setTerminal(request.getTerminal());
        }
    }

    private void calculateClassPrices(Flight flight, BigDecimal basePrice) {
        flight.setEconomyPrice(FlightClass.ECONOMY.calculatePrice(basePrice));
        flight.setBusinessPrice(FlightClass.BUSINESS.calculatePrice(basePrice));
        flight.setFirstClassPrice(FlightClass.FIRST_CLASS.calculatePrice(basePrice));
    }

    private BigDecimal getPriceByClass(Flight flight, FlightClass flightClass) {
        return switch (flightClass) {
            case ECONOMY -> flight.getEconomyPrice();
            case BUSINESS -> flight.getBusinessPrice();
            case FIRST_CLASS -> flight.getFirstClassPrice();
        };
    }

    private Integer getAvailableSeatsByClass(Flight flight, FlightClass flightClass) {
        return switch (flightClass) {
            case ECONOMY -> flight.getAvailableEconomySeats();
            case BUSINESS -> flight.getAvailableBusinessSeats();
            case FIRST_CLASS -> flight.getAvailableFirstClassSeats();
        };
    }

    private String calculateDuration(LocalDateTime departure, LocalDateTime arrival) {
        Duration duration = Duration.between(departure, arrival);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    private void recalculateClassPrices(Flight flight) {
        BigDecimal basePrice = flight.getBasePrice();
        calculateClassPrices(flight, basePrice);
    }
}