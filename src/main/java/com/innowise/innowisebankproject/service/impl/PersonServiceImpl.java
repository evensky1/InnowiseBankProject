package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.entity.Person;
import com.innowise.innowisebankproject.exception.AuthorizationException;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.exception.ResourceUpdateException;
import com.innowise.innowisebankproject.repository.PersonRepository;
import com.innowise.innowisebankproject.security.JwtService;
import com.innowise.innowisebankproject.service.PersonService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class PersonServiceImpl implements PersonService {

    @EJB
    private PersonRepository personRepository;
    @EJB
    private JwtService jwtService;

    @Override
    public Person savePerson(Person person) {
        var passwordHash = BCrypt.hashpw(person.getPassword(), BCrypt.gensalt());
        person.setPassword(passwordHash);

        return personRepository.add(person);
    }

    @Override
    public void attachPassport(String email, Passport passport) {
        if (personRepository.attachPassport(email, passport) == 0) {
            throw new ResourceUpdateException("User was not found or passport already attached");
        }
    }

    @Override
    public Person findPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Such user does not exist");
        });
    }

    @Override
    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public String authorizePerson(Person person) {
        var registeredPerson =
            personRepository.findByEmail(person.getEmail()).orElseThrow(() -> {
                throw new AuthorizationException("User wasn't found");
            });

        if (BCrypt.checkpw(person.getPassword(), registeredPerson.getPassword())) {
            return jwtService.generateJwt(person.getEmail());
        } else {
            throw new AuthorizationException("Incorrect password");
        }
    }

    @Override
    public Person findPersonByEmail(String email) {
        return personRepository.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("Person with such email does not exist");
        });
    }
}
