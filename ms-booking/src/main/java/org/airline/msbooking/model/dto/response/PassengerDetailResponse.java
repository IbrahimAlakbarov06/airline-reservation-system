package org.airline.msbooking.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDetailResponse {

    private Long id;
    private Long userProfileId;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private String nationality;
    private String gender;
    private String ageCategory;
    private BigDecimal passengerPrice;
    private String seatNumber;
    private String mealPreference;
    private String specialAssistance;
    private Boolean isMainPassenger;
}