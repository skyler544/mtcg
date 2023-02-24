package org.mtcg.application.service;

import org.mtcg.application.model.Card;
import org.mtcg.application.model.Trade;
import org.mtcg.application.repository.TradeRepository;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.exception.MtcgException;
import org.mtcg.http.exception.MtcgException;
import org.mtcg.http.exception.MtcgException;
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
            throw new MtcgException("Card in deck or not owned by this user.", HttpStatus.FORBIDDEN);
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
            throw new MtcgException("A trade with this ID already exists.", HttpStatus.CONFLICT);
        }
        checkCardInDeckAndUserOwnsCard(offer, token);

        tradeRepository.postTrade(offer);
    }

    public String getCurrentTradings() {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(tradeRepository.getCurrentTradings());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public Trade doesTradeExist(String tradeId) {
        Trade trade = findTradeById(tradeId);
        if (trade == null) {
            throw new MtcgException("The provided deal ID was not found.", HttpStatus.NOT_FOUND);
        }
        return trade;
    }

    public void trade(String tradeId, String offeredCardId, String token) {
        Trade trade = doesTradeExist(tradeId);

        Card offeredCard = cardRepository.findCardById(offeredCardId);
        Card cardFromTrade = cardRepository.findCardById(trade.getCardId());

        if (offeredCard == null || cardFromTrade == null) {
            throw new MtcgException("Card does not exist.", HttpStatus.NOT_FOUND);
        }

        String offeredCardOwner = offeredCard.getOwner();
        String cardFromTradeOwner = cardFromTrade.getOwner();

        String type = offeredCard.getName().toLowerCase().contains("spell") ? "spell" : "monster";

        if (!offeredCardOwner.equals(token)) {
            throw new MtcgException("You may not trade cards you do not own.", HttpStatus.FORBIDDEN);
        }
        if (offeredCardOwner.equals(cardFromTradeOwner)) {
            throw new MtcgException("You may not trade with yourself.", HttpStatus.FORBIDDEN);
        }
        if (!type.equals(trade.getType())) {
            throw new MtcgException("The card offered is not the required type.", HttpStatus.FORBIDDEN);
        }
        if (offeredCard.getDamage() < trade.getDamage()) {
            throw new MtcgException("The card offered is not strong enough.", HttpStatus.FORBIDDEN);
        }
        if (offeredCard.getInDeck() + cardFromTrade.getInDeck() != 0) {
            throw new MtcgException("You may not trade cards which are currently in the deck.", HttpStatus.FORBIDDEN);
        }

        // if (!offeredCardOwner.equals(token)
        //         || offeredCardOwner.equals(cardFromTradeOwner)
        //         || !type.equals(trade.getType())
        //         || offeredCard.getDamage() < trade.getDamage()
        //         || offeredCard.getInDeck() + cardFromTrade.getInDeck() != 0) {
        //     throw new MtcgException("Trade conditions not met.", HttpStatus.FORBIDDEN);
        // }

        cardRepository.saveCardOwner(offeredCard.getId(), cardFromTradeOwner);
        cardRepository.saveCardOwner(cardFromTrade.getId(), offeredCardOwner);

        tradeRepository.deleteTrade(tradeId);
    }

    public void deleteTrade(String tradeId, String token) {
        Trade trade = doesTradeExist(tradeId);

        if (!cardRepository.findCardById(trade.getCardId()).getOwner().equals(token)) {
            throw new MtcgException("You may not delete trades you do not own.", HttpStatus.FORBIDDEN);
        }

        tradeRepository.deleteTrade(tradeId);
    }
}
