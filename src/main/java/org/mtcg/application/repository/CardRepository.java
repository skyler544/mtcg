package org.mtcg.application.repository;

import org.mtcg.application.model.Card;

public interface CardRepository {
    void saveCard(Card card) throws IllegalStateException;
    String findCardByOwner(String token) throws IllegalStateException;

    void saveCardOwner(String token) throws IllegalStateException;
}
