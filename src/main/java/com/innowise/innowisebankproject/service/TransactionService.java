package com.innowise.innowisebankproject.service;

import com.innowise.innowisebankproject.entity.AccountTransaction;
import com.innowise.innowisebankproject.entity.CardTransaction;
import jakarta.ejb.Local;
import java.math.BigDecimal;

@Local
public interface TransactionService {

    AccountTransaction makeAccountTransaction(String destAccountNum, long srcAccountId, BigDecimal amount);
    CardTransaction makeCardTransaction(String destCardNum, long srcCardId, BigDecimal amount);
}
