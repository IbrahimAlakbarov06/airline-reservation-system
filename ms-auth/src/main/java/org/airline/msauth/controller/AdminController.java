package org.airline.msauth.controller;

import lombok.RequiredArgsConstructor;
import org.airline.msauth.model.dto.response.UserResponse;
import org.airline.msauth.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<UserResponse> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/active")
    public ResponseEntity<List<UserResponse>> getActiveUsers() {
        List<UserResponse> users = adminService.getActiveUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/inactive")
    public ResponseEntity<List<UserResponse>> getInactiveUsers() {
        List<UserResponse> users = adminService.getInactiveUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/admins")
    public ResponseEntity<List<UserResponse>> getAdminUsers() {
        List<UserResponse> users = adminService.getAdminUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id,
                                               @RequestHeader("Authorization") String authHeader) {
        adminService.deactivateUserWithValidation(id, authHeader);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        adminService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/make-admin")
    public ResponseEntity<Void> makeUserAdmin(@PathVariable Long id) {
        adminService.makeUserAdmin(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/remove-admin")
    public ResponseEntity<Void> removeAdminRole(@PathVariable Long id,
                                                @RequestHeader("Authorization") String authHeader) {
        adminService.removeAdminRoleWithValidation(id, authHeader);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader) {
        adminService.deleteUserWithValidation(id, authHeader);
        return ResponseEntity.ok().build();
    }
}