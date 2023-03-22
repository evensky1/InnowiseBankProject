package com.innowise.innowisebankproject.repository.impl;

import com.innowise.innowisebankproject.entity.Account;
import com.innowise.innowisebankproject.repository.AccountRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Stateless
public class AccountRepositoryImpl implements AccountRepository {

    @PersistenceContext(unitName = "BankPersistenceProvider")
    private EntityManager entityManager;

    @Override
    public Account add(Account account) {
        entityManager.persist(account);
        return account;
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        var query = entityManager
            .createQuery("SELECT a FROM Account a WHERE a.number LIKE :number")
            .setParameter("number", number);

        return query.getResultStream().findFirst();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    @Override
    public List<Account> findAccountsByUserId(Long id) {
        var query = entityManager
            .createQuery("SELECT a FROM Account a WHERE a.userId = :userId")
            .setParameter("userId", id);

        return query.getResultList();
    }
}
