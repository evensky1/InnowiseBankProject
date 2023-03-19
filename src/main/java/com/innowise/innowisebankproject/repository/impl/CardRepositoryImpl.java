package com.innowise.innowisebankproject.repository.impl;

import com.innowise.innowisebankproject.entity.Card;
import com.innowise.innowisebankproject.repository.CardRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Stateless
public class CardRepositoryImpl implements CardRepository {

    @PersistenceContext(unitName = "BankPersistenceProvider")
    private EntityManager entityManager;

    @Override
    public Card add(Card card) {
        entityManager.persist(card);
        return card;
    }

    @Override
    public List<Card> getAllByUserId(Long id) {
        var query = entityManager.createQuery("SELECT c FROM Card c WHERE c.userId = :userId")
            .setParameter("userId", id);

        return (List<Card>) query.getResultList();
    }

    @Override
    public Optional<Card> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Card.class, id));
    }
}
