package com.bank.banksystem.service.interfaces;

import com.bank.banksystem.dto.transaction.CreateTransactionDTO;
import com.bank.banksystem.dto.transaction.TransactionDTO;

import java.util.List;

public interface ITransactionService {
    TransactionDTO createTransaction(CreateTransactionDTO createTransactionDTO);
    List<TransactionDTO> getTransactionsByAccount(Long accountId, int page, int size, String type);
}
