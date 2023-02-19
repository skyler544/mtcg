package org.mtcg.application.repository;

import java.util.List;
import java.util.Set;

import org.mtcg.application.model.Card;

public interface CardRepository {
    void saveCard(Card card) throws IllegalStateException;
    Card findCardById(String id) throws IllegalStateException;

    List<Card> findCardsByOwner(String token) throws IllegalStateException;
    void saveCardOwner(String id, String token) throws IllegalStateException;

    Set<String> getAvailablePackages() throws IllegalStateException;
    List<Card> retrievePackage(String packageId) throws IllegalStateException;
    void setPackageOwner(String packageId, String token) throws IllegalStateException;

    void addCardToDeck(String id) throws IllegalStateException;
    List<Card> getUserDeck(String token) throws IllegalStateException;
    void clearUserDeck(String token) throws IllegalStateException;
}
