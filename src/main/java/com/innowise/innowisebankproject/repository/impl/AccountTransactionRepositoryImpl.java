package com.innowise.innowisebankproject.repository.impl;

import com.innowise.innowisebankproject.entity.Account;
import com.innowise.innowisebankproject.entity.AccountTransaction;
import com.innowise.innowisebankproject.exception.TransactionException;
import com.innowise.innowisebankproject.repository.AccountTransactionRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class AccountTransactionRepositoryImpl implements AccountTransactionRepository {

    @PersistenceContext(unitName = "BankPersistenceProvider")
    private EntityManager entityManager;

    @Override
    @Transactional
    public void transfer(long toAccountId, long fromAccountId, BigDecimal amount) {
        var query = entityManager.createQuery("SELECT ac FROM Account ac WHERE ac.id = :id");

        var sourceAccount = (Account) query.setParameter("id", fromAccountId).getSingleResult();
        var destAccount = (Account) query.setParameter("id", toAccountId).getSingleResult();

        if (!sourceAccount.sendMoney(destAccount, amount)) {
            throw new TransactionException("Money transfer goes wrong");
        }
    }


    @Override
    public List<AccountTransaction> findTransactionsByAccountId(Long id) {
        var query = entityManager.createQuery("SELECT t FROM AccountTransaction t WHERE t.fromAccount = :id")
            .setParameter("id", id);

        return (List<AccountTransaction>) query.getResultList();
    }

    @Override
    public AccountTransaction logTransaction(AccountTransaction accountTransaction) {
        entityManager.persist(accountTransaction);
        return accountTransaction;
    }
}
