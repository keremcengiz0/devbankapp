package com.keremcengiz0.devbankapp.consumer;

import com.keremcengiz0.devbankapp.converter.AccountDtoConverter;
import com.keremcengiz0.devbankapp.dto.request.MoneyTransferRequest;
import com.keremcengiz0.devbankapp.model.Account;
import com.keremcengiz0.devbankapp.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MoneyTransferConsumer {
    private final AccountRepository accountRepository;
    private final AccountDtoConverter accountDtoConverter;
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange exchange;
    private Logger LOGGER = LoggerFactory.getLogger(MoneyTransferConsumer.class);

    @Value("${rabbitmq.binding.first.routing.key}")
    private String firstRoute;
    @Value("${rabbitmq.binding.second.routing.key}")
    private String secondRoute;
    @Value("${rabbitmq.binding.third.routing.key}")
    private String thirdRoute;
    @Value("${rabbitmq.binding.notification.routing.key}")
    private String notificationRoute;

    public MoneyTransferConsumer(AccountRepository accountRepository, AccountDtoConverter accountDtoConverter,
                                 RabbitTemplate rabbitTemplate, TopicExchange exchange) {
        this.accountRepository = accountRepository;
        this.accountDtoConverter = accountDtoConverter;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    @RabbitListener(queues = "${rabbitmq.queue.first.name}")
    public void transferMoneyMessage(MoneyTransferRequest moneyTransferRequest) {
        Optional<Account> optionalAccount = accountRepository.findById(moneyTransferRequest.getFromId());

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            if (account.getBalance() > moneyTransferRequest.getAmount()) {
                account.setBalance(account.getBalance() - moneyTransferRequest.getAmount());
                accountRepository.save(account);
                LOGGER.info(String.format("Money Transfer Request sent to RabbitMQ Second Queue => %s", moneyTransferRequest));
                rabbitTemplate.convertAndSend(exchange.getName(), secondRoute, moneyTransferRequest);
            } else {
                LOGGER.info("Insufficient funds -> accountId: " + moneyTransferRequest.getFromId() + " balance: " + account.getBalance() + " amount: " + moneyTransferRequest.getAmount());
            }

        } else {
            LOGGER.info("Account with id " + moneyTransferRequest.getFromId() + " could not be found!");
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.second.name}")
    public void updateReceiverAccount(MoneyTransferRequest moneyTransferRequest) {
        Optional<Account> optionalAccount = accountRepository.findById(moneyTransferRequest.getToId());

        if (optionalAccount.isPresent()) {
            Account receiverAccount = optionalAccount.get();

            receiverAccount.setBalance(receiverAccount.getBalance() + moneyTransferRequest.getAmount());
            accountRepository.save(receiverAccount);

            LOGGER.info(String.format("Money Transfer Request sent to RabbitMQ Third Queue  => %s", moneyTransferRequest));

            rabbitTemplate.convertAndSend(exchange.getName(), thirdRoute, moneyTransferRequest);
        } else {
            LOGGER.info("Account with id " + moneyTransferRequest.getToId() + " could not be found!");

            Optional<Account> optionalSenderAccount = accountRepository.findById(moneyTransferRequest.getFromId());

            if (optionalSenderAccount.isPresent()) {
                Account senderAccount = optionalSenderAccount.get();

                LOGGER.info("The money charge back to sender ==> id: " + moneyTransferRequest.getFromId());

                senderAccount.setBalance(senderAccount.getBalance() + moneyTransferRequest.getAmount());
                accountRepository.save(senderAccount);
            }
        }
    }


    @RabbitListener(queues = "${rabbitmq.queue.third.name}")
    public void finalizeTransfer(MoneyTransferRequest moneyTransferRequest) {
        Optional<Account> optionalSenderAccount = accountRepository.findById(moneyTransferRequest.getFromId());

        if (optionalSenderAccount.isPresent()) {
            Account senderAccount = optionalSenderAccount.get();

            String notificationSenderMessage = "Dear customer %s, your money transfer request has been succeed. Your new balance is %s";
            LOGGER.info("Sender(" + senderAccount.getId() + ") new account balance: " + senderAccount.getBalance());
            String senderMessage = String.format(notificationSenderMessage, senderAccount.getId(), senderAccount.getBalance());
            rabbitTemplate.convertAndSend(exchange.getName(), notificationRoute, senderMessage);

        } else {
            LOGGER.info("Account with id " + moneyTransferRequest.getFromId() + " could not be found!");
        }

        Optional<Account> optionalReceiverAccount = accountRepository.findById(moneyTransferRequest.getToId());

        if (optionalReceiverAccount.isPresent()) {
            Account receiverAccount = optionalReceiverAccount.get();

            String notificationReceiverMessage = "Dear customer %s, you received a money transfer from %s. Your new balance is %s";
            LOGGER.info("Receiver(" + receiverAccount.getId() + ") new account balance: " + receiverAccount.getBalance());
            String receiverMessage = String.format(notificationReceiverMessage, receiverAccount.getId(), moneyTransferRequest.getFromId(), receiverAccount.getBalance());
            rabbitTemplate.convertAndSend(exchange.getName(), notificationRoute, receiverMessage);

        } else {
            LOGGER.info("Account with id " + moneyTransferRequest.getToId() + " could not be found!");
        }
    }
}
