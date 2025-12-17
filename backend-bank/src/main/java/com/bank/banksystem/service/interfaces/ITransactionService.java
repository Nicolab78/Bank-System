package com.bank.banksystem.service.interfaces;

import com.bank.banksystem.dto.transaction.CreateTransactionDTO;
import com.bank.banksystem.dto.transaction.TransactionDTO;

public interface ITransactionService {
    TransactionDTO createTransaction(CreateTransactionDTO createTransactionDTO);
}
