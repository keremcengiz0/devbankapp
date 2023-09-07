package com.keremcengiz0.devbankapp.service;

import com.keremcengiz0.devbankapp.converter.AccountDtoConverter;
import com.keremcengiz0.devbankapp.dto.AccountDto;
import com.keremcengiz0.devbankapp.dto.request.MoneyTransferRequest;
import com.keremcengiz0.devbankapp.exception.AccountNotFoundException;
import com.keremcengiz0.devbankapp.exception.InsufficientFundsException;
import com.keremcengiz0.devbankapp.model.Account;
import com.keremcengiz0.devbankapp.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MoneyService {
    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final AccountDtoConverter accountDtoConverter;
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange exchange;
    private Logger LOGGER = LoggerFactory.getLogger(MoneyService.class);

    @Value("${rabbitmq.binding.first.routing.key}")
    private String firstRoute;
    @Value("${rabbitmq.binding.second.routing.key}")
    private String secondRoute;
    @Value("${rabbitmq.binding.third.routing.key}")
    private String thirdRoute;

    public MoneyService(AccountRepository accountRepository, CustomerService customerService,
                        AccountDtoConverter accountDtoConverter, RabbitTemplate rabbitTemplate,
                        TopicExchange exchange) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.accountDtoConverter = accountDtoConverter;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public AccountDto withdrawMoney(Long id, Double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (!optionalAccount.isPresent()) {
            throw new AccountNotFoundException("Account with id " + id + " could not be found!");
        }

        Account account = optionalAccount.get();


        if (account.getBalance() > amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
        } else {
            LOGGER.info("Insufficient funds -> accountId: " + id + " balance: " + account.getBalance() + " amount: " + amount);
            throw new InsufficientFundsException("Insufficient funds -> accountId: " + id + " balance: " + account.getBalance() + " amount: " + amount);
        }

        AccountDto accountDto = accountDtoConverter.convert(account);

        return accountDto;
    }

    public AccountDto addMoney(Long id, Double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (!optionalAccount.isPresent()) {
            throw new AccountNotFoundException("Account with id " + id + " could not be found!");
        }

        Account account = optionalAccount.get();

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        AccountDto accountDto = accountDtoConverter.convert(account);

        return accountDto;
    }

    public void transferMoney(MoneyTransferRequest transferRequest) {
        LOGGER.info(String.format("Money Transfer Request sent to RabbitMQ => %s", transferRequest));
        rabbitTemplate.convertAndSend(exchange.getName(), firstRoute, transferRequest);
    }
}
