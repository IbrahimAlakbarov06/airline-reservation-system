package org.airline.mspricing.mapper;

import org.airline.mspricing.model.dto.request.PassengerRequest;
import org.airline.mspricing.model.dto.request.PricingRequest;
import org.airline.mspricing.model.dto.response.FlightInfoResponse;
import org.airline.mspricing.model.dto.response.PricingResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PricingMapper {

    public PricingResponse createPricingResponse(PricingRequest request,
                                                 FlightInfoResponse flight,
                                                 BigDecimal totalPrice,
                                                 BigDecimal basePrice,
                                                 BigDecimal totalDiscount,
                                                 Map<String, BigDecimal> passengerPrices) {
        return PricingResponse.builder()
                .totalPrice(totalPrice)
                .basePrice(basePrice)
                .totalDiscount(totalDiscount)
                .currency(request.getCurrency() != null ? request.getCurrency() : "AZN")
                .passengerPrices(passengerPrices)
                .build();
    }

    public Map<String, BigDecimal> createPassengerPrices(List<PassengerRequest> passengers, List<BigDecimal> prices) {
        Map<String, BigDecimal> passengerPrices = new HashMap<>();

        for (int i = 0; i < passengers.size(); i++) {
            String key = "passenger_" + passengers.get(i).getPassengerId();
            BigDecimal price = i < prices.size() ? prices.get(i) : BigDecimal.ZERO;
            passengerPrices.put(key, price);
        }

        return passengerPrices;
    }

    public BigDecimal calculateTimeMultiplier(LocalDateTime departureTime) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), departureTime);

        if (hours < 6) return new BigDecimal("2.5");
        if (hours < 24) return new BigDecimal("2.0");
        if (hours < 168) return new BigDecimal("1.5");
        if (hours < 720) return new BigDecimal("1.2");
        if (hours > 2160) return new BigDecimal("0.8");

        return BigDecimal.ONE;
    }

    public BigDecimal calculateOccupancyMultiplier(Double occupancy) {
        if (occupancy == null) return BigDecimal.ONE;

        if (occupancy >= 95) return new BigDecimal("2.0");
        if (occupancy >= 85) return new BigDecimal("1.6");
        if (occupancy >= 70) return new BigDecimal("1.3");
        if (occupancy >= 50) return new BigDecimal("1.1");

        return new BigDecimal("0.9");
    }

    public BigDecimal calculatePassengerPrice(BigDecimal basePrice, String ageCategory) {
        return switch (ageCategory.toUpperCase()) {
            case "INFANT" -> basePrice.multiply(new BigDecimal("0.1"));
            case "CHILD" -> basePrice.multiply(new BigDecimal("0.75"));
            default -> basePrice;
        };
    }

    public BigDecimal getBasePriceByClass(FlightInfoResponse flight, String flightClass) {
        return switch (flightClass.toUpperCase()) {
            case "BUSINESS" -> flight.getBusinessPrice();
            case "FIRST_CLASS" -> flight.getFirstClassPrice();
            default -> flight.getEconomyPrice();
        };
    }

    public BigDecimal calculateTotalPrice(List<BigDecimal> passengerPrices) {
        return passengerPrices.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalDiscount(BigDecimal basePrice,
                                             Integer passengerCount,
                                             BigDecimal totalPrice) {
        BigDecimal totalBasePrice = basePrice.multiply(BigDecimal.valueOf(passengerCount));
        return totalBasePrice.subtract(totalPrice);
    }
}