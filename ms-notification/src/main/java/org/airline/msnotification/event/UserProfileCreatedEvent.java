package org.airline.msnotification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCreatedEvent {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String profileStatus;
    private String  ageCategory;
    private Boolean isProfileComplete;
    private LocalDateTime createdAt;
}