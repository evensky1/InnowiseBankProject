package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.AccountTransaction;
import com.innowise.innowisebankproject.entity.CardTransaction;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.repository.AccountRepository;
import com.innowise.innowisebankproject.repository.AccountTransactionRepository;
import com.innowise.innowisebankproject.repository.CardRepository;
import com.innowise.innowisebankproject.repository.CardTransactionRepository;
import com.innowise.innowisebankproject.service.TransactionService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Stateless
@AllArgsConstructor
@NoArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @EJB
    private AccountTransactionRepository accountTransactionRepository;
    @EJB
    private AccountRepository accountRepository;
    @EJB
    private CardRepository cardRepository;
    @EJB
    private CardTransactionRepository cardTransactionRepository;

    @Override
    public AccountTransaction makeAccountTransaction(String destAccountNum, long srcAccountId, BigDecimal amount) {

        var destAccount =
            accountRepository.findByNumber(destAccountNum).orElseThrow(() -> {
                throw new ResourceNotFoundException("Account with such number was not found");
            });

        var srcAccount =
            accountRepository.findById(srcAccountId).orElseThrow(() -> {
                throw new ResourceNotFoundException("Account with such number was not found");
            });

        accountTransactionRepository.transfer(destAccount.getId(), srcAccountId, amount);

        var accountTransaction = AccountTransaction.builder()
            .toAccount(destAccount)
            .fromAccount(srcAccount)
            .amount(amount)
            .currencyType(srcAccount.getCurrencyType())
            .build();

        return accountTransactionRepository.logTransaction(accountTransaction);
    }

    @Override
    public CardTransaction makeCardTransaction(String destCardNum, long srcCardId, BigDecimal amount) {
        var destCard = cardRepository.findByNum(destCardNum).orElseThrow(() -> {
            throw new ResourceNotFoundException("Card with such number was not found");
        });
        var srcCard = cardRepository.findById(srcCardId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Card with such number was not found");
        });

        accountTransactionRepository.transfer(destCard.getAccountId(), srcCard.getAccountId(), amount);
        var currencyType = accountRepository.findById(destCard.getAccountId())
            .orElseThrow(() -> {
                throw new ResourceNotFoundException("Account with such id was not found");
            })
            .getCurrencyType();

        var cardTransaction = CardTransaction.builder()
            .toCard(destCard)
            .fromCard(srcCard)
            .amount(amount)
            .currencyType(currencyType)
            .build();

        return cardTransactionRepository.logTransaction(cardTransaction);
    }
}
