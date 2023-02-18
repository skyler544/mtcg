package org.mtcg.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mtcg.application.model.Card;
import org.mtcg.application.config.DataSource;

public class PostgresCardRepository implements CardRepository {
    private static final Connection connection = DataSource.getInstance().getConnection();

    private static final String SETUP_TABLE = """
                CREATE TABLE IF NOT EXISTS cards(
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    damage INTEGER NOT NULL,
                    owner TEXT
                );
            """;

    public PostgresCardRepository() {
        try (PreparedStatement ps = connection.prepareStatement(SETUP_TABLE)) {
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to set up table.", e);
        }
    }

    @Override
    public void saveCard(Card card) throws IllegalStateException {
        final String ADD_CARD = """
                INSERT INTO cards (id, name, damage) VALUES (?, ?, ?)
                        """;
        try (PreparedStatement ps = connection.prepareStatement(ADD_CARD)) {
            ps.setString(1, card.getId());
            ps.setString(2, card.getName());
            ps.setInt(3, card.getDamage());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save card.", e);
        }
    }

    @Override
    public Card findCardById(String id) throws IllegalStateException {
        final String FIND_CARDS_BY_ID = """
                SELECT id, name, damage, owner FROM cards WHERE id=?
                            """;

        try (PreparedStatement ps = connection.prepareStatement(FIND_CARDS_BY_ID)) {
            ps.setString(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                return new Card(rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query cards by id.", e);
        }
    }

    @Override
    public List<Card> findCardsByOwner(String token) throws IllegalStateException {
        final String FIND_CARDS_BY_OWNER = """
                SELECT id, name, damage, owner FROM cards WHERE owner=?
                            """;

        try (PreparedStatement ps = connection.prepareStatement(FIND_CARDS_BY_OWNER)) {
            ps.setString(1, token);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<Card> stack = new ArrayList<>();
            while (rs.next()) {
                stack.add(new Card(rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4)));
            }
            return stack;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query cards by owner.", e);
        }
    }

    @Override
    public void saveCardOwner(String id, String token) throws IllegalStateException {
        final String SET_CARD_USER = """
                UPDATE cards SET owner=? WHERE id=?
                """;
        try (PreparedStatement ps = connection.prepareStatement(SET_CARD_USER)) {
            ps.setString(1, token);
            ps.setString(2, id);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update user coins.", e);
        }
    }
}
