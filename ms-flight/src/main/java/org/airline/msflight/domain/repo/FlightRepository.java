package org.airline.msflight.domain.repo;

import org.airline.msflight.domain.entity.Flight;
import org.airline.msflight.model.enums.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByStatus(FlightStatus status);

    @Query("select f from Flight f where f.originAirport.id = :originAirportId and f.destinationAirport.id = :destinationAirportId order by f.departureTime asc")
    List<Flight> findByRoute(@Param("originId") Long originId, @Param("destinationId") Long destinationId);

    @Query("select f from Flight f " +
            "join f.originAirport oa " +
            "join f.destinationAirport da " +
            "where upper(oa.city) = upper(:originCity) " +
            "and upper(da.city) = upper(:destinationCity) " +
            "and f.departureTime between :startTime and :endTime" +
            "and f.status = :status order by f.departureTime asc")
    List<Flight> searchFlights(@Param("originCity") String originCity,
                               @Param("destinationCity") String destinationCity,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime,
                               @Param("status") FlightStatus status);


    @Query("select f from Flight f where f.departureTime between :startDate and :endDate")
    List<Flight> findByDepartureDateRange(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("select da.city, count(f) as flightcount from Flight f join f.destinationAirport da where f.status = 'SCHEDULED' group by da.city order by flightcount desc")
    List<String> findPopularDestinations();

    @Query("select f from Flight f where date(f.departureTime) = current_date order by f.departureTime asc")
    List<Flight> findTodaysFlights();

    @Query("select f from Flight f where f.status in ('SCHEDULED', 'BOARDING') and f.departureTime > :now order by f.departureTime asc")
    List<Flight> findUpcomingFlights(@Param("now") LocalDateTime now);
}
