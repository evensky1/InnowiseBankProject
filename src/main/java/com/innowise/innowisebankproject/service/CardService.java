package com.innowise.innowisebankproject.service;

import com.innowise.innowisebankproject.entity.Card;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CardService {

    Card createCard(Card card, Long accountId, Long userId);
    List<Card> getCardsByUserId(Long id);
    Card findCardById(Long id);
}
