package com.innowise.innowisebankproject.repository;

import com.innowise.innowisebankproject.entity.Account;
import jakarta.ejb.Local;
import java.util.List;
import java.util.Optional;

@Local
public interface AccountRepository {

    Account add(Account account);
    Optional<Account> findByNumber(String number);
    Optional<Account> findById(Long id);

    List<Account> findAccountsByUserId(Long id);
}
