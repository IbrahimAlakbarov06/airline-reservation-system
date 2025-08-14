package org.airline.msuser.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDateTime registeredAt;
}