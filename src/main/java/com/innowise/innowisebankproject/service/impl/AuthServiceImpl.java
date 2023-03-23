package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.exception.AuthorizationException;
import com.innowise.innowisebankproject.repository.UserRepository;
import com.innowise.innowisebankproject.security.JwtService;
import com.innowise.innowisebankproject.service.AuthService;
import com.innowise.innowisebankproject.util.ExceptionMessages;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
@AllArgsConstructor
@NoArgsConstructor
public class AuthServiceImpl implements AuthService {

    @EJB
    private UserRepository userRepository;
    @EJB
    private JwtService jwtService;

    @Override
    public String authorize(User user) {
        var registeredPerson =
            userRepository.findByEmail(user.getEmail()).orElseThrow(() -> {
                throw new AuthorizationException(ExceptionMessages.USER_BY_EMAIL_NOT_FOUND);
            });

        if (BCrypt.checkpw(user.getPassword(), registeredPerson.getPassword())) {
            return jwtService.generateJwt(registeredPerson);
        } else {
            throw new AuthorizationException(ExceptionMessages.INCORRECT_PASSWORD);
        }
    }
}
