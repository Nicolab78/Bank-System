package com.bank.banksystem.controller;

import java.util.List;

import com.bank.banksystem.dto.account.CreateAccountDTO;
import com.bank.banksystem.dto.account.AccountDTO;
import com.bank.banksystem.service.interfaces.IAccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        try {
            AccountDTO createdAccount = accountService.createAccount(createAccountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            AccountDTO account = accountService.getAccountById(id);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAccountsByUser(@PathVariable Long userId) {
        try {
            List<AccountDTO> accounts = accountService.getAccountsByUser(userId);
            return ResponseEntity.ok(accounts);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}