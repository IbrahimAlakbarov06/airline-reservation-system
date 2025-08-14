package org.airline.msuser.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airline.msuser.model.enums.AgeCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCompletedEvent {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String nationality;
    private AgeCategory ageCategory;
    private LocalDateTime completedAt;
}