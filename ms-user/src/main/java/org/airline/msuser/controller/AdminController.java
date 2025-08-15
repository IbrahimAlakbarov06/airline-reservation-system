package org.airline.msuser.controller;

import lombok.RequiredArgsConstructor;
import org.airline.msuser.model.dto.response.UserProfileResponse;
import org.airline.msuser.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserProfileService userProfileService;

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfileResponse>> getAllProfiles() {
        List<UserProfileResponse> profiles = userProfileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/profiles/complete")
    public ResponseEntity<List<UserProfileResponse>> getCompleteProfiles() {
        List<UserProfileResponse> profiles = userProfileService.getCompleteProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/profiles/incomplete")
    public ResponseEntity<List<UserProfileResponse>> getIncompleteProfiles() {
        List<UserProfileResponse> profiles = userProfileService.getIncompleteProfiles();
        return ResponseEntity.ok(profiles);
    }

    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long userId) {
        userProfileService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }
}