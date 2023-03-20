package com.innowise.innowisebankproject.service;

import jakarta.ejb.Local;
import java.math.BigDecimal;

@Local
public interface TransactionService {

    void makeAccountTransaction(String toAccountNumber, long fromAccountId, BigDecimal amount, String currencies);
}
