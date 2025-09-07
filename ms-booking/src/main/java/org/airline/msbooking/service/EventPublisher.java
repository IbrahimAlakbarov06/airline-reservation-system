package org.airline.msbooking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.msbooking.domain.entity.Booking;
import org.airline.msbooking.model.dto.response.UserProfileResponse;
import org.airline.msbooking.model.event.BookingCanceledEvent;
import org.airline.msbooking.model.event.BookingCompletedEvent;
import org.airline.msbooking.model.event.BookingConfirmedEvent;
import org.airline.msbooking.model.event.BookingCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String BOOKING_EXCHANGE = "booking.exchange";

    public void publishBookingCreated(Booking booking, UserProfileResponse profile) {
        try {
            BookingCreatedEvent event = BookingCreatedEvent.builder()
                    .bookingId(booking.getId())
                    .bookingCode(booking.getBookingCode())
                    .userId(profile.getUserId())
                    .userEmail(profile.getEmail())
                    .flightId(booking.getFlightId())
                    .flightNumber(null)
                    .flightClass(booking.getFlightClass().name())
                    .totalPassengers(booking.getTotalPassengers())
                    .totalPrice(booking.getTotalPrice())
                    .contactName(booking.getContactName())
                    .contactPhone(booking.getContactPhone())
                    .createdAt(booking.getCreatedAt())
                    .build();

            rabbitTemplate.convertAndSend(BOOKING_EXCHANGE, "booking.created", event);
        } catch (Exception e) {
            log.error("Error publishing booking created event for booking: {}", booking.getBookingCode(), e);
        }
    }

    public void publishBookingConfirmed(Booking booking) {
        try {
            BookingConfirmedEvent event = BookingConfirmedEvent.builder()
                    .bookingId(booking.getId())
                    .bookingCode(booking.getBookingCode())
                    .userId(booking.getUserId())
                    .userEmail(null)
                    .flightId(booking.getFlightId())
                    .flightNumber(null)
                    .totalPassengers(booking.getTotalPassengers())
                    .totalPrice(booking.getTotalPrice())
                    .confirmedAt(booking.getConfirmedAt())
                    .build();

            rabbitTemplate.convertAndSend(BOOKING_EXCHANGE, "booking.confirmed", event);
        } catch (Exception e) {
            log.error("Error publishing booking confirmed event for booking: {}", booking.getBookingCode(), e);
        }
    }

    public void publishBookingCanceled(Booking booking) {
        try {
            BookingCanceledEvent event = BookingCanceledEvent.builder()
                    .bookingId(booking.getId())
                    .bookingCode(booking.getBookingCode())
                    .userId(booking.getUserId())
                    .userEmail(null)
                    .flightId(booking.getFlightId())
                    .flightNumber(null)
                    .totalPassengers(booking.getTotalPassengers())
                    .totalPrice(booking.getTotalPrice())
                    .reason("User cancellation")
                    .cancelledAt(booking.getCancelledAt())
                    .build();

            rabbitTemplate.convertAndSend(BOOKING_EXCHANGE, "booking.cancelled", event);
        } catch (Exception e) {
            log.error("Error publishing booking cancelled event for booking: {}", booking.getBookingCode(), e);
        }
    }

    public void publishBookingCompleted(Booking booking) {
        try {
            BookingCompletedEvent event = BookingCompletedEvent.builder()
                    .bookingId(booking.getId())
                    .bookingCode(booking.getBookingCode())
                    .userId(booking.getUserId())
                    .userEmail(null)
                    .flightId(booking.getFlightId())
                    .flightNumber(null)
                    .totalPassengers(booking.getTotalPassengers())
                    .totalPrice(booking.getTotalPrice())
                    .currency("USD")
                    .completedAt(booking.getUpdatedAt())
                    .build();

            rabbitTemplate.convertAndSend(BOOKING_EXCHANGE, "booking.completed", event);

        } catch (Exception e) {
            log.error("Error publishing booking completed event for booking: {}", booking.getBookingCode(), e);
        }
    }
}
