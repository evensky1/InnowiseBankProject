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
public class CardTransactionRequest {
    private String destCardNum;
    private Long srcCardId;
    private BigDecimal amount;
}
