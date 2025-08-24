package org.airline.msflight.service;

import lombok.RequiredArgsConstructor;
import org.airline.msflight.domain.entity.Airport;
import org.airline.msflight.domain.repo.AirportRepository;
import org.airline.msflight.exception.AirportNotFoundException;
import org.airline.msflight.exception.AlreadyExistsException;
import org.airline.msflight.mapper.AirportMapper;
import org.airline.msflight.model.dto.request.AirportCreateRequest;
import org.airline.msflight.model.dto.request.AirportUpdateRequest;
import org.airline.msflight.model.dto.response.AirportResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AirportService {
    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    @Transactional
    public AirportResponse createAirport(AirportCreateRequest request) {
        if (airportRepository.existsByAirportCode(request.getAirportCode())) {
            throw new AlreadyExistsException("Airport already exists");
        }

        Airport airport = airportMapper.toEntity(request);
        Airport savedAirport = airportRepository.save(airport);
        return airportMapper.toResponse(savedAirport);
    }

    @Transactional(readOnly = true)
    public AirportResponse getAirportById(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found with id: " + id));

        return airportMapper.toResponse(airport);
    }

    @Transactional(readOnly = true)
    public AirportResponse getAirportByCode(String code) {
        Airport airport = airportRepository.findByAirportCode(code)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found with id: " + code));

        return airportMapper.toResponse(airport);
    }

    @Transactional(readOnly = true)
    public List<AirportResponse> getAllAirports() {
        List<Airport> airports = airportRepository.findAll();

        return airportMapper.toResponseList(airports);
    }

    @Transactional
    public AirportResponse updateAirport(Long id, AirportUpdateRequest request) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found with id: " + id));

        airportMapper.updateEntityFromRequest(airport,request);
        Airport updatedAirport = airportRepository.save(airport);
        return airportMapper.toResponse(updatedAirport);
    }

    @Transactional
    public void deleteAirport(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found"));
        airportRepository.delete(airport);
    }

    @Transactional
    public void deactivateAirport(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found"));
        airport.setIsActive(false);
        airportRepository.save(airport);
    }

    @Transactional
    public void activateAirport(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found"));
        airport.setIsActive(true);
        airportRepository.save(airport);
    }

    @Transactional(readOnly = true)
    public List<AirportResponse> getActiveAirports() {
        List<Airport> airports = airportRepository.findActiveAirports();
        return airportMapper.toResponseList(airports);
    }

    @Transactional(readOnly = true)
    public List<AirportResponse> getInactiveAirports() {
        List<Airport> airports = airportRepository.findInActiveAirports();
        return airportMapper.toResponseList(airports);
    }

    @Transactional(readOnly = true)
    public List<AirportResponse> getAirportsByCity(String city) {
        List<Airport> airports = airportRepository.findByCity(city);
        return airportMapper.toResponseList(airports);
    }
}
