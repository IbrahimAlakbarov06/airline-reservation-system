package org.airline.msbooking.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCompletedEvent {

    private Long bookingId;
    private String bookingCode;
    private Long userId;
    private String userEmail;
    private Long flightId;
    private String flightNumber;
    private Integer totalPassengers;
    private BigDecimal totalPrice;
    private String currency;
    private LocalDateTime completedAt;
}