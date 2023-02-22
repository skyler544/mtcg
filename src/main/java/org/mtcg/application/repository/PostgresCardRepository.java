package org.mtcg.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mtcg.application.model.Card;
import org.mtcg.application.util.DataSource;

public class PostgresCardRepository implements CardRepository {
    private static final Connection connection = DataSource.getInstance().getConnection();

    private static final String SETUP_TABLE = """
            CREATE TABLE IF NOT EXISTS cards(
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                damage INTEGER NOT NULL,
                owner TEXT,
                package_id TEXT NOT NULL,
                in_deck int NOT NULL,

                CONSTRAINT fk_user_token FOREIGN KEY(owner)
                REFERENCES users(token));
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
                INSERT INTO cards (id, name, damage, package_id, in_deck)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(ADD_CARD)) {
            ps.setString(1, card.getId());
            ps.setString(2, card.getName());
            ps.setInt(3, card.getDamage());
            ps.setString(4, card.getPackageId());
            ps.setInt(5, card.getInDeck());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save card.", e);
        }
    }

    @Override
    public Card findCardById(String id) throws IllegalStateException {
        final String FIND_CARDS_BY_ID = """
                SELECT id, name, damage, owner, package_id, in_deck
                FROM cards WHERE id=?
                """;

        try (PreparedStatement ps = connection.prepareStatement(FIND_CARDS_BY_ID)) {
            ps.setString(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                return new Card(rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6));
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
                SELECT id, name, damage, owner, package_id, in_deck
                FROM cards WHERE owner=?
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
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6)));
            }
            return stack;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query cards by owner.", e);
        }
    }

    @Override
    public List<Card> retrievePackage(String packageId) throws IllegalStateException {
        final String FIND_CARDS_BY_PACKAGE_ID = """
                SELECT id, name, damage, owner, package_id, in_deck
                FROM cards WHERE package_id=?
                """;

        try (PreparedStatement ps = connection.prepareStatement(FIND_CARDS_BY_PACKAGE_ID)) {
            ps.setString(1, packageId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<Card> pkg = new ArrayList<>();
            while (rs.next()) {
                pkg.add(new Card(rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(3)));
            }
            return pkg;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query for packages.", e);
        }
    }

    @Override
    public void saveCardOwner(String id, String token) throws IllegalStateException {
        final String SET_CARD_USER = """
                UPDATE cards SET owner=?
                WHERE id=?
                """;

        try (PreparedStatement ps = connection.prepareStatement(SET_CARD_USER)) {
            ps.setString(1, token);
            ps.setString(2, id);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to set card owner.", e);
        }
    }

    @Override
    public List<String> getAvailablePackages() throws IllegalStateException {
        final String FIND_AVAILABLE_PACKAGES = """
                SELECT package_id
                FROM cards WHERE owner IS NULL
                """;

        try (PreparedStatement ps = connection.prepareStatement(FIND_AVAILABLE_PACKAGES)) {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<String> packages = new ArrayList<>();
            while (rs.next()) {
                packages.add(rs.getString(1));
            }
            for (var pkg : packages) {
                System.out.println(pkg);
            }
            return packages;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query packages.", e);
        }
    }

    @Override
    public void setPackageOwner(String packageId, String token) throws IllegalStateException {
        List<Card> pkg = retrievePackage(packageId);
        for (var card : pkg) {
            saveCardOwner(card.getId(), token);
        }
    }

    @Override
    public void addCardToDeck(String id) throws IllegalStateException {
        final String SET_CARD_IN_DECK = """
                UPDATE cards SET in_deck=?
                WHERE id=?
                """;

        try (PreparedStatement ps = connection.prepareStatement(SET_CARD_IN_DECK)) {
            ps.setInt(1, 1);
            ps.setString(2, id);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to add card to deck.", e);
        }
    }

    @Override
    public List<Card> getUserDeck(String token) throws IllegalStateException {
        final String GET_CARDS_IN_DECK = """
                SELECT id, name, damage, owner, package_id, in_deck
                FROM cards WHERE in_deck=1 AND owner=?
                    """;

        try (PreparedStatement ps = connection.prepareStatement(GET_CARDS_IN_DECK)) {
            ps.setString(1, token);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<Card> deck = new ArrayList<>();
            while (rs.next()) {
                deck.add(new Card(rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6)));
            }
            return deck;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve user deck.", e);
        }
    }

    @Override
    public void clearUserDeck(String token) throws IllegalStateException {
        final String CLEAR_DECK = """
                UPDATE cards SET in_deck=0
                WHERE owner=?
                    """;

        try (PreparedStatement ps = connection.prepareStatement(CLEAR_DECK)) {
            ps.setString(1, token);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve user deck.", e);
        }
    }
}
