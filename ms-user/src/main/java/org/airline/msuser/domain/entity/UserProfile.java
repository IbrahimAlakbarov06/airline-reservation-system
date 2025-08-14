package org.airline.msuser.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msuser.model.enums.AgeCategory;
import org.airline.msuser.model.enums.ProfileStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String phone;


    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "passport_number")
    private String passportNumber;

    private String nationality;

    @Column(name = "identity_number")
    private String identityNumber;

    private String gender;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_category")
    private AgeCategory ageCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status", nullable = false)
    private ProfileStatus profileStatus;

    @Column(name = "is_profile_complete", nullable = false)
    private Boolean isProfileComplete = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateProfileCompleteness();
        calculateAgeCategory();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateProfileCompleteness();
        calculateAgeCategory();
    }

    @PostLoad
    private void calculateFields() {
        calculateProfileCompleteness();
        calculateAgeCategory();
    }

    private void calculateProfileCompleteness() {
        boolean requiredFieldsComplete = (
                dateOfBirth != null &&
                        passportNumber != null &&
                        !passportNumber.trim().isEmpty() &&
                        nationality != null &&
                        !nationality.trim().isEmpty()
        );

        this.isProfileComplete = requiredFieldsComplete;

        if (requiredFieldsComplete) {
            this.profileStatus = ProfileStatus.COMPLETE;
        } else {
            this.profileStatus = ProfileStatus.INCOMPLETE;
        }
    }

    private void calculateAgeCategory() {
        if (dateOfBirth != null) {
            this.ageCategory = AgeCategory.fromDateOfBirth(dateOfBirth);
        } else {
            this.ageCategory = AgeCategory.ADULT;
        }
    }
}