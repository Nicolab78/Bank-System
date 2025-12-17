package com.bank.banksystem.dto.transaction;

import lombok.Data;

@Data
public class CreateTransactionDTO {
    private Double amount;
    private Long sourceAccountId;
    private Long targetAccountId;
    private String type;
}
