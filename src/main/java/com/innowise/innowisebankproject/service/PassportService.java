package com.innowise.innowisebankproject.service;

import com.innowise.innowisebankproject.entity.Passport;
import jakarta.ejb.Local;

@Local
public interface PassportService {

    Passport save(Passport passport);
    Passport getById(Long id);
}
