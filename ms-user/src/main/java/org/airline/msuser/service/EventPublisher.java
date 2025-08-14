package org.airline.msuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.msuser.domain.entity.UserProfile;
import org.airline.msuser.model.event.UserProfileCompletedEvent;
import org.airline.msuser.model.event.UserProfileCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String PROFILE_EXCHANGE = "profile.exchange";

    public void publishProfileCreated(UserProfileCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(PROFILE_EXCHANGE, "profile.created", event);
        } catch (Exception e) {
            log.error("Error publishing profile created event for user: {} (ID: {})",
                    event.getEmail(), event.getUserId(), e);
        }
    }

    public void publishProfileUpdated(UserProfile profile) {
        try {
            UserProfileCreatedEvent event = UserProfileCreatedEvent.builder()
                    .userId(profile.getUserId())
                    .email(profile.getEmail())
                    .firstName(profile.getFirstName())
                    .lastName(profile.getLastName())
                    .phone(profile.getPhone())
                    .profileStatus(profile.getProfileStatus())
                    .ageCategory(profile.getAgeCategory())
                    .isProfileComplete(profile.getIsProfileComplete())
                    .createdAt(profile.getUpdatedAt())
                    .build();

            rabbitTemplate.convertAndSend(PROFILE_EXCHANGE, "profile.updated", event);
        } catch (Exception e) {
            log.error("Error publishing profile updated event for user: {} (ID: {})",
                    profile.getEmail(), profile.getUserId(), e);
        }
    }

    public void publishProfileCompleted(UserProfile profile) {
        try {
            UserProfileCompletedEvent event = UserProfileCompletedEvent.builder()
                    .userId(profile.getUserId())
                    .email(profile.getEmail())
                    .firstName(profile.getFirstName())
                    .lastName(profile.getLastName())
                    .dateOfBirth(profile.getDateOfBirth())
                    .nationality(profile.getNationality())
                    .ageCategory(profile.getAgeCategory())
                    .completedAt(profile.getUpdatedAt())
                    .build();

            rabbitTemplate.convertAndSend(PROFILE_EXCHANGE, "profile.completed", event);
        } catch (Exception e) {
            log.error("Error publishing profile completed event for user: {} (ID: {})",
                    profile.getEmail(), profile.getUserId(), e);
        }
    }
}