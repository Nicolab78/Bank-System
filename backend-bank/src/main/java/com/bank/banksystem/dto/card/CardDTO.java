package com.bank.banksystem.dto.card;

import com.bank.banksystem.model.CardStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CardDTO {
    private Long id;
    private String cardNumber;
    private String cvv;
    private LocalDate expirationDate;
    private CardStatus status;
    private Long accountId;
}
