package org.airline.msauth.controller;

import lombok.RequiredArgsConstructor;
import org.airline.msauth.model.dto.response.UserResponse;
import org.airline.msauth.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/active")
    public ResponseEntity<List<UserResponse>> getActiveUsers() {
        List<UserResponse> users = userService.getActiveUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/inactive")
    public ResponseEntity<List<UserResponse>> getInactiveUsers() {
        List<UserResponse> users = userService.getInactiveUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/admins")
    public ResponseEntity<List<UserResponse>> getAdminUsers() {
        List<UserResponse> users = userService.getAdminUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') and #id != authentication.principal")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/make-admin")
    public ResponseEntity<Void> makeUserAdmin(@PathVariable Long id) {
        userService.makeUserAdmin(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/remove-admin")
    @PreAuthorize("hasRole('ADMIN') and #id != authentication.principal")
    public ResponseEntity<Void> removeAdminRole(@PathVariable Long id) {
        userService.removeAdminRole(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') and #id != authentication.principal")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}