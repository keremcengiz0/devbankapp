package com.keremcengiz0.devbankapp.controller;

import com.keremcengiz0.devbankapp.dto.AccountDto;
import com.keremcengiz0.devbankapp.service.MoneyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/money")
public class MoneyController {
    private final MoneyService moneyService;

    public MoneyController(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResponseEntity<AccountDto> withdrawMoney(@PathVariable(name = "id") Long id,
                                                    @PathVariable(name = "amount") Double amount) {
        return new ResponseEntity<>(moneyService.withdrawMoney(id, amount), HttpStatus.OK);
    }

    @PutMapping("/add/{id}/{amount}")
    public ResponseEntity<AccountDto> addMoney(@PathVariable(name = "id") Long id,
                                               @PathVariable(name = "amount") Double amount) {
        return new ResponseEntity<>(moneyService.addMoney(id, amount), HttpStatus.OK);
    }
}
