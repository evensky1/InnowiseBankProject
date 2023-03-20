package com.innowise.innowisebankproject.repository;

import com.innowise.innowisebankproject.entity.AccountTransaction;
import jakarta.ejb.Local;
import java.math.BigDecimal;
import java.util.List;

@Local
public interface AccountTransactionRepository {

    void madeAccountTransaction(long toAccountId,
                                   long fromAccountId,
                                   BigDecimal amount,
                                   String currencies);

    List<AccountTransaction> findTransactionsByAccountId(Long id);

}
