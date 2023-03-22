package com.innowise.innowisebankproject.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.innowise.innowisebankproject.entity.Account;
import com.innowise.innowisebankproject.entity.CurrencyType;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.repository.AccountRepository;
import com.innowise.innowisebankproject.service.impl.AccountServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    void createAccountTest() {

        var account = Account.builder()
            .currencyType(CurrencyType.USD)
            .build();

        var createdAccount = accountService.createAccount(account, 1L);

        var accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).add(accountCaptor.capture());

        assertThat(createdAccount).isEqualTo(accountCaptor.getValue());
    }

    @Test
    void findByNumTest() {
        var num = "1234567891234567";
        var account = Account.builder()
            .id(1L)
            .number(num)
            .build();

        given(accountRepository.findAccountByAccountNumber(num)).willReturn(Optional.of(account));
        var foundedAccount = accountService.findByNum(num);

        assertThat(foundedAccount).isEqualTo(account);
    }

    @Test
    void findByNonExistingNumTest() {
        var num = "1234567891234567";

        given(accountRepository.findAccountByAccountNumber(num)).willReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findByNum(num))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Account with such number was not found");
    }

    @Test
    void findByIdTest() {
        var id = 1L;
        var account = Account.builder()
            .id(id)
            .number("1234567891234567")
            .build();

        given(accountRepository.findAccountById(id)).willReturn(Optional.of(account));
        var foundedAccount = accountService.findById(id);

        assertThat(foundedAccount).isEqualTo(account);
    }

    @Test
    void findByNonExistingId() {
        var id = 1L;

        given(accountRepository.findAccountById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Account with such id was not found");
    }

    @Test
    void getAccountsByUserIdTest() {
        var userId = 1L;
        var account = Account.builder()
            .id(1L)
            .userId(userId)
            .number("1234567891234567")
            .build();

        given(accountRepository.findAccountsByUserId(userId)).willReturn(List.of(account));
        var accounts = accountService.getAccountsByUserId(userId);

        assertThat(accounts.get(0)).isEqualTo(account);
    }

    @Test
    void getAccountsByNonExistingUserIdTest() {
        var userId = 1L;

        given(accountRepository.findAccountsByUserId(userId)).willReturn(List.of());
        var accounts = accountService.getAccountsByUserId(userId);

        assertThat(accounts.isEmpty()).isTrue();
    }
}