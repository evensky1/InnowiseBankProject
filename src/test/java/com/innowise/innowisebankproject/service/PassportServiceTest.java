package com.innowise.innowisebankproject.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.repository.PassportRepository;
import com.innowise.innowisebankproject.service.impl.PassportServiceImpl;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PassportServiceTest {

    @Mock
    private PassportRepository passportRepository;
    private PassportService passportService;

    @BeforeEach
    void setUp() {
        passportService = new PassportServiceImpl(passportRepository);
    }

    @Test
    void save() {
        var passport = Passport.builder()
            .identityCode("no matter")
            .name("arthur")
            .surname("connan")
            .succession("alfred")
            .dob(LocalDate.of(2002, 9, 6))
            .build();
        passportService.save(passport);

        var passportCapture = ArgumentCaptor.forClass(Passport.class);
        verify(passportRepository).add(passportCapture.capture());

        var savedPassport = passportCapture.getValue();
        assertThat(savedPassport).isEqualTo(passport);
    }

    @Test
    void getById() {
        var id = 1L;
        var passport = Passport.builder()
            .id(id)
            .identityCode("no matter")
            .name("arthur")
            .surname("connan")
            .succession("alfred")
            .dob(LocalDate.of(2002, 9, 6))
            .build();

        given(passportRepository.findById(id)).willReturn(Optional.of(passport));
        var foundedPassport = passportService.getById(id);

        assertThat(foundedPassport).isEqualTo(passport);
    }

    @Test
    void getByNonExistingId() {
        var id = 1L;

        given(passportRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> passportService.getById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Passport with such id was not found");
    }
}