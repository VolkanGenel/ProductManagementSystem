package com.volkan.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    private String directExchangeAuth = "auth-direct-exchange";
    private String resetPasswordQueue = "reset-password-queue";
    private String resetBindingKey = "reset-password-binding-key";
    @Bean
    DirectExchange directExchangeAuth() {
        return new DirectExchange(directExchangeAuth);
    }
    @Bean
    Queue queueAuth() {
        return new Queue(resetPasswordQueue);
    }
    @Bean
    public Binding bindingSaveDirectExchange(final Queue queueAuth, final DirectExchange directExchangeAuth) {
        return BindingBuilder.bind(queueAuth).to(directExchangeAuth).with(resetBindingKey);
    }
}
