package com.innowise.innowisebankproject.service.impl;

import com.innowise.innowisebankproject.entity.Account;
import com.innowise.innowisebankproject.exception.ResourceNotFoundException;
import com.innowise.innowisebankproject.repository.AccountRepository;
import com.innowise.innowisebankproject.service.AccountService;
import com.innowise.innowisebankproject.util.ExceptionMessages;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Stateless
@AllArgsConstructor
@NoArgsConstructor
public class AccountServiceImpl implements AccountService {

    @EJB
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account cardAccount, Long userId) {
        var accountNum = (long) (1000_0000_0000_0000L + Math.random() * (Long.MAX_VALUE / 1000L));

        var randomBalanceNum = (int) (1000 + Math.random() * 9999);
        var iban = "BY" + "24" + "1234" + randomBalanceNum + accountNum;

        var newAccount = Account.builder()
            .currencyType(cardAccount.getCurrencyType())
            .money(BigDecimal.ZERO)
            .number(String.valueOf(accountNum))
            .iban(iban)
            .userId(userId)
            .build();

        accountRepository.add(newAccount);

        return newAccount;
    }

    @Override
    public Account findByNum(String number) {
        return accountRepository.findByNumber(number).orElseThrow(() -> {
            throw new ResourceNotFoundException(ExceptionMessages.ACCOUNT_BY_NUM_NOT_FOUND);
        });
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(ExceptionMessages.ACCOUNT_BY_ID_NOT_FOUND);
        });
    }

    @Override
    public List<Account> getAccountsByUserId(Long id) {
        return accountRepository.findAccountsByUserId(id);
    }
}
