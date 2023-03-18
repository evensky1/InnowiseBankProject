package com.innowise.innowisebankproject.service;

import com.innowise.innowisebankproject.entity.Person;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface PersonService {

    Person savePerson(Person person);
    Person updatePerson(Person person);
    Person findPersonById(Long id);
    List<Person> findAllPersons();

    String authorizePerson(Person person);
}
