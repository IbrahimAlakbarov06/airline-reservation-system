package org.airline.mspricing.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange("booking.exchange");
    }

    @Bean
    public Queue seatReservedQueue() {
        return QueueBuilder.durable("seat.reserved.queue").build();
    }

    @Bean
    public Binding seatReservedBinding() {
        return BindingBuilder
                .bind(seatReservedQueue())
                .to(bookingExchange())
                .with("booking.created");
    }

    @Bean
    public Queue seatReleasedQueue() {
        return QueueBuilder.durable("seat.released.queue").build();
    }

    @Bean
    public Binding seatReleasedBinding() {
        return BindingBuilder
                .bind(seatReleasedQueue())
                .to(bookingExchange())
                .with("booking.cancelled");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}