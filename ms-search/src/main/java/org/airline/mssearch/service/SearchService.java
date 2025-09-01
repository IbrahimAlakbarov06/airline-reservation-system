package org.airline.mssearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.mssearch.client.FlightClient;
import org.airline.mssearch.exception.FlightServiceException;
import org.airline.mssearch.exception.SearchException;
import org.airline.mssearch.mapper.SearchMapper;
import org.airline.mssearch.model.dto.request.FlightSearchRequest;
import org.airline.mssearch.model.dto.response.CityResponse;
import org.airline.mssearch.model.dto.response.FlightSearchResponse;
import org.airline.mssearch.model.dto.response.PopularDestinationResponse;
import org.airline.mssearch.model.enums.City;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final FlightClient flightClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SearchMapper searchMapper;

    private static final String FLIGHT_SEARCH_CACHE = "flight_search:";
    private static final String POPULAR_DESTINATIONS_CACHE = "popular_destinations";
    private static final String CITIES_CACHE = "cities";

    public List<FlightSearchResponse> search(FlightSearchRequest request) {
        String cacheKey = FLIGHT_SEARCH_CACHE +
                request.getOriginCity() + "_" + request.getDestinationCity() + "_" +
                request.getDepartureDate() + "_" + request.getFlightClass();

        try {
            @SuppressWarnings("unchecked")
            List<FlightSearchResponse> cached = (List<FlightSearchResponse>) redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                log.info("Returning cached search results");
                return cached;
            }
        } catch (Exception e) {
            log.warn("Cache error: {}", e.getMessage());
        }

        try {
            List<FlightSearchResponse> flights = flightClient.searchFlights(request);
            redisTemplate.opsForValue().set(cacheKey, flights, 5, TimeUnit.MINUTES);
            return flights;
        } catch (Exception e) {
            throw new SearchException("Flight search failed: " + e.getMessage());
        }
    }

    public FlightSearchResponse getFlightDetails(Long flightId, String flightClass) {
        try {
            return flightClient.getFlightDetails(flightId, flightClass);
        } catch (Exception e) {
            throw new FlightServiceException("FLight details unavailable: " + e.getMessage());
        }
    }

    public List<CityResponse> getAllCities() {
        try {
            @SuppressWarnings("unchecked")
            List<CityResponse> cached = (List<CityResponse>) redisTemplate.opsForValue().get(CITIES_CACHE);
            if (cached != null) {
                return cached;
            }
        } catch (Exception e) {
            log.warn("Cache error: {}", e.getMessage());
        }

        try {
            List<City> cities = flightClient.getAllCities();
            List<CityResponse> citiesResponse = searchMapper.mapCitiesToResponseList(cities);
            redisTemplate.opsForValue().set(CITIES_CACHE, citiesResponse, 24, TimeUnit.HOURS);
            return citiesResponse;
        } catch (Exception e) {
            throw new FlightServiceException("Cities unavailable: " + e.getMessage());
        }
    }

    public List<PopularDestinationResponse> getPopularDestinations() {
        try {
            @SuppressWarnings("unchecked")
            List<PopularDestinationResponse> cached = (List<PopularDestinationResponse>) redisTemplate.opsForValue().get(POPULAR_DESTINATIONS_CACHE);
            if (cached != null) {
                return cached;
            }
        } catch (Exception e) {
            log.warn("Cache error: {}", e.getMessage());
        }

        try {
            List<String> popularDestinations = flightClient.getPopularDestinations();
            List<PopularDestinationResponse> responses = searchMapper.mapToPopularDestinationList(popularDestinations);
            redisTemplate.opsForValue().set(POPULAR_DESTINATIONS_CACHE, responses, 1, TimeUnit.HOURS);
            return responses;
        } catch (Exception e) {
            throw new FlightServiceException("Popular destinations unavailable: " + e.getMessage());
        }
    }

    public boolean checkFlightAvailability(Long flightId, String flightClass, Integer passengers) {
        try {
            return flightClient.checkSeatAvailability(flightId, flightClass, passengers);
        } catch (Exception e) {
            log.error("Error checking availability: {}", e.getMessage());
            return false;
        }
    }

    public List<PopularDestinationResponse> getFallbackPopularDestinations() {
        List<String> fallbackCities = Arrays.asList("Istanbul", "Dubai", "London", "Paris", "New York");
        return searchMapper.mapToPopularDestinationList(fallbackCities);
    }
}
