package com.keremcengiz0.devbankapp.service;

import com.keremcengiz0.devbankapp.converter.AccountDtoConverter;
import com.keremcengiz0.devbankapp.dto.AccountDto;
import com.keremcengiz0.devbankapp.dto.request.CreateAccountRequest;
import com.keremcengiz0.devbankapp.dto.request.UpdateAccountRequest;
import com.keremcengiz0.devbankapp.exception.AccountNotFoundException;
import com.keremcengiz0.devbankapp.exception.CustomerNotFoundException;
import com.keremcengiz0.devbankapp.model.Account;
import com.keremcengiz0.devbankapp.model.Customer;
import com.keremcengiz0.devbankapp.repository.AccountRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountDtoConverter accountDtoConverter;
    private final CustomerService customerService;


    public AccountService(AccountRepository accountRepository, AccountDtoConverter accountDtoConverter, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.accountDtoConverter = accountDtoConverter;
        this.customerService = customerService;
    }

    @Cacheable(value = "accounts")
    public List<AccountDto> getAllAccountsDto() {
        System.out.println("inside method");
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream().map(accountDtoConverter::convert).collect(Collectors.toList());
    }

    public AccountDto getAccountDtoById(Long id) {
        System.out.println("inside method");
        Optional<Account> account = accountRepository.findById(id);

        if (!account.isPresent()) {
            throw new AccountNotFoundException("Account with id " + id + " could not be found!");
        }

        AccountDto accountDto = accountDtoConverter.convert(account.get());

        return accountDto;
    }

    public Account getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);

        if (!account.isPresent()) {
            throw new AccountNotFoundException("Account with id " + id + " could not be found!");
        }

        return account.get();
    }

    @CachePut(value = "accounts", key = "#id")
    public AccountDto createAccount(CreateAccountRequest createAccountRequest) {
        Customer customer = customerService.getCustomerById(createAccountRequest.getCustomerId());

        if (customer.getId() == null || customer.getId().equals("")) {
            throw new CustomerNotFoundException("Customer not found!");
        }

        Account account = Account.builder()
                .id(createAccountRequest.getId())
                .balance(createAccountRequest.getBalance())
                .currency(createAccountRequest.getCurrency())
                .customerId(createAccountRequest.getCustomerId())
                .city(createAccountRequest.getCity())
                .build();

        AccountDto accountDto = accountDtoConverter.convert(account);

        accountRepository.save(account);

        return accountDto;
    }

    @CacheEvict(value = "accounts", allEntries = true)
    public AccountDto updateAccount(Long id, UpdateAccountRequest updateAccountRequest) {
        Customer customer = customerService.getCustomerById(updateAccountRequest.getCustomerId());

        if (customer.getId() == null || customer.getId().equals("")) {
            throw new CustomerNotFoundException("Customer not found!");
        }

        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (!optionalAccount.isPresent()) {
            throw new AccountNotFoundException("Account with id " + id + " could not be found!");
        }

        Account account = optionalAccount.get();

        account.setBalance(updateAccountRequest.getBalance());
        account.setCity(updateAccountRequest.getCity());
        account.setCurrency(updateAccountRequest.getCurrency());
        account.setCustomerId(updateAccountRequest.getCustomerId());

        AccountDto accountDto = accountDtoConverter.convert(account);

        accountRepository.save(account);

        return accountDto;

    }

    @CacheEvict(value = "accounts", allEntries = true)
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
