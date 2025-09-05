package org.airline.mspricing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.mspricing.client.FlightClient;
import org.airline.mspricing.exception.PriceException;
import org.airline.mspricing.mapper.PricingMapper;
import org.airline.mspricing.model.dto.request.PassengerRequest;
import org.airline.mspricing.model.dto.request.PricingRequest;
import org.airline.mspricing.model.dto.response.FlightInfoResponse;
import org.airline.mspricing.model.dto.response.PricingResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingService {

    private final FlightClient flightClient;
    private final CurrencyService currencyService;
    private final PricingMapper pricingMapper;

    public PricingResponse calculatePrice(PricingRequest request) {
        try {
            FlightInfoResponse flight = flightClient.getFlightPricing(request.getFlightId());

            BigDecimal basePriceUsd = pricingMapper.getBasePriceByClass(flight, request.getFlightClass());

            BigDecimal dynamicPriceUsd = calculateDynamicPrice(flight, basePriceUsd);

            List<BigDecimal> passengerPrices = new ArrayList<>();
            for (PassengerRequest passengerRequest : request.getPassengers()) {
                BigDecimal passengerPrice = pricingMapper.calculatePassengerPrice(dynamicPriceUsd, passengerRequest.getAgeCategory());
                passengerPrices.add(passengerPrice);
            }

            BigDecimal totalPriceUsd = pricingMapper.calculateTotalPrice(passengerPrices);
            BigDecimal discountedPriceUsd = pricingMapper.calculateTotalDiscount(dynamicPriceUsd, request.getPassengers().size(), totalPriceUsd);

            BigDecimal finalBasePrice = currencyService.convertFromUSD(dynamicPriceUsd, request.getCurrency());
            BigDecimal finalTotalPrice = currencyService.convertFromUSD(totalPriceUsd, request.getCurrency());
            BigDecimal finalTotalDiscount = currencyService.convertFromUSD(discountedPriceUsd, request.getCurrency());

            List<BigDecimal> finalPassengerPrices = passengerPrices.stream()
                    .map(price -> currencyService.convertFromUSD(price, request.getCurrency()))
                    .toList();

            Map<String, BigDecimal> passengerPricesMap = pricingMapper.createPassengerPrices(request.getPassengers(), finalPassengerPrices);

            return pricingMapper.createPricingResponse(request, flight, finalTotalPrice, finalBasePrice, finalTotalDiscount, passengerPricesMap);
        } catch (Exception e) {
            throw new PriceException("Price calculation failed: " + e.getMessage());
        }

    }

    public BigDecimal getSimplePrice(Long flightId, String flightClass, Integer passengerCount, String currency) {
        try {
            FlightInfoResponse flight = flightClient.getFlightPricing(flightId);

            BigDecimal basePriceUsd = pricingMapper.getBasePriceByClass(flight, flightClass);
            BigDecimal dynamicPriceUsd = calculateDynamicPrice(flight, basePriceUsd);
            BigDecimal totalPriceUsd = dynamicPriceUsd.multiply(new BigDecimal(passengerCount));

            return currencyService.convertFromUSD(totalPriceUsd, currency);
        } catch (Exception e) {
            throw new PriceException("Price retrieval failed: " + e.getMessage());
        }
    }

    private BigDecimal calculateDynamicPrice(FlightInfoResponse flight, BigDecimal basePrice) {
        BigDecimal timeMultiplier = pricingMapper.calculateTimeMultiplier(flight.getDepartureTime());
        BigDecimal occupancyMultiplier = pricingMapper.calculateOccupancyMultiplier(flight.getOccupancyRate());

        return basePrice
                .multiply(timeMultiplier)
                .multiply(occupancyMultiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }
}