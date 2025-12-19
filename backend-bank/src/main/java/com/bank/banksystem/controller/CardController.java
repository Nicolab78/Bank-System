package com.bank.banksystem.controller;

import com.bank.banksystem.dto.card.CardDTO;
import com.bank.banksystem.dto.card.CreateCardDTO;
import com.bank.banksystem.service.interfaces.ICardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final ICardService cardService;

    public CardController(ICardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDTO createCardDTO) {
        try {
            CardDTO card = cardService.createCard(createCardDTO);
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getCardsByAccount(@PathVariable Long accountId) {
        try {
            List<CardDTO> cards = cardService.getCardsByAccount(accountId);
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{cardId}/block")
    public ResponseEntity<?> blockCard(@PathVariable Long cardId) {
        try {
            CardDTO card = cardService.blockCard(cardId);
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{cardId}/renew")
    public ResponseEntity<?> renewCard(@PathVariable Long cardId) {
        try {
            CardDTO card = cardService.renewCard(cardId);
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}