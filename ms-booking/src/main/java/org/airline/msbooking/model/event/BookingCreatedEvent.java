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
public class BookingCreatedEvent {

    private Long bookingId;
    private String bookingCode;
    private Long userId;
    private String userEmail;
    private Long flightId;
    private String flightNumber;
    private String flightClass;
    private Integer totalPassengers;
    private BigDecimal totalPrice;
    private String contactName;
    private String contactPhone;
    private LocalDateTime createdAt;
}
