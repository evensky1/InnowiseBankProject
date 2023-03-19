package com.innowise.innowisebankproject.repository;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User add(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();

    int attachPassport(String email, Passport passport);
}
