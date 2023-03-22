package com.innowise.innowisebankproject.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.innowise.innowisebankproject.entity.Card;
import com.innowise.innowisebankproject.repository.CardRepository;
import com.innowise.innowisebankproject.service.impl.CardServiceImpl;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardServiceImpl(cardRepository);
    }

    @Test
    void createCardTest() {
        var accountId = 1L;
        var userId = 1L;
        var card = Card.builder()
            .expirationTime(LocalDate.of(2025, 1, 1))
            .build();

        var createdCard = cardService.createCard(card, accountId, userId);

        var cardCapture = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).add(cardCapture.capture());

        assertThat(createdCard).isEqualTo(cardCapture.getValue());
    }

    @Test
    void getCardsByUserId() {
        var userId = 1L;
        var card = Card.builder()
            .expirationTime(LocalDate.of(2025, 1, 1))
            .userId(userId)
            .build();

        given(cardRepository.getAllByUserId(userId)).willReturn(List.of(card));
        var cards = cardService.getCardsByUserId(userId);

        assertThat(cards.get(0)).isEqualTo(card);
    }

    @Test
    void getCardsByNonExistingUserId() {
        var userId = 1L;

        given(cardRepository.getAllByUserId(userId)).willReturn(List.of());
        var cards = cardService.getCardsByUserId(userId);

        assertThat(cards.isEmpty()).isTrue();
    }
}