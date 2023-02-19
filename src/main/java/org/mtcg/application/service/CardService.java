package org.mtcg.application.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mtcg.application.model.Card;
import org.mtcg.application.repository.CardRepository;
import org.mtcg.http.exception.ConflictException;
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
}
