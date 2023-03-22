package com.innowise.innowisebankproject.service;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.entity.User;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface UserService {

    User save(User user);
    void attachPassport(String email, Passport passport);
    User findUserById(Long id);
    List<User> findAll();
    User findUserByEmail(String email);
}
