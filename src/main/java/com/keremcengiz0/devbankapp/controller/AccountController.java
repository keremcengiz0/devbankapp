package com.keremcengiz0.devbankapp.controller;

import com.keremcengiz0.devbankapp.dto.AccountDto;
import com.keremcengiz0.devbankapp.dto.request.CreateAccountRequest;
import com.keremcengiz0.devbankapp.dto.request.UpdateAccountRequest;
import com.keremcengiz0.devbankapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccountsDto(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(accountService.getAccountDtoById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        return new ResponseEntity<>(accountService.createAccount(createAccountRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable(name = "id") Long id, @RequestBody UpdateAccountRequest updateAccountRequest) {
        return new ResponseEntity<>(accountService.updateAccount(id, updateAccountRequest), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable(name = "id") Long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
