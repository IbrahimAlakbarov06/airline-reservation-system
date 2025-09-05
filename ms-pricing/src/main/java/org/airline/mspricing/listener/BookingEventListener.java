package org.airline.mspricing.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.mspricing.model.event.BookingCanceledEvent;
import org.airline.mspricing.model.event.BookingCreatedEvent;
import org.airline.mspricing.service.OccupancyTrackingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {

    private final OccupancyTrackingService occupancyTrackingService;

    @RabbitListener(queues = "booking.created.queue")
    public void handleBookingCreated(BookingCreatedEvent event) {
        try {
            occupancyTrackingService.updateOccupancy(event.getFlightId(), event.getTotalPassengers(), true);
        } catch (Exception e) {
            log.error("Error processing booking created event for flight: {}", event.getFlightId(), e);
        }
    }

    @RabbitListener(queues = "booking.cancelled.queue")
    public void handleBookingCancelled(BookingCanceledEvent event) {
        try {
            occupancyTrackingService.updateOccupancy(event.getFlightId(), event.getTotalPassengers(), false);
        } catch (Exception e) {
            log.error("Error processing booking cancelled event for flight: {}", event.getFlightId(), e);
        }
    }
}
