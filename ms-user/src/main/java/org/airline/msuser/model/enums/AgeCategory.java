package org.airline.msuser.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum AgeCategory {
    INFANT(0, 2, new BigDecimal("0.90"), "Under 2 y.o., on lap"),
    CHILD(2, 12, new BigDecimal("0.25"), "2-11 years old"),
    ADULT(12, 120, BigDecimal.ZERO, "12 years and older");

    private final int minAge;
    private final int maxAge;
    private final BigDecimal discountPercentage;
    private final String description;


    public static AgeCategory fromAge(int age) {
        return Arrays.stream(values())
                .filter(category -> age >= category.minAge && age < category.maxAge)
                .findFirst()
                .orElse(ADULT);
    }

    public static AgeCategory fromDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return ADULT;
        }
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return fromAge(age);
    }

    public BigDecimal applyDiscount(BigDecimal basePrice) {
        if (discountPercentage.compareTo(BigDecimal.ZERO) == 0) {
            return basePrice;
        }
        BigDecimal discount = basePrice.multiply(discountPercentage);
        return basePrice.subtract(discount);
    }
}