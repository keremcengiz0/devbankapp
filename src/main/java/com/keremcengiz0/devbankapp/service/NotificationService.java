package com.keremcengiz0.devbankapp.service;

import com.keremcengiz0.devbankapp.consumer.MoneyTransferConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private Logger LOGGER = LoggerFactory.getLogger(MoneyTransferConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue.notification.name}")
    public void consume(String message) {
        LOGGER.info("Message receiver:  {}", message);
    }

}
