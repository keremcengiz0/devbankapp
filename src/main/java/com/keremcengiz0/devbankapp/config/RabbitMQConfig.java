package com.keremcengiz0.devbankapp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.queue.first.name}")
    private String firstQueue;
    @Value("${rabbitmq.binding.first.routing.key}")
    private String firstRoute;
    @Value("${rabbitmq.queue.second.name}")
    private String secondQueue;
    @Value("${rabbitmq.binding.second.routing.key}")
    private String secondRoute;
    @Value("${rabbitmq.queue.third.name}")
    private String thirdQueue;
    @Value("${rabbitmq.binding.third.routing.key}")
    private String thirdRoute;
    @Value("${rabbitmq.queue.notification.name}")
    private String notificationQueue;
    @Value("${rabbitmq.binding.notification.routing.key}")
    private String notificationRoute;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue firstStepQueue() {
        return new Queue(firstQueue);
    }

    @Bean
    public Queue secondStepQueue() {
        return new Queue(secondQueue);
    }

    @Bean
    public Queue thirdStepQueue() {
        return new Queue(thirdQueue);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueue);
    }

    @Bean
    public Binding firstBinding() {
        return BindingBuilder.bind(firstStepQueue())
                .to(exchange())
                .with(firstRoute);
    }

    @Bean
    public Binding secondBinding() {
        return BindingBuilder.bind(secondStepQueue())
                .to(exchange())
                .with(secondRoute);
    }

    @Bean
    public Binding thirdBinding() {
        return BindingBuilder.bind(thirdStepQueue())
                .to(exchange())
                .with(thirdRoute);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue())
                .to(exchange())
                .with(notificationRoute);
    }

    // message converter
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // configure RabbitTemplate
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
