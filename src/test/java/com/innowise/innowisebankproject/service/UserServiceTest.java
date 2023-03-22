package com.innowise.innowisebankproject.service;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.exception.ResourceUpdateException;
import com.innowise.innowisebankproject.repository.UserRepository;
import com.innowise.innowisebankproject.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void findAllTest() {
        userService.findAll();
        verify(userRepository).findAll();
    }

    @Test
    void userSaveTest() {
        var plainPassword = "abc123";
        var user = User.builder()
            .email("tempuser@gmail.com")
            .password(plainPassword)
            .build();

        userService.save(user);

        var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).add(userCaptor.capture());

        var savedUser = userCaptor.getValue();

        assertThat(savedUser).isEqualTo(user);
        assertThat(BCrypt.checkpw(plainPassword, savedUser.getPassword())).isTrue();
    }

    @Test
    void attachPassportTest() {
        var email = "tempuser@gmail.com";
        var passport = new Passport();

        given(userRepository.attachPassport(email, passport)).willReturn(1);

        userService.attachPassport(email, passport);
        verify(userRepository).attachPassport(email, passport);
    }

    @Test
    void attachPassportBadInputTest() {
        var email = "tempuser@gmail.com";
        var passport = new Passport();

        given(userRepository.attachPassport(email, passport)).willReturn(0);

        assertThatThrownBy(() -> userService.attachPassport(email, passport))
            .isInstanceOf(ResourceUpdateException.class)
            .hasMessage("User was not found or passport already attached");
    }

    @Test
    void findUserByIdTest() {
        var id = 1L;
        var user = User.builder()
            .id(id)
            .email("tempuser@gmail.com")
            .password("pswhash")
            .passport(new Passport())
            .build();

        given(userRepository.findById(id)).willReturn(Optional.of(user));

        var foundedUser = userService.findUserById(id);
        verify(userRepository).findById(id);

        assertThat(foundedUser).isEqualTo(user);
    }

    @Test
    void findNonExistingUserByIdTest() {
        var id = 1L;

        given(userRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() ->  userService.findUserById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Such user does not exist");
    }

    @Test
    void findUserByEmailTest() {
        var email = "tempuser@gmail.com";
        var user = User.builder()
            .id(1L)
            .email(email)
            .password("pswhash")
            .passport(new Passport())
            .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        var foundedUser = userService.findUserByEmail(email);
        verify(userRepository).findByEmail(email);

        assertThat(foundedUser).isEqualTo(user);
    }

    @Test
    void findNonExistingUserByEmailTest() {
        var email = "tempuser@gmail.com";

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        assertThatThrownBy(() ->  userService.findUserByEmail(email))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("User with such email does not exist");
    }
}