package org.airline.mssearch.service;

import lombok.extern.slf4j.Slf4j;
import org.airline.mssearch.model.dto.response.FlightSearchResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}