package org.airline.msauth.config;

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