package org.airline.msflight.domain.repo;

import org.airline.msflight.domain.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByAircraftCode(String aircraftCode);

    boolean existsByAircraftCode(String aircraftCode);

    @Query("select a from Aircraft a where a.isActive = true")
    List<Aircraft> findActiveAircrafts();

    @Query("select a from Aircraft a where a.isActive = false ")
    List<Aircraft> findInActiveAircrafts();
}
