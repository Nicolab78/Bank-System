package com.bank.banksystem.dto.transaction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {
    private Long id;
    private Double amount;
    private String type;
    private Long sourceAccountId;
    private Long targetAccountId;
    private String date;
}
