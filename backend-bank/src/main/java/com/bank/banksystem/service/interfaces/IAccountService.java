package com.bank.banksystem.service.interfaces;

import java.util.List;

import com.bank.banksystem.dto.account.AccountDTO;
import com.bank.banksystem.dto.account.CreateAccountDTO;

public interface IAccountService {

    AccountDTO createAccount(CreateAccountDTO dto);

    AccountDTO getAccountById(Long id);

    List<AccountDTO> getAccountsByUser(Long userId);
}