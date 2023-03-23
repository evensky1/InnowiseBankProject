package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.repository.PassportRepository;
import com.innowise.innowisebankproject.service.PassportService;
import com.innowise.innowisebankproject.util.ExceptionMessages;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Stateless
@AllArgsConstructor
@NoArgsConstructor
public class PassportServiceImpl implements PassportService {

    @EJB
    private PassportRepository passportRepository;

    @Override
    public Passport save(Passport passport) {
        return passportRepository.add(passport);
    }

    @Override
    public Passport getById(Long id) {
        return passportRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(ExceptionMessages.PASSPORT_BY_ID_NOT_FOUND);
        });
    }
}
