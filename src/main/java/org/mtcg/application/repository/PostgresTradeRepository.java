package org.mtcg.application.repository;

import org.mtcg.application.model.Trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mtcg.application.util.DataSource;

public class PostgresTradeRepository implements TradeRepository {
    private static final Connection connection = DataSource.getInstance().getConnection();

    private static final String SETUP_TABLE = """
                CREATE TABLE IF NOT EXISTS trades(
                    id TEXT PRIMARY KEY,
                    card_id TEXT NOT NULL,
                    type TEXT NOT NULL,
                    damage INTEGER NOT NULL,

                    CONSTRAINT fk_card_id FOREIGN KEY(card_id)
                    REFERENCES cards(id)
                );
            """;

    public PostgresTradeRepository() {
        try (PreparedStatement ps = connection.prepareStatement(SETUP_TABLE)) {
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to set up table.", e);
        }
    }

    @Override
    public List<String> getCurrentTradings() {
        final String GET_TRADES = """
                SELECT id FROM trades
                """;

        try (PreparedStatement ps = connection.prepareStatement(GET_TRADES)) {
            ps.execute();

            ResultSet rs = ps.getResultSet();
            List<String> trades = new ArrayList<>();

            while (rs.next()) {
                trades.add(rs.getString(1));
            }
            return trades;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve trade.", e);
        }
    }

    @Override
    public Trade getTradeById(String id) {
        final String GET_TRADE_BY_ID = """
                SELECT id, card_id, type, damage FROM trades where id=?
                """;

        try (PreparedStatement ps = connection.prepareStatement(GET_TRADE_BY_ID)) {
            ps.setString(1, id);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            if (rs.next()) {
                return new Trade(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve trade.", e);
        }
    }

    @Override
    public void postTrade(Trade trade) {
        final String ADD_TRADE = """
                INSERT INTO trades (id, card_id, type, damage) VALUES (?, ?, ?, ?)
                                """;
        try (PreparedStatement ps = connection.prepareStatement(ADD_TRADE)) {
            ps.setString(1, trade.getId());
            ps.setString(2, trade.getCardId());
            ps.setString(3, trade.getType());
            ps.setInt(4, trade.getDamage());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save trade.", e);
        }
    }
}
