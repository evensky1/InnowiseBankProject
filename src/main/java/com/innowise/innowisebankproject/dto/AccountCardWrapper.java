package com.innowise.innowisebankproject.dto;

import com.innowise.innowisebankproject.entity.Account;
import com.innowise.innowisebankproject.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountCardWrapper {
    private Account account;
    private Card card;
}