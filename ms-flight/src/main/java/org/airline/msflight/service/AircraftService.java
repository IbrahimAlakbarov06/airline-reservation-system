package org.airline.msflight.service;

import lombok.RequiredArgsConstructor;
import org.airline.msflight.domain.entity.Aircraft;
import org.airline.msflight.domain.repo.AircraftRepository;
import org.airline.msflight.exception.AircraftNotFoundException;
import org.airline.msflight.exception.AlreadyExistsException;
import org.airline.msflight.mapper.AircraftMapper;
import org.airline.msflight.model.dto.request.AircraftCreateRequest;
import org.airline.msflight.model.dto.request.AircraftUpdateRequest;
import org.airline.msflight.model.dto.response.AircraftResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AircraftService {
    private final AircraftRepository aircraftRepository;
    private final AircraftMapper aircraftMapper;

    @Transactional
    public AircraftResponse createAircraft(AircraftCreateRequest request) {
        if (aircraftRepository.existsByAircraftCode(request.getAircraftCode())) {
            throw new AlreadyExistsException("Aircraft already exists");
        }

        Aircraft aircraft = aircraftMapper.toEntity(request);
        Aircraft savedAircraft = aircraftRepository.save(aircraft);
        return aircraftMapper.toResponse(savedAircraft);
    }

    @Transactional(readOnly = true)
    public AircraftResponse getAircraftByCode(String aircraftCode) {
        Aircraft aircraft = aircraftRepository.findByAircraftCode(aircraftCode)
                .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with code " + aircraftCode));

        return aircraftMapper.toResponse(aircraft);
    }

    @Transactional(readOnly = true)
    public AircraftResponse getAircraftById(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with id " + id));

        return aircraftMapper.toResponse(aircraft);
    }

    @Transactional(readOnly = true)
    public List<AircraftResponse> getAllAircrafts() {
        List<Aircraft> aircrafts = aircraftRepository.findAll();

        return aircraftMapper.toResponseList(aircrafts);
    }

    @Transactional(readOnly = true)
    public List<AircraftResponse> getActiveAircraft() {
        List<Aircraft> aircrafts = aircraftRepository.findActiveAircrafts();

        return aircraftMapper.toResponseList(aircrafts);
    }

    @Transactional(readOnly = true)
    public List<AircraftResponse> getInActiveAircraft() {
        List<Aircraft> aircrafts = aircraftRepository.findInActiveAircrafts();

        return aircraftMapper.toResponseList(aircrafts);
    }

    @Transactional
    public AircraftResponse updateAircraft(Long id, AircraftUpdateRequest request) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with id " + id));

        aircraftMapper.updateEntityFromRequest(aircraft, request);
        Aircraft updatedAircraft = aircraftRepository.save(aircraft);

        return aircraftMapper.toResponse(updatedAircraft);
    }

    @Transactional
    public void deleteAircraft(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with id " + id));
        aircraftRepository.delete(aircraft);
    }

    @Transactional
    public void deactivateAircraft(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException("Aircraft not found with id: " + id));
        aircraft.setIsActive(false);
        aircraftRepository.save(aircraft);
    }
}
