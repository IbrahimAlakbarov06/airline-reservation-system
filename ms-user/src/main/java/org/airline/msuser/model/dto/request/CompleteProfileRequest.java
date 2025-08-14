package org.airline.msuser.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompleteProfileRequest {

    @NotNull(message = "Date of birth is required for booking")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Passport number is required for booking")
    @Pattern(regexp = "^[A-Z0-9]{6,12}$", message = "Invalid passport number format")
    private String passportNumber;

    @NotBlank(message = "Nationality is required for booking")
    private String nationality;

    private String identityNumber;

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Gender must be MALE, FEMALE, or OTHER")
    private String gender;

    private String emergencyContactName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String emergencyContactPhone;
}