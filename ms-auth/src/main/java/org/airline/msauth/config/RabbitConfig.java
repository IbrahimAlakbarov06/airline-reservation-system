package org.airline.msauth.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange("user.exchange");
    }

    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder.durable("user.registered.queue").build();
    }

    @Bean
    public Binding userRegisteredBinding() {
        return BindingBuilder
                .bind(userRegisteredQueue())
                .to(userExchange())
                .with("user.registered");
    }

    @Bean
    public Queue userLoginQueue() {
        return QueueBuilder.durable("user.login.queue").build();
    }

    @Bean
    public Binding userLoginBinding() {
        return BindingBuilder
                .bind(userLoginQueue())
                .to(userExchange())
                .with("user.login");
    }

    @Bean
    public Queue userLogoutQueue() {
        return QueueBuilder.durable("user.logout.queue").build();
    }

    @Bean
    public Binding userLogoutBinding() {
        return BindingBuilder
                .bind(userLogoutQueue())
                .to(userExchange())
                .with("user.logout");
    }
}