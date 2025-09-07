package org.airline.msbooking.config;

import org.hibernate.annotations.Bag;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange("booking.exchange");
    }

    @Bean
    public Queue bookingCreatedQueue() {
        return QueueBuilder.durable("booking.created.queue").build();
    }

    @Bean
    public Binding bookingCreatedBinding() {
        return BindingBuilder
                .bind(bookingCreatedQueue())
                .to(bookingExchange())
                .with("booking.created");
    }

    @Bean
    public Queue bookingConfirmedQueue() {
        return QueueBuilder.durable("booking.confirmed.queue").build();
    }

    @Bean
    public Binding bookingConfirmedBinding() {
        return BindingBuilder
                .bind(bookingConfirmedQueue())
                .to(bookingExchange())
                .with("booking.confirmed");
    }

    @Bean
    public Queue bookingCanceledQueue() {
        return QueueBuilder.durable("booking.canceled.queue").build();
    }

    @Bean
    public Binding bookingCanceledBinding() {
        return BindingBuilder
                .bind(bookingCanceledQueue())
                .to(bookingExchange())
                .with("booking.canceled");
    }

    @Bean
    public Queue bookingCompletedQueue() {
        return QueueBuilder.durable("booking.completed.queue").build();
    }

    @Bean
    public Binding bookingCompletedBinding() {
        return BindingBuilder
                .bind(bookingCompletedQueue())
                .to(bookingExchange())
                .with("booking.completed");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}
