package com.bank.banksystem.controller;

import com.bank.banksystem.dto.transaction.CreateTransactionDTO;
import com.bank.banksystem.dto.transaction.TransactionDTO;
import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.service.interfaces.ITransactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionDTO createTransactionDTO) {
        try {
            TransactionDTO createdTransaction = transactionService.createTransaction(createTransactionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getTransactionsByAccount(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByAccount(accountId, page, size, type);

            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}