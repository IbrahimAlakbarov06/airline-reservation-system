package org.airline.msuser.mapper;

import org.airline.msuser.domain.entity.UserProfile;
import org.airline.msuser.model.dto.request.CompleteProfileRequest;
import org.airline.msuser.model.dto.request.UpdateProfileRequest;
import org.airline.msuser.model.dto.response.UserProfileResponse;
import org.airline.msuser.model.event.UserRegisteredEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserProfileMapper {

    public UserProfileResponse toResponse(UserProfile profile) {
        if (profile == null) {
            return null;
        }

        return UserProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .email(profile.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .fullName(profile.getFullName())
                .phone(profile.getPhone())
                .dateOfBirth(profile.getDateOfBirth())
                .passportNumber(profile.getPassportNumber())
                .nationality(profile.getNationality())
                .identityNumber(profile.getIdentityNumber())
                .gender(profile.getGender())
                .emergencyContactName(profile.getEmergencyContactName())
                .emergencyContactPhone(profile.getEmergencyContactPhone())
                .ageCategory(profile.getAgeCategory())
                .profileStatus(profile.getProfileStatus())
                .isProfileComplete(profile.getIsProfileComplete())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    public List<UserProfileResponse> toResponseList(List<UserProfile> profiles) {
        if (profiles == null) {
            return null;
        }

        return profiles.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserProfile toEntity(UserRegisteredEvent event) {
        if (event == null) {
            return null;
        }

        return UserProfile.builder()
                .userId(event.getUserId())
                .email(event.getEmail())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .phone(event.getPhone())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void updateEntityFromRequest(UserProfile profile, UpdateProfileRequest request) {
        if (request == null || profile == null) {
            return;
        }

        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            profile.setPhone(request.getPhone());
        }
        if (request.getDateOfBirth() != null) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getPassportNumber() != null) {
            profile.setPassportNumber(request.getPassportNumber());
        }
        if (request.getNationality() != null) {
            profile.setNationality(request.getNationality());
        }
        if (request.getIdentityNumber() != null) {
            profile.setIdentityNumber(request.getIdentityNumber());
        }
        if (request.getGender() != null) {
            profile.setGender(request.getGender());
        }
        if (request.getEmergencyContactName() != null) {
            profile.setEmergencyContactName(request.getEmergencyContactName());
        }
        if (request.getEmergencyContactPhone() != null) {
            profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        }
    }

    public void updateEntityFromCompleteRequest(UserProfile profile, CompleteProfileRequest request) {
        if (request == null || profile == null) {
            return;
        }

        // Required fields
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setPassportNumber(request.getPassportNumber());
        profile.setNationality(request.getNationality());

        // Optional fields
        if (request.getIdentityNumber() != null) {
            profile.setIdentityNumber(request.getIdentityNumber());
        }
        if (request.getGender() != null) {
            profile.setGender(request.getGender());
        }
        if (request.getEmergencyContactName() != null) {
            profile.setEmergencyContactName(request.getEmergencyContactName());
        }
        if (request.getEmergencyContactPhone() != null) {
            profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        }
    }
}