package com.innowise.innowisebankproject.repository;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.entity.Person;
import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    Person add(Person person);
    Optional<Person> findById(Long id);
    Optional<Person> findByEmail(String email);
    List<Person> findAll();

    int attachPassport(String email, Passport passport);
}
