package org.airline.msflight.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum City {
    BAKU("Baku", "AZ", "Azerbaijan"),
    GANJA("Ganja", "AZ", "Azerbaijan"),
    GABALA("Gabala", "AZ", "Azerbaijan"),

    ISTANBUL("Istanbul", "TR", "Turkey"),
    ANKARA("Ankara", "TR", "Turkey"),
    ANTALYA("Antalya", "TR", "Turkey"),
    IZMIR("Izmir", "TR", "Turkey"),

    MOSCOW("Moscow", "RU", "Russia"),
    ST_PETERSBURG("St. Petersburg", "RU", "Russia"),
    SOCHI("Sochi", "RU", "Russia"),

    BERLIN("Berlin", "DE", "Germany"),
    MUNICH("Munich", "DE", "Germany"),
    FRANKFURT("Frankfurt", "DE", "Germany"),

    LONDON("London", "UK", "United Kingdom"),
    MANCHESTER("Manchester", "UK", "United Kingdom"),
    EDINBURGH("Edinburgh", "UK", "United Kingdom"),

    PARIS("Paris", "FR", "France"),
    LYON("Lyon", "FR", "France"),
    MARSEILLE("Marseille", "FR", "France"),

    ROME("Rome", "IT", "Italy"),
    MILAN("Milan", "IT", "Italy"),
    VENICE("Venice", "IT", "Italy"),

    MADRID("Madrid", "ES", "Spain"),
    BARCELONA("Barcelona", "ES", "Spain"),
    VALENCIA("Valencia", "ES", "Spain"),

    DUBAI("Dubai", "AE", "United Arab Emirates"),
    ABU_DHABI("Abu Dhabi", "AE", "United Arab Emirates"),

    NEW_YORK("New York", "US", "United States"),
    LOS_ANGELES("Los Angeles", "US", "United States"),
    CHICAGO("Chicago", "US", "United States"),
    MIAMI("Miami", "US", "United States"),

    TBILISI("Tbilisi", "GE", "Georgia"),
    BATUMI("Batumi", "GE", "Georgia");

    private final String cityName;
    private final String countryCode;
    private final String countryName;

    public static City fromCityName(String cityName) {
        if (cityName == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(city -> city.getCityName().equalsIgnoreCase(cityName))
                .findFirst()
                .orElse(null);
    }

    public static List<City> getCitiesByCountry(String countryCode) {
        return Arrays.stream(values())
                .filter(city -> city.getCountryCode().equalsIgnoreCase(countryCode))
                .collect(Collectors.toList());
    }

    public static List<String> getAllCountries() {
        return Arrays.stream(values())
                .map(City::getCountryName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public String getFullLocation() {
        return cityName + ", " + countryName;
    }
}