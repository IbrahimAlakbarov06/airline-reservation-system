package org.airline.msbooking.mapper;

import lombok.RequiredArgsConstructor;
import org.airline.msbooking.domain.entity.Booking;
import org.airline.msbooking.domain.repo.BookingRepository;
import org.airline.msbooking.model.dto.request.BookingCreateRequest;
import org.airline.msbooking.model.dto.request.PassengerRequest;
import org.airline.msbooking.model.dto.request.PricingRequest;
import org.airline.msbooking.model.dto.response.*;
import org.airline.msbooking.model.enums.AgeCategory;
import org.airline.msbooking.model.enums.BookingStatus;
import org.airline.msbooking.model.enums.FlightClass;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final BookingRepository bookingRepository;

    public Booking toEntity(BookingCreateRequest request, Long userId, PricingResponse pricingResponse, UserProfileResponse userProfile) {
        String bookingCode = generateBookingCode();

        return Booking.builder()
                .bookingCode(bookingCode)
                .userId(userId)
                .flightId(request.getFlightId())
                .flightClass(FlightClass.fromString(request.getFlightClass()))
                .bookingStatus(BookingStatus.PENDING)
                .totalPassengers(request.getPassengers().size())
                .totalPrice(pricingResponse.getTotalPrice())
                .basePrice(pricingResponse.getBasePrice())
                .discountAmount(pricingResponse.getTotalDiscount())
                .contactName(request.getContactName() != null ?
                        request.getContactName() : userProfile.getFullName())
                .contactPhone(request.getContactPhone() != null ?
                        request.getContactPhone() : userProfile.getPhone())
                .specialRequests(request.getSpecialRequests())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

    }

    public BookingResponse toResponse(Booking booking, FlightInfoResponse flightInfo, List<PassengerDetailResponse> passengers) {
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingCode(booking.getBookingCode())
                .userId(booking.getUserId())
                .flightId(booking.getFlightId())
                .flightNumber(flightInfo != null ? flightInfo.getFlightNumber() : null)
                .flightClass(booking.getFlightClass().name())
                .status(booking.getBookingStatus())
                .totalPassengers(booking.getPassengers().size())
                .totalPrice(booking.getTotalPrice())
                .basePrice(booking.getBasePrice())
                .discountAmount(booking.getDiscountAmount())
                .currency("USD")
                .contactName(booking.getContactName())
                .contactPhone(booking.getContactPhone())
                .specialRequests(booking.getSpecialRequests())
                .confirmedAt(booking.getConfirmedAt())
                .cancelledAt(booking.getCancelledAt())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .passengerDetails(passengers)
                .flightInfo(flightInfo)
                .build();
    }

    public PricingRequest toPricingRequest(BookingCreateRequest request, Long userId) {
        List<PassengerRequest> passengerRequests = request.getPassengers().stream()
                .map(p -> PassengerRequest.builder()
                        .passengerId((long) Math.abs(p.hashCode())) // Generate consistent ID
                        .ageCategory(AgeCategory.fromDateOfBirth(p.getDateOfBirth()).name())
                        .build())
                .collect(Collectors.toList());

        return PricingRequest.builder()
                .flightId(request.getFlightId())
                .flightClass(request.getFlightClass())
                .passengers(passengerRequests)
                .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .build();
    }

    public BookingResponse toResponseWithCurrency(Booking booking,
                                                  FlightInfoResponse flightInfo,
                                                  List<PassengerDetailResponse> passengers,
                                                  String currency) {
        BookingResponse response = toResponse(booking, flightInfo, passengers);
        response.setCurrency(currency);

        if (passengers != null) {
            passengers.forEach(p -> p.setCurrency(currency));
        }

        return response;
    }

    private String generateBookingCode() {
        String code;
        do {
            code = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (bookingRepository.existsByBookingCode(code));
        return code;
    }
}