package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.exception.ResourceUpdateException;
import com.innowise.innowisebankproject.repository.UserRepository;
import com.innowise.innowisebankproject.service.UserService;
import com.innowise.innowisebankproject.util.ExceptionMessages;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @EJB
    private UserRepository userRepository;

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
            throw new ResourceNotFoundException(ExceptionMessages.USER_BY_ID_NOT_FOUND);
        });
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException(ExceptionMessages.USER_BY_EMAIL_NOT_FOUND);
        });
    }
}
