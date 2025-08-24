package org.airline.msflight.domain.repo;

import org.airline.msflight.domain.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByAirportCode(String airportCode);

    boolean existsByAirportCode(String airportCode);

    @Query("select a from Airport a where a.isActive = true")
    List<Airport> findActiveAirports();

    @Query("select a from Airport a where a.isActive = false ")
    List<Airport> findInActiveAirports();

    @Query("select a from Airport a where upper(a.city) = upper(:city) order by a.airportName asc ")
    List<Airport> findByCity(@Param("city") String city);
}
