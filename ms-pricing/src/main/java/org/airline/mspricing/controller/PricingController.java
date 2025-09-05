package org.airline.mspricing.controller;


import lombok.RequiredArgsConstructor;
import org.airline.mspricing.model.dto.request.PricingRequest;
import org.airline.mspricing.model.dto.response.CurrencyResponse;
import org.airline.mspricing.model.dto.response.PricingResponse;
import org.airline.mspricing.service.CurrencyService;
import org.airline.mspricing.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;
    private final CurrencyService currencyService;

    @PostMapping("/calculate")
    public ResponseEntity<PricingResponse> calculatePrice(@RequestBody PricingRequest request) {
        PricingResponse response = pricingService.calculatePrice(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<BigDecimal> getSimplePrice(
            @PathVariable Long flightId,
            @RequestParam String flightClass,
            @RequestParam Integer passengerCount,
            @RequestParam(defaultValue = "USD") String currency) {

        BigDecimal price = pricingService.getSimplePrice(flightId, flightClass, passengerCount, currency);
        return ResponseEntity.ok(price);
    }

    @GetMapping("/convert")
    public ResponseEntity<BigDecimal> convertFromUSD(
            @RequestParam BigDecimal amount,
            @RequestParam String toCurrency) {

        BigDecimal convertedAmount = currencyService.convertFromUSD(amount, toCurrency);
        return ResponseEntity.ok(convertedAmount);
    }

    @GetMapping("/currencies/rates")
    public ResponseEntity<List<CurrencyResponse>> getAllRates() {
        List<CurrencyResponse> rates = currencyService.getAllRates();
        return ResponseEntity.ok(rates);
    }
}