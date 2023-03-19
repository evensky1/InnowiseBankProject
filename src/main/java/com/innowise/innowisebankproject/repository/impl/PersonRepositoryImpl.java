package com.innowise.innowisebankproject.repository.impl;

import com.innowise.innowisebankproject.entity.Person;
import com.innowise.innowisebankproject.repository.PersonRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Stateless
public class PersonRepositoryImpl implements PersonRepository {

    @PersistenceContext(unitName = "BankPersistenceProvider")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Person add(Person person) {
        entityManager.persist(person);
        return person;
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Person.class, id));
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        var query = entityManager.createQuery("SELECT p FROM Person p WHERE p.email LIKE :email");

        return Optional.ofNullable(
            (Person) query.setParameter("email", email).getSingleResult()
        );
    }

    @Override
    public List<Person> findAll() {
        var query = entityManager.createQuery("SELECT p FROM Person p");
        return (List<Person>) query.getResultList();
    }
}