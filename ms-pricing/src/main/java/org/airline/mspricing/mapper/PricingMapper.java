package org.airline.mspricing.mapper;

import org.airline.mspricing.model.dto.request.PassengerRequest;
import org.airline.mspricing.model.dto.request.PricingRequest;
import org.airline.mspricing.model.dto.response.FlightInfoResponse;
import org.airline.mspricing.model.dto.response.PricingResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
}