package org.airline.msauth.service;

import lombok.RequiredArgsConstructor;
import org.airline.msauth.domain.entity.User;
import org.airline.msauth.domain.repo.UserRepository;
import org.airline.msauth.exception.UserNotFoundException;
import org.airline.msauth.mapper.UserMapper;
import org.airline.msauth.model.dto.response.UserResponse;
import org.airline.msauth.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toDto);
    }

    public List<UserResponse> getActiveUsers() {
        List<User> activeUsers = userRepository.findActiveUsers();
        return userMapper.toDtoList(activeUsers);
    }

    public List<UserResponse> getInactiveUsers() {
        List<User> inactiveUsers = userRepository.findInActiveUsers();
        return userMapper.toDtoList(inactiveUsers);
    }

    public List<UserResponse> getAdminUsers() {
        List<User> adminUsers = userRepository.findByUserRole(UserRole.ADMIN);
        return userMapper.toDtoList(adminUsers);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getActiveUserByEmail(String email) {
        User user = userRepository.findActiveUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setIsActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setIsActive(true);
        userRepository.save(user);
    }

    public void makeUserAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);
    }

    public void removeAdminRole(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setUserRole(UserRole.USER);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
}