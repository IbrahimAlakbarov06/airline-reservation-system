package org.airline.mspricing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class OccupancyTrackingService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String OCCUPANCY_PREFIX = "occupancy:flight:";

    public void updateOccupancy(Long flightId, Integer passengerCount, boolean isReserved) {
        String key = OCCUPANCY_PREFIX + flightId;

        try {
            if (isReserved) {
                redisTemplate.opsForValue().increment(key, passengerCount);
            } else {
                redisTemplate.opsForValue().increment(key, -passengerCount);
            }

            redisTemplate.expire(key, Duration.ofHours(48));
        } catch (Exception e) {
            log.error("Failed to update occupancy for flight: {}", flightId, e);
        }
    }

    public Integer getCurrentOccupancy(Long flightId) {
        String key = OCCUPANCY_PREFIX + flightId;
        String occupancy = redisTemplate.opsForValue().get(key);
        return occupancy != null ? Integer.parseInt(occupancy) : 0;
    }
}
