package com.bank.banksystem.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bank.banksystem.dto.account.AccountDTO;
import com.bank.banksystem.dto.account.CreateAccountDTO;
import com.bank.banksystem.model.Account;
import com.bank.banksystem.model.AccountStatus;
import com.bank.banksystem.model.AccountType;
import com.bank.banksystem.model.User;
import com.bank.banksystem.repository.AccountRepository;
import com.bank.banksystem.repository.UserRepository;
import com.bank.banksystem.service.interfaces.IAccountService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountDTO createAccount(CreateAccountDTO createAccountDTO) {

        User user = userRepository.findById(createAccountDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User with ID " + createAccountDTO.getUserId() + " not found."));

        Account account = Account.builder()
                .owner(user)
                .type(AccountType.valueOf(createAccountDTO.getType()))
                .balance(0.0)
                .status(AccountStatus.ACTIVE)
                .accountNumber(generateAccountNumber())
                .build();

        accountRepository.save(account);
        return mapToDto(account);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account with ID " + id + " not found."));

        return mapToDto(account);
    }

    @Override
    public List<AccountDTO> getAccountsByUser(Long userId) {

        List<Account> accounts = accountRepository.findByOwnerId(userId);

        if (accounts.isEmpty()) {
            throw new RuntimeException("No accounts found for user ID " + userId + ".");
        }

        return accounts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AccountDTO mapToDto(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .type(account.getType().name())
                .status(account.getStatus().name())
                .ownerId(account.getOwner().getId())
                .build();
    }

    private String generateAccountNumber() {
        return "ACC-" + System.currentTimeMillis();
    }
}