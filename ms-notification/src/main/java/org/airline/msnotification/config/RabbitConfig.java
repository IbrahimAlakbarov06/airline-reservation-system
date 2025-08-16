package org.airline.msnotification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
    public TopicExchange profileExchange() {
        return new TopicExchange("profile.exchange");
    }

    @Bean
    public Queue profileCreatedQueue() {
        return QueueBuilder.durable("profile.created.queue").build();
    }

    @Bean
    public Binding profileCreatedBinding() {
        return BindingBuilder
                .bind(profileCreatedQueue())
                .to(profileExchange())
                .with("profile.created");
    }

    @Bean
    public Queue profileCompletedQueue() {
        return QueueBuilder.durable("profile.completed.queue").build();
    }

    @Bean
    public Binding profileCompletedBinding() {
        return BindingBuilder
                .bind(profileCompletedQueue())
                .to(profileExchange())
                .with("profile.completed");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}