package org.airline.msauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airline.msauth.model.event.UserLoginEvent;
import org.airline.msauth.model.event.UserLogoutEvent;
import org.airline.msauth.model.event.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String USER_EXCHANGE = "user.exchange";

    public void publishUserRegistered(UserRegisteredEvent event) {
        try {
            rabbitTemplate.convertAndSend(USER_EXCHANGE, "user.registered", event);
            log.info("User registered event published successfully");
        } catch (Exception e) {
            log.error("Error publishing user registered event", e);
        }
    }

    public void publishUserLogin(UserLoginEvent event) {
        try {
            rabbitTemplate.convertAndSend(USER_EXCHANGE, "user.login", event);
            log.info("User login event published successfully");
        } catch (Exception e) {
            log.error("Error publishing user login event", e);
        }
    }

    public void publishUserLogout(UserLogoutEvent event) {
        try {
            rabbitTemplate.convertAndSend(USER_EXCHANGE, "user.logout", event);
            log.info("User logout event published successfully");
        } catch (Exception e) {
            log.error("Error publishing user logout event", e);
        }
    }
}