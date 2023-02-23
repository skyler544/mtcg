package org.mtcg.application.service;

import java.util.List;
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
        List<String> packages = cardRepository.getAvailablePackages();
        if (!packages.isEmpty()) {
            cardRepository.setPackageOwner(packages.get(0), token);
        } else {
            throw new NotFoundException("No packages available.");
        }
    }

    public String returnUserCards(String token) {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(cardRepository.findCardsByOwner(token));
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

    public String getUserDeck(String token) {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(cardRepository.getUserDeck(token));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public String getUserDeckPlainText(String token) {
        List<Card> deck = cardRepository.getUserDeck(token);
        StringBuilder result = new StringBuilder();
        for(var card : deck) {
            result.append("Name: " + card.getName() + "\n");
            result.append("Damage: " + card.getDamage() + "\n\n");
        }
        return result.toString();
    }
}
