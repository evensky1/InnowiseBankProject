package com.innowise.innowisebankproject.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.BDDMockito.given;

import com.innowise.innowisebankproject.entity.Account;
import com.innowise.innowisebankproject.entity.Card;
import com.innowise.innowisebankproject.repository.AccountRepository;
import com.innowise.innowisebankproject.repository.AccountTransactionRepository;
import com.innowise.innowisebankproject.repository.CardRepository;
import com.innowise.innowisebankproject.repository.CardTransactionRepository;
import com.innowise.innowisebankproject.service.impl.TransactionServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountTransactionRepository accountTransactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardTransactionRepository cardTransactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {

        transactionService = new TransactionServiceImpl(accountTransactionRepository,
            accountRepository, cardRepository, cardTransactionRepository);
    }

    @Test
    void makeAccountTransaction() {
        var destAccountNum = "1234567891234567";
        var srcAccountId = 1L;
        var destAccount = Account.builder().id(2L).build();
        var srcAccount = new Account();
        var amount = BigDecimal.TEN;

        given(accountRepository.findByNumber(destAccountNum)).willReturn(Optional.of(destAccount));
        given(accountRepository.findById(srcAccountId)).willReturn(Optional.of(srcAccount));

        assertThatNoException().isThrownBy(() ->
            transactionService.makeAccountTransaction(destAccountNum, srcAccountId, amount));
    }

    @Test
    void makeCardTransaction() {
        var destCardNum = "1234567891234567";
        var srcCardId = 1L;
        var destCard = Card.builder().id(2L).accountId(1L).build();
        var srcCard = Card.builder().id(1L).accountId(2L).build();
        var amount = BigDecimal.TEN;

        given(cardRepository.findByNum(destCardNum)).willReturn(Optional.of(destCard));
        given(cardRepository.findById(srcCardId)).willReturn(Optional.of(srcCard));
        given(accountRepository.findById(destCard.getAccountId())).willReturn(Optional.of(new Account()));

        assertThatNoException().isThrownBy(() ->
            transactionService.makeCardTransaction(destCardNum, srcCardId, amount));
    }
}