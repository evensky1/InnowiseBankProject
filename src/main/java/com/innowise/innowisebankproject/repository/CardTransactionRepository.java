package com.innowise.innowisebankproject.repository;

import com.innowise.innowisebankproject.entity.CardTransaction;
import jakarta.ejb.Local;

@Local
public interface CardTransactionRepository {

    CardTransaction logTransaction(CardTransaction cardTransaction);
}
