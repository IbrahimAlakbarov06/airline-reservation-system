package org.airline.msnotification.listener;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.msnotification.event.UserProfileCompletedEvent;
import org.airline.msnotification.event.UserProfileCreatedEvent;
import org.airline.msnotification.event.UserRegisteredEvent;
import org.airline.msnotification.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {

    private final EmailService emailService;

    @RabbitListener(queues = "user.registered.queue")
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event.getEmail());
        emailService.sendWelcomeEmail(event.getEmail(), event.getFirstName());
    }

    @RabbitListener(queues = "profile.created.queue")
    public void handleProfileCreated(UserProfileCreatedEvent event) {
        log.info("Received profile created event: {}", event.getEmail());
        emailService.sendProfileCreatedEmail(event.getEmail(), event.getFirstName());
    }

    @RabbitListener(queues = "profile.completed.queue")
    public void handleProfileCompleted(UserProfileCompletedEvent event) {
        log.info("Received profile completed event: {}", event.getEmail());
        emailService.sendProfileCompletedEmail(event.getEmail(), event.getFirstName());
    }
}
