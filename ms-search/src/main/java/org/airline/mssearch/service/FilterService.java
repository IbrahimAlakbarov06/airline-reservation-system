package org.airline.mssearch.service;

import lombok.extern.slf4j.Slf4j;
import org.airline.mssearch.model.dto.response.FlightSearchResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilterService {

    public List<FlightSearchResponse> sortByPrice(List<FlightSearchResponse> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(FlightSearchResponse::getPrice))
                .collect(Collectors.toList());
    }

    public List<FlightSearchResponse> sortByDeparture(List<FlightSearchResponse> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(FlightSearchResponse::getDepartureTime))
                .collect(Collectors.toList());
    }

    public List<FlightSearchResponse> sortByDuration(List<FlightSearchResponse> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(FlightSearchResponse::getDuration))
                .collect(Collectors.toList());
    }

    public List<FlightSearchResponse> filterByMaxPrice(List<FlightSearchResponse> flights, BigDecimal maxPrice) {
        return flights.stream()
                .filter(flight -> flight.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    public List<FlightSearchResponse> filterByMinPrice(List<FlightSearchResponse> flights, BigDecimal minPrice) {
        return flights.stream()
                .filter(flight -> flight.getPrice().compareTo(minPrice) >= 0)
                .collect(Collectors.toList());
    }

    public List<FlightSearchResponse> filterByPriceRange(List<FlightSearchResponse> flights,
                                                         BigDecimal minPrice, BigDecimal maxPrice) {
        return flights.stream()
                .filter(flight -> flight.getPrice().compareTo(minPrice) >= 0 &&
                        flight.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    public List<FlightSearchResponse> filterByMinSeats(List<FlightSearchResponse> flights, Integer minSeats) {
        return flights.stream()
                .filter(flight -> flight.getAvailableSeats() >= minSeats)
                .collect(Collectors.toList());
    }

    public List<FlightSearchResponse> filterByDepartureTimeRange(List<FlightSearchResponse> flights,
                                                                 LocalDateTime startTime, LocalDateTime endTime) {
        return flights.stream()
                .filter(flight -> !flight.getDepartureTime().isBefore(startTime) &&
                        !flight.getDepartureTime().isAfter(endTime))
                .collect(Collectors.toList());
    }

    public List<FlightSearchResponse> filterByAircraft(List<FlightSearchResponse> flights, String aircraftType) {
        return flights.stream()
                .filter(flight -> flight.getAircraftType().equalsIgnoreCase(aircraftType))
                .collect(Collectors.toList());
    }

    public FlightSearchResponse findCheapest(List<FlightSearchResponse> flights) {
        return flights.stream()
                .min(Comparator.comparing(FlightSearchResponse::getPrice))
                .orElse(null);
    }

    public FlightSearchResponse findFastest(List<FlightSearchResponse> flights) {
        return flights.stream()
                .min(Comparator.comparing(FlightSearchResponse::getDuration))
                .orElse(null);
    }

    public FlightSearchResponse findEarliest(List<FlightSearchResponse> flights) {
        return flights.stream()
                .min(Comparator.comparing(FlightSearchResponse::getDepartureTime))
                .orElse(null);
    }

    public FlightSearchResponse findLatest(List<FlightSearchResponse> flights) {
        return flights.stream()
                .max(Comparator.comparing(FlightSearchResponse::getDepartureTime))
                .orElse(null);
    }

    public List<FlightSearchResponse> filterAndSort(List<FlightSearchResponse> flights,
                                                    BigDecimal maxPrice,
                                                    Integer minSeats,
                                                    String sortBy) {
        List<FlightSearchResponse> filtered = flights.stream()
                .filter(flight -> maxPrice == null || flight.getPrice().compareTo(maxPrice) <= 0)
                .filter(flight -> minSeats == null || flight.getAvailableSeats() >= minSeats)
                .collect(Collectors.toList());

        return switch (sortBy.toLowerCase()) {
            case "price" -> sortByPrice(filtered);
            case "departure" -> sortByDeparture(filtered);
            case "duration" -> sortByDuration(filtered);
            default -> filtered;
        };
    }
}