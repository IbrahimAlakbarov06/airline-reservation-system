package org.airline.msuser.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
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
    public Queue profileUpdatedQueue() {
        return QueueBuilder.durable("profile.updated.queue").build();
    }

    @Bean
    public Binding profileUpdatedBinding() {
        return BindingBuilder
                .bind(profileUpdatedQueue())
                .to(profileExchange())
                .with("profile.updated");
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
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}