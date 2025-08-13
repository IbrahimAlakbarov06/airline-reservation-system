package org.airline.msauth.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginEvent {
    private Long userId;
    private String email;
    private LocalDateTime loginTime;
}
