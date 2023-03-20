package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.repository.AccountRepository;
import com.innowise.innowisebankproject.repository.AccountTransactionRepository;
import com.innowise.innowisebankproject.service.TransactionService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.math.BigDecimal;

@Stateless
public class TransactionServiceImpl implements TransactionService {

    @EJB
    private AccountTransactionRepository accountTransactionRepository;
    @EJB
    private AccountRepository accountRepository;

    @Override
    public void makeAccountTransaction(String toAccountNumber,
                                       long fromAccountId,
                                       BigDecimal amount,
                                       String currencies) {

        var destAccountId =
            accountRepository.findAccountByAccountNumber(toAccountNumber).orElseThrow(() -> {
                throw new ResourceNotFoundException("Account with such number was not found");
            }).getId();

        accountTransactionRepository.madeAccountTransaction(
            destAccountId, fromAccountId, amount, currencies
        );

    }
}
