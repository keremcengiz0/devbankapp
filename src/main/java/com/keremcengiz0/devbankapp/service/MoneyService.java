package com.keremcengiz0.devbankapp.service;

import com.keremcengiz0.devbankapp.converter.AccountDtoConverter;
import com.keremcengiz0.devbankapp.dto.AccountDto;
import com.keremcengiz0.devbankapp.exception.AccountNotFoundException;
import com.keremcengiz0.devbankapp.model.Account;
import com.keremcengiz0.devbankapp.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MoneyService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final AccountDtoConverter accountDtoConverter;

    private Logger LOGGER = LoggerFactory.getLogger(MoneyService.class);


    public MoneyService(AccountRepository accountRepository, CustomerService customerService, AccountDtoConverter accountDtoConverter) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.accountDtoConverter = accountDtoConverter;
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


}
