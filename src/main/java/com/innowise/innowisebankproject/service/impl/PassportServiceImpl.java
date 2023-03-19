package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.repository.PassportRepository;
import com.innowise.innowisebankproject.service.PassportService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class PassportServiceImpl implements PassportService {

    @EJB
    PassportRepository passportRepository;

    @Override
    public Passport save(Passport passport) {
        return passportRepository.add(passport);
    }

    @Override
    public Passport getById(Long id) {
        return passportRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Passport with this id was not found");
        });
    }
}
