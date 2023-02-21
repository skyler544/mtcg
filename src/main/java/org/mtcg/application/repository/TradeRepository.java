package org.mtcg.application.repository;

import java.util.List;

import org.mtcg.application.model.Trade;

public interface TradeRepository {
    List<String> getCurrentTradings();
    Trade getTradeById(String id);

    void postTrade(Trade trade);
}
