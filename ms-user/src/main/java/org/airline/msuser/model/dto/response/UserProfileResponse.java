package org.airline.msuser.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msuser.model.enums.AgeCategory;
import org.airline.msuser.model.enums.ProfileStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    private Long id;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private String nationality;
    private String identityNumber;
    private String gender;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private AgeCategory ageCategory;
    private ProfileStatus profileStatus;
    private Boolean isProfileComplete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}