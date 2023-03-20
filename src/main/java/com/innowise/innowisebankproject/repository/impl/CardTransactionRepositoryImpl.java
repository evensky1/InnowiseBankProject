package com.innowise.innowisebankproject.repository.impl;

import com.innowise.innowisebankproject.entity.CardTransaction;
import com.innowise.innowisebankproject.repository.CardTransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CardTransactionRepositoryImpl implements CardTransactionRepository {

    @PersistenceContext(unitName = "BankPersistenceProvider")
    private EntityManager entityManager;

    @Override
    public CardTransaction logTransaction(CardTransaction cardTransaction) {
        entityManager.persist(cardTransaction);
        return cardTransaction;
    }
}
