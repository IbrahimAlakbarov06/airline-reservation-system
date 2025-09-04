package org.airline.msbooking.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FlightClass {
    ECONOMY("Economy"),
    BUSINESS("Business"),
    FIRST_CLASS("First Class");

    private final String displayName;

    public static FlightClass fromString(String flightClass) {
        if (flightClass == null) return ECONOMY;

        return switch (flightClass.toUpperCase()) {
            case "BUSINESS" -> BUSINESS;
            case "FIRST_CLASS", "FIRST" -> FIRST_CLASS;
            default -> ECONOMY;
        };
    }
}