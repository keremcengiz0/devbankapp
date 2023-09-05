package com.keremcengiz0.devbankapp.converter;

import com.keremcengiz0.devbankapp.dto.AccountDto;
import com.keremcengiz0.devbankapp.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoConverter {
    public AccountDto convert(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .customerId(account.getCustomerId())
                .build();
    }
}
