package com.innowise.innowisebankproject.repository.impl;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.repository.PassportRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Stateless
public class PassportRepositoryImpl implements PassportRepository {

    @PersistenceContext(unitName = "BankPersistenceProvider")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Passport add(Passport passport) {
        entityManager.persist(passport);
        return passport;
    }

    @Override
    public Optional<Passport> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Passport.class, id));
    }
}
