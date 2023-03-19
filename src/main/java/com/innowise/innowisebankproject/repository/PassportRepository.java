package com.innowise.innowisebankproject.repository;

import com.innowise.innowisebankproject.entity.Passport;
import jakarta.ejb.Local;
import java.util.Optional;

@Local
public interface PassportRepository {

    Passport add(Passport passport);
    Optional<Passport> findById(Long id);
}
