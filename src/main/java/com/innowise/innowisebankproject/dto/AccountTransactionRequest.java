package com.innowise.innowisebankproject.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountTransactionRequest {
    private String destAccountNum;
    private Long accountId;
    private BigDecimal amount;
    private String currencies;
}