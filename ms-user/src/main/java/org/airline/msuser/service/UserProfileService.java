package org.airline.msuser.service;

import lombok.RequiredArgsConstructor;
import org.airline.msuser.domain.entity.UserProfile;
import org.airline.msuser.domain.repo.UserProfileRepository;
import org.airline.msuser.exception.UserProfileAlreadyExistsException;
import org.airline.msuser.exception.UserProfileNotFoundException;
import org.airline.msuser.mapper.UserProfileMapper;
import org.airline.msuser.model.dto.request.CompleteProfileRequest;
import org.airline.msuser.model.dto.request.UpdateProfileRequest;
import org.airline.msuser.model.dto.response.UserProfileResponse;
import org.airline.msuser.model.enums.ProfileStatus;
import org.airline.msuser.model.event.UserProfileCreatedEvent;
import org.airline.msuser.model.event.UserRegisteredEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final EventPublisher eventPublisher;

    @Transactional
    public UserProfileResponse createProfileFromRegistration(UserRegisteredEvent event) {
        if (userProfileRepository.existsByUserId(event.getUserId())) {
            throw new UserProfileAlreadyExistsException("Profile already exists for user: " + event.getUserId());
        }

        UserProfile profile = userProfileMapper.toEntity(event);
        profile.setProfileStatus(ProfileStatus.INCOMPLETE);
        profile.setIsProfileComplete(false);

        UserProfile savedProfile = userProfileRepository.save(profile);

        UserProfileCreatedEvent profileEvent = UserProfileCreatedEvent.builder()
                .userId(savedProfile.getUserId())
                .email(savedProfile.getEmail())
                .firstName(savedProfile.getFirstName())
                .lastName(savedProfile.getLastName())
                .phone(savedProfile.getPhone())
                .profileStatus(savedProfile.getProfileStatus())
                .ageCategory(savedProfile.getAgeCategory())
                .isProfileComplete(savedProfile.getIsProfileComplete())
                .createdAt(savedProfile.getCreatedAt())
                .build();

        eventPublisher.publishProfileCreated(profileEvent);

        return userProfileMapper.toResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfileByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user: " + userId));

        return userProfileMapper.toResponse(profile);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfileByEmail(String email) {
        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for email: " + email));

        return userProfileMapper.toResponse(profile);
    }

    @Transactional
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user: " + userId));

        boolean wasComplete = profile.getIsProfileComplete();

        userProfileMapper.updateEntityFromRequest(profile, request);

        UserProfile savedProfile = userProfileRepository.save(profile);

        if (!wasComplete && savedProfile.getIsProfileComplete()) {
            eventPublisher.publishProfileCompleted(savedProfile);
        } else {
            eventPublisher.publishProfileUpdated(savedProfile);
        }

        return userProfileMapper.toResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse completeProfile(Long userId, CompleteProfileRequest request) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user: " + userId));

        userProfileMapper.updateEntityFromCompleteRequest(profile, request);

        UserProfile savedProfile = userProfileRepository.save(profile);

        eventPublisher.publishProfileCompleted(savedProfile);

        return userProfileMapper.toResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> profiles = userProfileRepository.findAll();
        return userProfileMapper.toResponseList(profiles);
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getCompleteProfiles() {
        List<UserProfile> profiles = userProfileRepository.findCompleteProfiles();
        return userProfileMapper.toResponseList(profiles);
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getIncompleteProfiles() {
        List<UserProfile> profiles = userProfileRepository.findIncompleteProfiles();
        return userProfileMapper.toResponseList(profiles);
    }

    @Transactional
    public void deleteProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile not found for user: " + userId));

        userProfileRepository.delete(profile);
    }
}