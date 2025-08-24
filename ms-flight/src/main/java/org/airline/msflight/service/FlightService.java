package org.airline.msflight.service;

import lombok.RequiredArgsConstructor;
import org.airline.msflight.domain.entity.Aircraft;
import org.airline.msflight.domain.entity.Airport;
import org.airline.msflight.domain.entity.Flight;
import org.airline.msflight.domain.repo.AircraftRepository;
import org.airline.msflight.domain.repo.AirportRepository;
import org.airline.msflight.domain.repo.FlightRepository;
import org.airline.msflight.exception.AircraftNotFoundException;
import org.airline.msflight.exception.AirportNotFoundException;
import org.airline.msflight.exception.FlightNotFoundException;
import org.airline.msflight.mapper.FlightMapper;
import org.airline.msflight.model.dto.request.FlightCreateRequest;
import org.airline.msflight.model.dto.request.FlightSearchRequest;
import org.airline.msflight.model.dto.request.FlightUpdateRequest;
import org.airline.msflight.model.dto.response.FlightBookingResponse;
import org.airline.msflight.model.dto.response.FlightPricingResponse;
import org.airline.msflight.model.dto.response.FlightResponse;
import org.airline.msflight.model.dto.response.FlightSearchResponse;
import org.airline.msflight.model.enums.FlightClass;
import org.airline.msflight.model.enums.FlightStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AirportRepository airportRepository;
    private final AircraftRepository aircraftRepository;

    @Transactional
    public FlightResponse createFlight(FlightCreateRequest request) {
        Aircraft aircraft = aircraftRepository.findById(request.getAircraftId())
                .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with id: " + request.getAircraftId()));

        Airport originAirport = airportRepository.findById(request.getOriginAirportId())
                .orElseThrow(() -> new AirportNotFoundException("Origin airport not found with id: " + request.getOriginAirportId()));

        Airport destinationAirport = airportRepository.findById(request.getDestinationAirportId())
                .orElseThrow(() -> new AirportNotFoundException("Destination airport not found with id: " + request.getDestinationAirportId()));

        Flight flight = flightMapper.toEntity(request, aircraft, originAirport, destinationAirport);
        Flight savedFlight = flightRepository.save(flight);

        return flightMapper.toResponse(savedFlight);
    }

    @Transactional
    public FlightResponse updateFlight(Long id, FlightUpdateRequest request) {
        Flight flight = getFlightByFlightId(id);

        flightMapper.updateEntityFromRequest(flight, request);
        Flight updatedFlight = flightRepository.save(flight);
        return flightMapper.toResponse(updatedFlight);
    }

    @Transactional
    public void deleteFlight(Long id) {
        Flight flight = getFlightByFlightId(id);

        flightRepository.delete(flight);
    }

    @Transactional(readOnly = true)
    public FlightResponse getFlightById(Long id) {
        Flight flight = getFlightByFlightId(id);

        return flightMapper.toResponse(flight);
    }

    @Transactional(readOnly = true)
    public FlightResponse getByFlightNumber(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with flight number: " + flightNumber));

        return flightMapper.toResponse(flight);
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return flightMapper.toResponseList(flights);
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> getFlightsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Flight> flights = flightRepository.findByDepartureDateRange(startDate, endDate);
        return flightMapper.toResponseList(flights);
    }

    @Transactional
    public FlightResponse updateFlightStatus(Long id, FlightStatus status) {
        Flight flight = getFlightByFlightId(id);

        flight.setStatus(status);
        flightRepository.save(flight);
        return flightMapper.toResponse(flight);
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> getFlightByStatus(FlightStatus status) {
        List<Flight> flights = flightRepository.findByStatus(status);
        return flightMapper.toResponseList(flights);
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> getFlightsByRoute(Long originAirportId, Long destinationAirportId) {
        List<Flight> flights = flightRepository.findByRoute(originAirportId, destinationAirportId);
        return flightMapper.toResponseList(flights);
    }

    @Transactional(readOnly = true)
    public FlightBookingResponse getFlightForBooking(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        return flightMapper.toBookingResponse(flight);
    }

    @Transactional(readOnly = true)
    public FlightPricingResponse getFlightForPricing(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        return flightMapper.toPricingResponse(flight);
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> upcomingFlights() {
        List<Flight> flights = flightRepository.findUpcomingFlights(LocalDateTime.now());
        return flightMapper.toResponseList(flights);
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> todaysFlights() {
        List<Flight> flights = flightRepository.findTodaysFlights();
        return flightMapper.toResponseList(flights);
    }

    @Transactional(readOnly = true)
    public List<String> getPopularDestinations() {
        return flightRepository.findPopularDestinations();
    }

    @Transactional(readOnly = true)
    public FlightSearchResponse getFlightForSearch(Long flightId, String flightClass) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));

        FlightClass fc = FlightClass.fromString(flightClass);
        return flightMapper.toSearchResponse(flight, fc);
    }

    @Transactional(readOnly = true)
    public List<FlightSearchResponse> searchFlights(FlightSearchRequest request) {
        LocalDateTime startOfDay = request.getDepartureDate().atStartOfDay();
        LocalDateTime endOfDay = request.getDepartureDate().atTime(23, 59, 59);

        String originCity = request.getOriginCity().getCityName();
        String destinationCity = request.getDestinationCity().getCityName();

        List<Flight> flights = flightRepository.searchFlights(
                originCity, destinationCity, startOfDay, endOfDay, FlightStatus.SCHEDULED);

        FlightClass flightClass = request.getFlightClass();

        return flights.stream()
                .filter(flight -> hasAvailableSeats(flight, flightClass, request.getPassengerCount()))
                .map(flight -> flightMapper.toSearchResponse(flight, flightClass))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean reserveSeats(Long flightId, String flightClass, Integer seatCount) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));

        FlightClass fc = FlightClass.fromString(flightClass);

        if (!hasAvailableSeats(flight, fc, seatCount)) {
            return false;
        }

        updateAvailableSeats(flight, fc, -seatCount);
        flightRepository.save(flight);
        return true;
    }

    @Transactional
    public void releaseSeats(Long flightId, String flightClass, Integer seatCount) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));

        FlightClass fc = FlightClass.fromString(flightClass);
        updateAvailableSeats(flight, fc, seatCount);
        flightRepository.save(flight);
    }

    @Transactional
    public boolean checkSeatAvailability(Long flightId, String flightClass, Integer seatCount) {
        Flight flight = getFlightByFlightId(flightId);

        FlightClass fc = FlightClass.fromString(flightClass);
        return hasAvailableSeats(flight, fc, seatCount);
    }

    private boolean hasAvailableSeats(Flight flight, FlightClass flightClass, Integer requestedSeats) {
        return switch (flightClass) {
            case ECONOMY -> flight.getAvailableEconomySeats() >= requestedSeats;
            case BUSINESS -> flight.getAvailableBusinessSeats() >= requestedSeats;
            case FIRST_CLASS -> flight.getAvailableFirstClassSeats() >= requestedSeats;
        };
    }

    private void updateAvailableSeats(Flight flight, FlightClass flightClass, Integer seatChange) {
        switch (flightClass) {
            case ECONOMY -> {
                int currentSeats = flight.getAvailableEconomySeats();
                int maxSeats = flight.getAircraft().getEconomySeats();
                int newSeats = currentSeats + seatChange;

                if (newSeats < 0) newSeats = 0;
                if (newSeats > maxSeats) newSeats = maxSeats;

                flight.setAvailableEconomySeats(newSeats);
            }
            case BUSINESS -> {
                int currentSeats = flight.getAvailableBusinessSeats();
                int maxSeats = flight.getAircraft().getBusinessSeats();
                int newSeats = currentSeats + seatChange;

                if (newSeats < 0) newSeats = 0;
                if (newSeats > maxSeats) newSeats = maxSeats;

                flight.setAvailableBusinessSeats(newSeats);
            }
            case FIRST_CLASS -> {
                int currentSeats = flight.getAvailableFirstClassSeats();
                int maxSeats = flight.getAircraft().getFirstClassSeats();
                int newSeats = currentSeats + seatChange;

                if (newSeats < 0) newSeats = 0;
                if (newSeats > maxSeats) newSeats = maxSeats;

                flight.setAvailableFirstClassSeats(newSeats);
            }
        }
    }

    private Flight getFlightByFlightId(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
        return flight;
    }
}