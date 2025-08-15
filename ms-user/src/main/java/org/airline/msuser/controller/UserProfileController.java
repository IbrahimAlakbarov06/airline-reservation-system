package org.airline.msuser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.msuser.model.dto.request.CompleteProfileRequest;
import org.airline.msuser.model.dto.request.UpdateProfileRequest;
import org.airline.msuser.model.dto.response.UserProfileResponse;
import org.airline.msuser.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;


    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(
            @RequestHeader("X-User-Id") String userIdHeader) {

        Long userId = Long.parseLong(userIdHeader);
        UserProfileResponse profile = userProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateCurrentUserProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            @RequestHeader("X-User-Id") String userIdHeader) {

        Long userId = Long.parseLong(userIdHeader);
        UserProfileResponse updatedProfile = userProfileService.updateProfile(userId, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/profile/complete")
    public ResponseEntity<UserProfileResponse> completeCurrentUserProfile(
            @Valid @RequestBody CompleteProfileRequest request,
            @RequestHeader("X-User-Id") String userIdHeader) {

        Long userId = Long.parseLong(userIdHeader);
        UserProfileResponse completedProfile = userProfileService.completeProfile(userId, request);
        return ResponseEntity.ok(completedProfile);
    }
}