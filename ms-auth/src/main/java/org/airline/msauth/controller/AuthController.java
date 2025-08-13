package org.airline.msauth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.airline.msauth.model.dto.request.ChangePasswordRequest;
import org.airline.msauth.model.dto.request.LoginRequest;
import org.airline.msauth.model.dto.request.RegisterRequest;
import org.airline.msauth.model.dto.response.AuthResponse;
import org.airline.msauth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader,
                                         @RequestHeader("X-User-Id") Long userId) {
        String token = authHeader.substring(7);
        authService.logout(token, userId);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                                 @RequestHeader("X-User-Id") Long userId) {
        authService.changePassword(request, userId);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(String token) {
        AuthResponse response = authService.validate(token);
        return ResponseEntity.ok(response);
    }
}