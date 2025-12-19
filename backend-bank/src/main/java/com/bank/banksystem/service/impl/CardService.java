package com.bank.banksystem.service.impl;

import com.bank.banksystem.dto.card.CardDTO;
import com.bank.banksystem.dto.card.CreateCardDTO;
import com.bank.banksystem.model.Account;
import com.bank.banksystem.model.Card;
import com.bank.banksystem.model.CardStatus;
import com.bank.banksystem.repository.AccountRepository;
import com.bank.banksystem.repository.CardRepository;
import com.bank.banksystem.service.interfaces.ICardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
@Service
@Transactional
@RequiredArgsConstructor
public class CardService implements ICardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    @Override
    public CardDTO createCard(CreateCardDTO createCardDTO) {
        Account account = accountRepository.findById(createCardDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        Card card = Card.builder()
                .cardNumber(generateCardNumber())
                .cvv(generateCVV())
                .expirationDate(LocalDate.now().plusYears(3))
                .status(CardStatus.ACTIVE)
                .account(account)
                .build();

        cardRepository.save(card);

        return mapToDto(card);
    }

    @Override
    public List<CardDTO> getCardsByAccount(Long accountId) {
        return cardRepository.findByAccountId(accountId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public CardDTO blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée"));

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        return mapToDto(card);
    }

    @Override
    public CardDTO renewCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée"));

        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCVV());
        card.setExpirationDate(LocalDate.now().plusYears(3));
        card.setStatus(CardStatus.ACTIVE);

        cardRepository.save(card);

        return mapToDto(card);
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private String generateCVV() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private CardDTO mapToDto(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cvv(card.getCvv())
                .expirationDate(card.getExpirationDate())
                .status(card.getStatus())
                .accountId(card.getAccount().getId())
                .build();
    }
}
