package com.bank.banksystem.dto.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private Double balance;
    private String type;
    private String status;
    private Long ownerId;
}