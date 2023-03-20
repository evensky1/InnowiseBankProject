package com.innowise.innowisebankproject.repository;

import com.innowise.innowisebankproject.entity.Card;
import jakarta.ejb.Local;
import java.util.List;
import java.util.Optional;

@Local
public interface CardRepository {

    Card add(Card card);
    List<Card> getAllByUserId(Long id);
    Optional<Card> findById(Long id);

    Optional<Card> findByNum(String number);
}
