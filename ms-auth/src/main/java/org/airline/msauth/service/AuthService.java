package org.airline.msauth.service;

import lombok.RequiredArgsConstructor;
import org.airline.msauth.domain.entity.User;
import org.airline.msauth.domain.repo.UserRepository;
import org.airline.msauth.exception.AuthException;
import org.airline.msauth.exception.UserAlreadyExistException;
import org.airline.msauth.exception.UserNotFoundException;
import org.airline.msauth.mapper.UserMapper;
import org.airline.msauth.model.dto.request.ChangePasswordRequest;
import org.airline.msauth.model.dto.request.LoginRequest;
import org.airline.msauth.model.dto.request.RegisterRequest;
import org.airline.msauth.model.dto.response.AuthResponse;
import org.airline.msauth.model.dto.response.UserResponse;
import org.airline.msauth.model.event.UserLoginEvent;
import org.airline.msauth.model.event.UserLogoutEvent;
import org.airline.msauth.model.event.UserRegisteredEvent;
import org.airline.msauth.util.JwtService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EventPublisher eventPublisher;

    private static final String BLACKLIST_PREFIX = "blacklist:token:";
    private static final String LOGIN_ATTEMPTS_PREFIX = "login_attempts:";
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 15;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException("User with email " + request.getEmail() + " already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser =userRepository.save(user);

        String token = jwtService.generateToken(user);

        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getPhone(),
                savedUser.getCreatedAt()
        );
        eventPublisher.publishUserRegistered(event);

        return userMapper.toAuthResponse(savedUser, token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail();
        checkLoginAttempts(email);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            incrementLoginAttempts(email);
            throw new AuthException("Incorrect password");
        }

        clearLoginAttempts(email);
        String token = jwtService.generateToken(user);

        UserLoginEvent event = new UserLoginEvent(
                user.getId(),
                user.getEmail(),
                LocalDateTime.now()
        );
        eventPublisher.publishUserLogin(event);

        return userMapper.toAuthResponse(user, token);
    }

    public void logout(String token, Long userId) {
        String blacklistPrefix = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(blacklistPrefix, "blacklisted", 24, TimeUnit.MINUTES);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        UserLogoutEvent event = new UserLogoutEvent(
                user.getId(),
                user.getEmail(),
                LocalDateTime.now()
        );
        eventPublisher.publishUserLogout(event);
    }

    public void     changePassword(ChangePasswordRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AuthException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public AuthResponse validate(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new AuthException("Invalid token");
        }

        String userId = jwtService.extractUserId(token);
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        return userMapper.toAuthResponse(user, token);
    }

    private void checkLoginAttempts(String email) {
        String key = LOGIN_ATTEMPTS_PREFIX + email;
        String attempts = (String) redisTemplate.opsForValue().get(key);

        if (attempts != null && Integer.parseInt(attempts) >= MAX_LOGIN_ATTEMPTS) {
            throw new AuthException("Too many failed attempts. Please try again after " + LOCKOUT_DURATION_MINUTES + " minutes");
        }
    }

    private void incrementLoginAttempts(String email) {
        String key = LOGIN_ATTEMPTS_PREFIX + email;
        String attempts = (String) redisTemplate.opsForValue().get(key);

        int currentAttempts = attempts == null ? 0 : Integer.parseInt(attempts);
        redisTemplate.opsForValue().set(key, String.valueOf(currentAttempts + 1), LOCKOUT_DURATION_MINUTES, TimeUnit.MINUTES);
    }

    private void clearLoginAttempts(String email) {
        String key = LOGIN_ATTEMPTS_PREFIX + email;
        redisTemplate.delete(key);
    }
}
