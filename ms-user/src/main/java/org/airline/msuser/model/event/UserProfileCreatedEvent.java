package org.airline.msuser.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msuser.model.enums.AgeCategory;
import org.airline.msuser.model.enums.ProfileStatus;

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
    private ProfileStatus profileStatus;
    private AgeCategory ageCategory;
    private Boolean isProfileComplete;
    private LocalDateTime createdAt;
}