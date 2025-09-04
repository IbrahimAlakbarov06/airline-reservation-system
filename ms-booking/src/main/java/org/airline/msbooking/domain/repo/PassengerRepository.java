package org.airline.msbooking.domain.repo;

import org.airline.msbooking.domain.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    @Query("SELECT p FROM Passenger p WHERE p.booking.id = :bookingId")
    List<Passenger> findPassengersByBookingId(@Param("bookingId") Long bookingId);

    @Query("SELECT p FROM Passenger p WHERE p.userProfileId = :userProfileId")
    List<Passenger> findByUserProfileId(@Param("userProfileId") Long userProfileId);

    @Query("SELECT p FROM Passenger p WHERE p.booking.id = :bookingId AND p.isMainPassenger = true")
    Passenger findMainPassengerByBookingId(@Param("bookingId") Long bookingId);
}
