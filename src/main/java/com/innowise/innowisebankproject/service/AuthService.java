package com.innowise.innowisebankproject.service;

import com.innowise.innowisebankproject.entity.User;
import jakarta.ejb.Local;

@Local
public interface AuthService {

    String authorize(User user);
}
