package com.innowise.innowisebankproject.repository;

import com.innowise.innowisebankproject.entity.AccountTransaction;
import jakarta.ejb.Local;
import java.math.BigDecimal;
import java.util.List;

@Local
public interface AccountTransactionRepository {

    void transfer(long toAccountId, long fromAccountId, BigDecimal amount);
    List<AccountTransaction> findTransactionsByAccountId(Long id);
    AccountTransaction logTransaction(AccountTransaction accountTransaction);
}
