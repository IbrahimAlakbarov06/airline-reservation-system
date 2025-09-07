package org.airline.msbooking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.msbooking.client.FlightClient;
import org.airline.msbooking.domain.entity.Booking;
import org.airline.msbooking.domain.entity.Passenger;
import org.airline.msbooking.domain.repo.BookingRepository;
import org.airline.msbooking.domain.repo.PassengerRepository;
import org.airline.msbooking.exception.BookingNotFoundException;
import org.airline.msbooking.exception.InvalidBookingException;
import org.airline.msbooking.mapper.BookingMapper;
import org.airline.msbooking.mapper.PassengerMapper;
import org.airline.msbooking.model.dto.response.BookingResponse;
import org.airline.msbooking.model.dto.response.FlightInfoResponse;
import org.airline.msbooking.model.dto.response.PassengerDetailResponse;
import org.airline.msbooking.model.enums.BookingStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminBookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final PassengerMapper passengerMapper;
    private final FlightClient flightClient;
    private final EventPublisher eventPublisher;
    private final PassengerRepository passengerRepository;

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();

        return buildListBookingResponse(bookings);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = findById(id);

        return buildBookingResponse(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponse getByBookingCode(String bookingCode) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode);

        return buildBookingResponse(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepository.findByBookingStatus(status);

        return buildListBookingResponse(bookings);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByUser(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return buildListBookingResponse(bookings);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingByFlightId(Long flightId) {
        List<Booking> bookings = bookingRepository.findByFlightId(flightId);

        return buildListBookingResponse(bookings);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getRecentBookings(Integer hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<Booking> bookings = bookingRepository.findRecentBookings(since);

        return buildListBookingResponse(bookings);
    }

    @Transactional(readOnly = true)
    public List<PassengerDetailResponse> findPassengersByBookingId(Long bookingId) {
        List<Passenger> passengers = passengerRepository.findPassengersByBookingId(bookingId);

        return passengerMapper.toPassengerResponseList(passengers);
    }

    @Transactional(readOnly = true)
    public List<PassengerDetailResponse> findByUserProfileId(Long userProfileId) {
        List<Passenger> passengers = passengerRepository.findByUserProfileId(userProfileId);

        return passengerMapper.toPassengerResponseList(passengers);
    }

    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = findById(id);

        validateBookingCanBeCancelled(booking);

        releaseSeatsForBooking(booking);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        eventPublisher.publishBookingCanceled(savedBooking);
    }

    @Transactional
    public BookingResponse confirmBooking(Long id) {
        Booking booking = findById(id);

        validateBookingCanBeConfirmed(booking);

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);
        eventPublisher.publishBookingConfirmed(savedBooking);

        return buildBookingResponse(savedBooking);
    }

    @Transactional
    public BookingResponse completeBooking(Long id) {
        Booking booking = findById(id);

        validateBookingCanBeCompleted(booking);

        booking.setBookingStatus(BookingStatus.COMPLETED);

        Booking savedBooking = bookingRepository.save(booking);
        eventPublisher.publishBookingCompleted(savedBooking);

        return buildBookingResponse(savedBooking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = findById(id);

        validateBookingCanBeDeleted(booking);

        if (booking.getBookingStatus() == BookingStatus.PENDING) {
            releaseSeatsForBooking(booking);
        }

        bookingRepository.delete(booking);
    }

    private Booking findById(Long id) {
        return bookingRepository.findById(id).
                orElseThrow(() -> new BookingNotFoundException("Booking with id " + id + " not found"));
    }

    private void validateBookingCanBeConfirmed(Booking booking) {
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingException("Only pending bookings can be confirmed");
        }
    }

    private void validateBookingCanBeCancelled(Booking booking) {
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException("Booking is already cancelled");
        }
        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new InvalidBookingException("Cannot cancel completed booking");
        }
    }

    private void validateBookingCanBeCompleted(Booking booking) {
        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidBookingException("Only confirmed bookings can be completed");
        }
    }

    private void validateBookingCanBeDeleted(Booking booking) {
        if (booking.getBookingStatus() == BookingStatus.CONFIRMED ||
                booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new InvalidBookingException("Cannot delete confirmed or completed bookings");
        }
    }

    private void releaseSeatsForBooking(Booking booking) {
        try {
            flightClient.releaseSeats(booking.getFlightId(),
                    booking.getFlightClass().name(), booking.getTotalPassengers());
        } catch (Exception e) {
            log.error("Failed to release seats for booking: {}", booking.getBookingCode(), e);
        }
    }

    private BookingResponse buildBookingResponse(Booking booking) {
        try {
            FlightInfoResponse flight = flightClient.getFlightForBooking(booking.getFlightId());

            List<PassengerDetailResponse> passengers = passengerMapper.toPassengerResponseList(booking.getPassengers());

            return bookingMapper.toResponse(booking, flight, passengers);
        } catch (BookingNotFoundException e) {
            List<PassengerDetailResponse> passengers = passengerMapper.toPassengerResponseList(booking.getPassengers());

            return bookingMapper.toResponse(booking, null, passengers);
        }
    }

    private List<BookingResponse> buildListBookingResponse(List<Booking> bookings) {
        return bookings.stream()
                .map(this::buildBookingResponse)
                .collect(Collectors.toList());
    }
}
