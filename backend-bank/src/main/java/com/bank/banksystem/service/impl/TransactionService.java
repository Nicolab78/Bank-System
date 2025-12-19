package com.bank.banksystem.service.impl;

import com.bank.banksystem.dto.transaction.CreateTransactionDTO;
import com.bank.banksystem.dto.transaction.TransactionDTO;
import com.bank.banksystem.model.Account;
import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.model.TransactionType;
import com.bank.banksystem.repository.AccountRepository;
import com.bank.banksystem.repository.TransactionRepository;
import com.bank.banksystem.service.interfaces.ITransactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService implements ITransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public TransactionDTO createTransaction(CreateTransactionDTO createTransactionDTO) {

        TransactionType type = TransactionType.valueOf(createTransactionDTO.getType());

        Account source = null;
        Account target = null;

        if (createTransactionDTO.getSourceAccountId() != null) {
            source = accountRepository.findById(createTransactionDTO.getSourceAccountId())
                    .orElseThrow(() -> new RuntimeException("Source account not found."));
        }

        if (createTransactionDTO.getTargetAccountId() != null) {
            target = accountRepository.findById(createTransactionDTO.getTargetAccountId())
                    .orElseThrow(() -> new RuntimeException("Target account not found."));
        }

        Double amount = createTransactionDTO.getAmount();

        switch (type) {

            case DEPOSIT -> {
                if (target == null)
                    throw new RuntimeException("Target account required for deposit.");

                target.setBalance(target.getBalance() + amount);
                accountRepository.save(target);
            }

            case WITHDRAW -> {
                if (source == null)
                    throw new RuntimeException("Source account required for withdrawal.");

                if (source.getBalance() < amount)
                    throw new RuntimeException("Insufficient funds.");

                source.setBalance(source.getBalance() - amount);
                accountRepository.save(source);
            }

            case TRANSFER -> {
                if (source == null || target == null)
                    throw new RuntimeException("Both accounts required for transfer.");

                if (source.getBalance() < amount)
                    throw new RuntimeException("Insufficient funds.");

                source.setBalance(source.getBalance() - amount);
                target.setBalance(target.getBalance() + amount);

                accountRepository.save(source);
                accountRepository.save(target);
            }
        }

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .date(LocalDateTime.now())
                .type(type)
                .sourceAccount(source)
                .targetAccount(target)
                .build();

        transactionRepository.save(transaction);

        return mapToDto(transaction);
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccount(Long accountId, int page, int size, String type) {

        accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found."));

        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> transactions = transactionRepository
                .findBySourceAccountIdOrTargetAccountIdOrderByDateDesc(
                        accountId,
                        accountId,
                        pageable
                );

        Stream<Transaction> stream = transactions.stream();

        if (type != null && !type.isBlank()) {
            TransactionType filterType = TransactionType.valueOf(type.toUpperCase());
            stream = stream.filter(t -> t.getType() == filterType);
        }

        return stream
                .map(this::mapToDto)
                .toList();
    }

    private TransactionDTO mapToDto(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType().name())
                .sourceAccountId(transaction.getSourceAccount() != null ? transaction.getSourceAccount().getId() : null)
                .targetAccountId(transaction.getTargetAccount() != null ? transaction.getTargetAccount().getId() : null)
                .date(transaction.getDate().toString())
                .build();
    }
}