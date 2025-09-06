package org.airline.msbooking.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msbooking.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;
    private String bookingCode;
    private Long userId;
    private Long flightId;
    private String flightNumber;
    private String flightClass;
    private BookingStatus status;
    private Integer totalPassengers;

    private BigDecimal totalPrice;
    private BigDecimal basePrice;
    private BigDecimal discountAmount;
    private String currency;

    private String contactName;
    private String contactPhone;
    private String specialRequests;

    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<PassengerDetailResponse> passengerDetails;
    private FlightInfoResponse flightInfo;
}