package com.innowise.innowisebankproject.repository.impl;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext(unitName = "BankPersistenceProvider")
    private EntityManager entityManager;

    @Override
    @Transactional
    public User add(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var query = entityManager
            .createQuery("SELECT p FROM User p WHERE p.email LIKE :email")
            .setParameter("email", email);

        return query.getResultStream().findFirst();
    }

    @Override
    public List<User> findAll() {
        var query = entityManager.createQuery("SELECT p FROM User p");
        return query.getResultList();
    }

    @Override
    @Transactional
    public int attachPassport(String email, Passport passport) {
        return entityManager
            .createQuery("UPDATE User p SET p.passport = :passport WHERE p.email LIKE :email")
            .setParameter("email", email)
            .setParameter("passport", passport)
            .executeUpdate();
    }
}
