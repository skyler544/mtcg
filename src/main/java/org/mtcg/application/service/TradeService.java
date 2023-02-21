package org.mtcg.application.service;

import org.mtcg.application.model.Card;
import org.mtcg.application.model.Trade;
import org.mtcg.application.repository.TradeRepository;
import org.mtcg.http.exception.ConflictException;
import org.mtcg.http.exception.ForbiddenException;
import org.mtcg.http.exception.NotFoundException;
import org.mtcg.application.repository.CardRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TradeService {
    private final TradeRepository tradeRepository;
    private final CardRepository cardRepository;
    private static final ObjectMapper om = new ObjectMapper();

    public TradeService(TradeRepository tradeRepository, CardRepository cardRepository) {
        this.tradeRepository = tradeRepository;
        this.cardRepository = cardRepository;
    }

    public Trade findTradeById(String id) {
        return tradeRepository.getTradeById(id);
    }

    public void checkCardInDeckAndUserOwnsCard(Trade trade, String token) {
        Card card = cardRepository.findCardById(trade.getCardId());

        if (card.getInDeck() != 0 || !card.getOwner().equals(token)) {
            throw new ForbiddenException("Card in deck or not owned by this user.");
        }
    }

    public Trade parseTrade(String trade) {
        Trade offer;
        try {
            offer = om.readValue(trade, Trade.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
        return offer;
    }

    public void postTrade(String trade, String token) {
        Trade offer = parseTrade(trade);

        if (findTradeById(offer.getId()) != null) {
            throw new ConflictException("A trade with this ID already exists.");
        }
        checkCardInDeckAndUserOwnsCard(offer, token);

        tradeRepository.postTrade(offer);
    }

    public String getCurrentTradings() {
        try {
            return om.writeValueAsString(tradeRepository.getCurrentTradings());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public void trade(String tradeId, String offeredCardId, String token) {
        Trade trade = findTradeById(tradeId);

        if (trade == null) {
            throw new NotFoundException("The provided deal ID was not found.");
        }

        Card offeredCard = cardRepository.findCardById(offeredCardId);
        Card cardFromTrade = cardRepository.findCardById(trade.getCardId());

        if (offeredCard == null || cardFromTrade == null) {
            throw new NotFoundException("Card does not exist.");
        }

        String offeredCardOwner = offeredCard.getOwner();
        String cardFromTradeOwner = cardFromTrade.getOwner();

        String type = offeredCard.getName().toLowerCase().contains("spell") ? "spell" : "monster";

        if (!offeredCardOwner.equals(token)) {
            throw new ForbiddenException("You may not trade cards you do not own.");
        }
        if (offeredCardOwner.equals(cardFromTradeOwner)) {
            throw new ForbiddenException("You may not trade with yourself");
        }
        if (!type.equals(trade.getType())) {
            throw new ForbiddenException("The card offered is not the required type.");
        }
        if (offeredCard.getDamage() < trade.getDamage()) {
            throw new ForbiddenException("The card offered is not strong enough.");
        }
        if (offeredCard.getInDeck() + cardFromTrade.getInDeck() != 0) {
            throw new ForbiddenException("You may not trade cards which are currently in the deck.");
        }

        // if (!offeredCardOwner.equals(token)
        //         || offeredCardOwner.equals(cardFromTradeOwner)
        //         || !type.equals(trade.getType())
        //         || offeredCard.getDamage() < trade.getDamage()
        //         || offeredCard.getInDeck() + cardFromTrade.getInDeck() != 0) {
        //     throw new ForbiddenException("Trade conditions not met.");
        // }

        cardRepository.saveCardOwner(offeredCard.getId(), cardFromTradeOwner);
        cardRepository.saveCardOwner(cardFromTrade.getId(), offeredCardOwner);

        tradeRepository.deleteTrade(tradeId);
    }
}
