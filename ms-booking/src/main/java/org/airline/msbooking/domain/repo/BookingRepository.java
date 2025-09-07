package org.airline.msbooking.domain.repo;

import org.airline.msbooking.domain.entity.Booking;
import org.airline.msbooking.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByBookingCode(String bookingCode);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    List<Booking> findByFlightId(Long flightId);

    List<Booking> findByUserId(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.createdAt >= :since ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookings(@Param("since") LocalDateTime since);

    Booking findByBookingCode(String bookingCode);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.flightId = :flightId AND b.bookingStatus IN ('CONFIRMED', 'COMPLETED')")
    Long countConfirmedBookingsByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT SUM(b.totalPassengers) FROM Booking b WHERE b.flightId = :flightId AND b.bookingStatus IN ('CONFIRMED', 'COMPLETED')")
    Integer countPassengersByFlightId(@Param("flightId") Long flightId);


}
