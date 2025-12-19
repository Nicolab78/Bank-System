package com.bank.banksystem.service.interfaces;

import com.bank.banksystem.dto.card.CardDTO;
import com.bank.banksystem.dto.card.CreateCardDTO;

import java.util.List;

public interface ICardService {

    CardDTO createCard(CreateCardDTO createCardDTO);
    List<CardDTO> getCardsByAccount(Long accountId);
    CardDTO blockCard(Long cardId);
    CardDTO renewCard(Long cardId);
}
