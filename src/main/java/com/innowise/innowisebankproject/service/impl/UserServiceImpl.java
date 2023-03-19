package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.exception.AuthorizationException;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.exception.ResourceUpdateException;
import com.innowise.innowisebankproject.repository.UserRepository;
import com.innowise.innowisebankproject.security.JwtService;
import com.innowise.innowisebankproject.service.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UserServiceImpl implements UserService {

    @EJB
    private UserRepository userRepository;
    @EJB
    private JwtService jwtService;

    @Override
    public User save(User user) {
        var passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(passwordHash);

        return userRepository.add(user);
    }

    @Override
    public void attachPassport(String email, Passport passport) {
        if (userRepository.attachPassport(email, passport) == 0) {
            throw new ResourceUpdateException("User was not found or passport already attached");
        }
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Such user does not exist");
        });
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public String authorize(User user) {
        var registeredPerson =
            userRepository.findByEmail(user.getEmail()).orElseThrow(() -> {
                throw new AuthorizationException("User wasn't found");
            });

        if (BCrypt.checkpw(user.getPassword(), registeredPerson.getPassword())) {
            return jwtService.generateJwt(user.getEmail());
        } else {
            throw new AuthorizationException("Incorrect password");
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User with such email does not exist");
        });
    }
}
