package org.airline.msbooking.mapper;

import org.airline.msbooking.domain.entity.Booking;
import org.airline.msbooking.domain.entity.Passenger;
import org.airline.msbooking.model.dto.request.BookingCreateRequest;
import org.airline.msbooking.model.dto.request.PassengerDetailRequest;
import org.airline.msbooking.model.dto.response.PassengerDetailResponse;
import org.airline.msbooking.model.dto.response.PricingResponse;
import org.airline.msbooking.model.enums.AgeCategory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PassengerMapper {

    public Passenger toEntity(PassengerDetailRequest request,
                              Booking booking,
                              PricingResponse pricingResponse) {
        AgeCategory ageCategory = AgeCategory.fromDateOfBirth(request.getDateOfBirth());

        int passengerIndex = booking.getPassengers() != null ? booking.getPassengers().size() + 1 : 1;
        String passengerKey = "passenger_" + passengerIndex;

        BigDecimal passengerPrice = pricingResponse.getPassengerPrices()
                .getOrDefault(passengerKey, pricingResponse.getBasePrice());

        BigDecimal discount = pricingResponse.getBasePrice().subtract(passengerPrice);

        return Passenger.builder()
                .booking(booking)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .passportNumber(request.getPassportNumber())
                .nationality(request.getNationality())
                .gender(request.getGender())
                .ageCategory(ageCategory)
                .dateOfBirth(request.getDateOfBirth())
                .passengerPrice(passengerPrice)
                .discountApplied(discount)
                .mealPreference(request.getMealPreference())
                .isMainPassenger(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public List<Passenger> toEntities(BookingCreateRequest request,
                                      Booking booking,
                                      PricingResponse pricingResponse) {
        List<Passenger> passengers = request.getPassengers().stream()
                .map(passengerRequest -> toEntity(passengerRequest, booking, pricingResponse))
                .collect(Collectors.toList());

        if (!passengers.isEmpty()) {
            passengers.get(0).setIsMainPassenger(true);
        }
        return passengers;
    }

    public PassengerDetailResponse toPassengerResponse(Passenger passenger) {
        return PassengerDetailResponse.builder()
                .id(passenger.getId())
                .userProfileId(passenger.getUserProfileId())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .fullName(passenger.getFullName())
                .dateOfBirth(passenger.getDateOfBirth())
                .passportNumber(passenger.getPassportNumber())
                .nationality(passenger.getNationality())
                .gender(passenger.getGender())
                .ageCategory(passenger.getAgeCategory().name())
                .passengerPrice(passenger.getPassengerPrice())
                .discountApplied(passenger.getDiscountApplied())
                .currency("USD")
                .seatNumber(passenger.getSeatNumber())
                .mealPreference(passenger.getMealPreference())
                .isMainPassenger(passenger.getIsMainPassenger())
                .build();
    }

    public List<PassengerDetailResponse> toPassengerResponseList(List<Passenger> passengers) {
        return passengers.stream()
                .map(this::toPassengerResponse)
                .collect(Collectors.toList());
    }
}