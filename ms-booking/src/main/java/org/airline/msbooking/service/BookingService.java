package org.airline.msbooking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.msbooking.client.FlightClient;
import org.airline.msbooking.client.PricingClient;
import org.airline.msbooking.client.UserClient;
import org.airline.msbooking.domain.entity.Booking;
import org.airline.msbooking.domain.entity.Passenger;
import org.airline.msbooking.domain.repo.BookingRepository;
import org.airline.msbooking.exception.BookingNotFoundException;
import org.airline.msbooking.exception.FlightNotAvailableException;
import org.airline.msbooking.exception.InvalidBookingException;
import org.airline.msbooking.mapper.BookingMapper;
import org.airline.msbooking.mapper.PassengerMapper;
import org.airline.msbooking.model.dto.request.BookingCreateRequest;
import org.airline.msbooking.model.dto.request.PassengerDetailRequest;
import org.airline.msbooking.model.dto.request.PricingRequest;
import org.airline.msbooking.model.dto.response.*;
import org.airline.msbooking.model.enums.BookingStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final PassengerMapper passengerMapper;
    private final FlightClient flightClient;
    private final PricingClient pricingClient;
    private final UserClient userClient;
    private final EventPublisher eventPublisher;

    public BookingResponse createBooking(BookingCreateRequest request, String userIdHeader) {
        try {
            Long userId = Long.parseLong(userIdHeader);

            FlightInfoResponse flight = flightClient.getFlightForBooking(request.getFlightId());
            UserProfileResponse user = userClient.getCurrentUserProfile(userIdHeader);

            if (!user.getIsProfileComplete()) {
                throw new InvalidBookingException("Please complete the profile first");
            }

            List<PassengerDetailRequest> allPassengers = bookingMapper.buildPassengerList(request, user);

            if (!flightClient.checkSeatAvailability(request.getFlightId(),
                    request.getFlightClass(), allPassengers.size())) {
                throw new FlightNotAvailableException("Insufficient seats available for flight " + request.getFlightId());
            }

            if (!flightClient.reserveSeats(request.getFlightId(),
                    request.getFlightClass(), allPassengers.size())) {
                throw new FlightNotAvailableException("Failed to reserve seats for flight " + request.getFlightId());
            }

            BookingCreateRequest processedRequest = bookingMapper.buildProcessedRequest(request, user, allPassengers);

            PricingRequest pricingRequest = bookingMapper.toPricingRequest(processedRequest, userId);
            PricingResponse pricingResponse = pricingClient.calculatePrice(pricingRequest);

            Booking booking = bookingMapper.toEntity(processedRequest, userId, pricingResponse, user);
            List<Passenger> passengers = passengerMapper.toEntities(processedRequest, booking, pricingResponse);
            booking.setPassengers(passengers);

            Booking savedBooking = bookingRepository.save(booking);

            List<PassengerDetailResponse> passengerResponses = passengerMapper.toPassengerResponseList(passengers);
            BookingResponse response = bookingMapper.toResponseWithCurrency(
                    savedBooking, flight, passengerResponses, pricingResponse.getCurrency());

            eventPublisher.publishBookingCreated(savedBooking, user);

            return response;

        } catch (Exception e) {
            try {
                int totalPassengers = request.getPassengers().size() + (request.isUseMyProfile() ? 1 : 0);
                flightClient.releaseSeats(request.getFlightId(), request.getFlightClass(), totalPassengers);
            } catch (Exception ex) {
                log.error("Failed to release seats after booking error", ex);
            }
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public BookingResponse getUserBookingById(Long id, String userIdHeader) {
        Long userId = Long.parseLong(userIdHeader);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + id + " not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new InvalidBookingException("You can only view your own bookings");
        }

        return bookingMapper.buildBookingResponse(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getUserAllBookings(String userIdHeader) {
        Long userId = Long.parseLong(userIdHeader);

        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookingMapper.buildListBookingResponse(bookings);
    }

    @Transactional
    public void cancelBooking(Long id, String userIdHeader) {
        Long userId = Long.parseLong(userIdHeader);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + id + " not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new InvalidBookingException("You can only view your own bookings");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException("Booking is already cancelled");
        }

        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new InvalidBookingException("Cannot cancel completed booking");
        }

        try {
            FlightInfoResponse flight = flightClient.getFlightForBooking(booking.getFlightId());
            LocalDateTime cancellationTime = flight.getDepartureTime().minusHours(24);

            if (LocalDateTime.now().isAfter(cancellationTime)) {
                throw new InvalidBookingException("Cannot cancel booking less than 24 hours before departure");
            }
        } catch (Exception e) {
            log.warn("Could not validate cancellation deadline for booking: {}", booking.getBookingCode());
        }

        try {
            flightClient.releaseSeats(booking.getFlightId(),
                    booking.getFlightClass().name(), booking.getTotalPassengers());
        }  catch (Exception e) {
            log.error("Failed to release seats for booking: {}", booking.getBookingCode(), e);
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        eventPublisher.publishBookingCanceled(savedBooking);
    }

    @Transactional(readOnly = true)
    public PricingResponse getBookingPricePreview(BookingCreateRequest request) {
        try {
            PricingRequest pricingRequest = bookingMapper.toPricingRequest(request, null);

            PricingResponse pricing = pricingClient.calculatePrice(pricingRequest);

            return pricing;

        } catch (Exception e) {
            log.error("Error calculating price preview for flight: {}", request.getFlightId(), e);
            throw new RuntimeException("Price preview calculation failed: " + e.getMessage(), e);
        }
    }
}
