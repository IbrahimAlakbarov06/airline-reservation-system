package org.airline.msuser.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.msuser.model.event.UserRegisteredEvent;
import org.airline.msuser.service.UserProfileService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {

    private final UserProfileService userProfileService;

    @RabbitListener(queues = "user.registered.queue")
    public void handleUserRegistered(UserRegisteredEvent event) {
        try {
            userProfileService.createProfileFromRegistration(event);

            log.info("Successfully created minimal profile for user: {} (ID: {})", event.getEmail(), event.getUserId());
        } catch (Exception e) {
            log.error("Error processing user registered event for user: {} (ID: {})",
                    event.getEmail(), event.getUserId(), e);

            throw e;
        }
    }
}