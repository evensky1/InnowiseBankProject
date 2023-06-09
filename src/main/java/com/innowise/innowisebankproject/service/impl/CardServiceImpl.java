package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.Card;
import com.innowise.innowisebankproject.repository.CardRepository;
import com.innowise.innowisebankproject.service.CardService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Stateless
@AllArgsConstructor
@NoArgsConstructor
public class CardServiceImpl implements CardService {

    @EJB
    private CardRepository cardRepository;

    @Override
    public Card createCard(Card card, Long accountId, Long userId) {
        var cvv = (int) (Math.random() * 899 + 100);
        var num = (long) (1000_0000_0000_0000L + Math.random() * (Long.MAX_VALUE / 1000L));
        var pin = (int) (Math.random() * 1000);

        var newCard = Card.builder()
            .accountId(accountId)
            .userId(userId)
            .cvv(String.valueOf(cvv))
            .pin(String.valueOf(pin))
            .number(String.valueOf(num))
            .expirationTime(card.getExpirationTime())
            .build();

        cardRepository.add(newCard);

        return newCard;
    }

    @Override
    public List<Card> getCardsByUserId(Long id) {
        return cardRepository.getAllByUserId(id);
    }
}
