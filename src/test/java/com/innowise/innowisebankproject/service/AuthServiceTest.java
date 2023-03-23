package com.innowise.innowisebankproject.service;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.exception.AuthorizationException;
import com.innowise.innowisebankproject.repository.UserRepository;
import com.innowise.innowisebankproject.security.JwtService;
import com.innowise.innowisebankproject.service.impl.AuthServiceImpl;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    private JwtService jwtService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        authService = new AuthServiceImpl(userRepository, jwtService);
    }

    @Test
    void authorizeTest() {
        var email = "tempuser@gmail.com";
        var password = "abc123";

        var registeredUser = User.builder()
            .id(1L)
            .email(email)
            .password(BCrypt.hashpw(password, BCrypt.gensalt()))
            .roles(new HashSet<>())
            .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(registeredUser));

        var user = User.builder()
            .id(1L)
            .email(email)
            .password(password)
            .roles(new HashSet<>())
            .build();
        var jwt = authService.authorize(user);

        assertThatNoException().isThrownBy(() -> jwtService.validateToken(jwt));
    }

    @Test
    void authorizeByNonExistingEmail() {
        var email = "tempuser@gmail.com";
        var password = "abc123";

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        var user = User.builder()
            .id(1L)
            .email(email)
            .password(password)
            .build();

        assertThatThrownBy(() -> authService.authorize(user))
            .isInstanceOf(AuthorizationException.class)
            .hasMessage("User with such email was not found");
    }

    @Test
    void authorizeByIncorrectPassword() {
        var email = "tempuser@gmail.com";
        var password = "abc123";

        var registeredUser = User.builder()
            .id(1L)
            .email(email)
            .password(BCrypt.hashpw(password, BCrypt.gensalt()))
            .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(registeredUser));

        var user = User.builder()
            .id(1L)
            .email(email)
            .password(password + "redundant string")
            .build();

        assertThatThrownBy(() -> authService.authorize(user))
            .isInstanceOf(AuthorizationException.class)
            .hasMessage("Incorrect password");
    }
}