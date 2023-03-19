package com.innowise.innowisebankproject.service;

import com.innowise.innowisebankproject.entity.Account;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface AccountService {

    Account createAccount(Account cardAccount, Long userId);
    Account findByNum(String number);
    Account findById(Long id);
    List<Account> getAccountsByUserId(Long id);
}
