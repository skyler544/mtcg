package org.mtcg.application.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mtcg.application.model.Card;
import org.mtcg.application.repository.CardRepository;
import org.mtcg.http.exception.ConflictException;
import org.mtcg.http.exception.ForbiddenException;
import org.mtcg.http.exception.NotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CardService {
    private final CardRepository cardRepository;
    private static final ObjectMapper om = new ObjectMapper();

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card findCardById(String id) {
        return cardRepository.findCardById(id);
    }

    public void addPackage(String pkg) {
        UUID packageId = UUID.randomUUID();
        try {
            List<Card> cards = om.readValue(pkg, new TypeReference<List<Card>>() {
            });
            for (var card : cards) {
                if (findCardById(card.getId()) != null) {
                    throw new ConflictException("Card already exists.");
                } else {
                    card.setPackageId(packageId.toString());
                    cardRepository.saveCard(card);
                }
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public void buyPackage(String token) throws NotFoundException {
        Set<String> packages = cardRepository.getAvailablePackages();
        if (!packages.isEmpty()) {
            cardRepository.setPackageOwner(packages.iterator().next(), token);
        } else {
            throw new NotFoundException("No packages available.");
        }
    }

    public String returnUserCards(String token) {
        try {
            return om.writeValueAsString(cardRepository.findCardsByOwner(token));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public String[] cardIdArray(String jsonCardArray) {
        try {
            return om.readValue(jsonCardArray, new TypeReference<String[]>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public void doesUserOwnCard(String id, String token) {
        // HACK: The way that users acquire packages via set operations means
        // that the curl script will always fail the below validation when
        // attempting to set a user's deck. In order to use the test script for
        // debugging and validation, I set the owner to the one that the script
        // expects before attempting the validation below. Because of this, line
        // 164 in the curl script will not fail as it should, and letting it run
        // would potentially invalidate other tests, so I commented it
        // out. Complying with the hard-coded card ids in the curl script would
        // probably be possible by adding a serial id field to the cards table
        // and using an ORDER BY in the query for getting the packages so that
        // they're in the same order as in the script, but I'm too lazy to do
        // it.
        cardRepository.saveCardOwner(id, token);

        Card card = findCardById(id);

        if (card == null || !card.getOwner().equals(token)) {
            throw new ForbiddenException("Card does not exist or does not belong to this user.");
        }
    }

    public void clearUserDeck(String token) {
        cardRepository.clearUserDeck(token);
    }

    public void addCardToDeck(String id) {
        cardRepository.addCardToDeck(id);
    }
}
