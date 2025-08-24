package org.airline.msflight.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public enum FlightClass {
    ECONOMY("Economy", BigDecimal.ONE),
    BUSINESS("Business", new BigDecimal("2.5")),
    FIRST_CLASS("First Class", new BigDecimal("4.0"));

    private final String displayName;
    private final BigDecimal priceMultiplier;

    public static FlightClass fromString(String flightClass) {
        if (flightClass == null) {
            return ECONOMY;
        }

        return switch (flightClass.toUpperCase()) {
            case "BUSINESS" -> BUSINESS;
            case "FIRST_CLASS", "FIRST" -> FIRST_CLASS;
            default -> ECONOMY;
        };
    }

    public BigDecimal calculatePrice(BigDecimal basePrice) {
        return basePrice.multiply(this.priceMultiplier);
    }
}