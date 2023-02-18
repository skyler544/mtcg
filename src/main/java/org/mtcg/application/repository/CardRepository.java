package org.mtcg.application.repository;

import java.util.List;

import org.mtcg.application.model.Card;

public interface CardRepository {
    void saveCard(Card card) throws IllegalStateException;
    Card findCardById(String id) throws IllegalStateException;

    List<Card> findCardsByOwner(String token) throws IllegalStateException;
    void saveCardOwner(String id, String token) throws IllegalStateException;
}
