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

    @RabbitListener(queues = "seat.reserve.queue")
    public void handleReserveSeat(BookingCreatedEvent event) {
        try {
            occupancyTrackingService.updateOccupancy(event.getFlightId(), event.getTotalPassengers(), true);
        } catch (Exception e) {
            log.error("Error processing seat reserved event for flight: {}", event.getFlightId(), e);
        }
    }

    @RabbitListener(queues = "seat.reserve.queue")
    public void handleReleaseSeat(BookingCanceledEvent event) {
        try {
            occupancyTrackingService.updateOccupancy(event.getFlightId(), event.getTotalPassengers(), false);
        } catch (Exception e) {
            log.error("Error processing seat release event for flight: {}", event.getFlightId(), e);
        }
    }
}
