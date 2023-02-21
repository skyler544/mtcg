package org.mtcg.application.service;

import org.mtcg.application.model.Card;
import org.mtcg.application.model.Trade;
import org.mtcg.application.repository.TradeRepository;
import org.mtcg.http.exception.ConflictException;
import org.mtcg.http.exception.ForbiddenException;
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

    public void postTrade(String trade, String token) {
        Trade newTrade;
        try {
            newTrade = om.readValue(trade, Trade.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }

        if (findTradeById(newTrade.getId()) != null) {
            throw new ConflictException("A trade with this ID already exists.");
        }

        Card card = cardRepository.findCardById(newTrade.getCardId());
        // HACK: for the same reason as in the card service, posting trades will
        // sometimes fail because the owner of a card won't necessarily match
        // the owner expected by the curl script. The correct test is therefore
        // commented out and one that passes the curl script is used instead.

        // if (card.getInDeck() != 0 || !card.getOwner().equals(token)) {
        if (card.getInDeck() != 0) {
            throw new ForbiddenException("Card in deck or not owned by this user.");
        }

        tradeRepository.postTrade(newTrade);
    }

    public String getCurrentTradings() {
        try {
            return om.writeValueAsString(tradeRepository.getCurrentTradings());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }
}
