package org.mtcg.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mtcg.application.model.Battle;
import org.mtcg.application.model.BattleRound;
import org.mtcg.application.util.DataSource;

public class PostgresBattleRepository implements BattleRepository {
    private static final Connection connection = DataSource.getInstance().getConnection();

    private static final String SETUP_TABLES = """
            CREATE TABLE IF NOT EXISTS battles(
                id SERIAL PRIMARY KEY,
                player_one TEXT NOT NULL,
                player_two TEXT NOT NULL,
                result TEXT NOT NULL);

            CREATE TABLE IF NOT EXISTS rounds(
                round_id SERIAL PRIMARY KEY,
                battle_id INTEGER NOT NULL,
                card_one_info TEXT NOT NULL,
                card_two_info TEXT NOT NULL,
                result TEXT NOT NULL,

                CONSTRAINT fk_battle_id FOREIGN KEY(battle_id)
                REFERENCES battles(id));
            """;

    public PostgresBattleRepository() {
        try (PreparedStatement ps = connection.prepareStatement(SETUP_TABLES)) {
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to set up tables.", e);
        }
    }

    @Override
    public int saveBattle(Battle battle) {
        final String ADD_BATTLE = """
                INSERT INTO battles (id, player_one, player_two, result)
                VALUES (DEFAULT, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(ADD_BATTLE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, battle.getPlayerOne());
            ps.setString(2, battle.getPlayerTwo());
            ps.setString(3, battle.getResult());
            ps.execute();
            ResultSet generatedKey = ps.getGeneratedKeys();
            if (generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save battle.", e);
        }
    }

    @Override
    public Battle readBattleById(int id) {
        final String GET_BATTLE = """
                SELECT id, player_one, player_two, result
                FROM battles WHERE id=?
                """;
        try (PreparedStatement ps = connection.prepareStatement(GET_BATTLE)) {
            ps.setInt(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                return new Battle(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to read battle info.", e);
        }
    }

    @Override
    public List<Battle> getBattlesByParticipant(String name) {
        final String GET_BATTLES = """
                SELECT id, player_one, player_two, result
                FROM battles WHERE player_one=? OR player_two=?
                """;
        try (PreparedStatement ps = connection.prepareStatement(GET_BATTLES)) {
            ps.setString(1, name);
            ps.setString(2, name);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<Battle> battles = new ArrayList<>();
            if (rs.next()) {
                battles.add(new Battle(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)));
            }
            return battles;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to read battle info.", e);
        }
    }

    @Override
    public void updateBattleResult(String result, int id) {
        final String UPDATE_BATTLE = """
                UPDATE battles set result=?
                WHERE id=?
                """;
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_BATTLE)) {
            ps.setString(1, result);
            ps.setInt(2, id);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to read battle info.", e);
        }
    }

    @Override
    public void saveBattleRound(BattleRound round) {
        final String SAVE_BATTLE_LOG = """
                INSERT INTO rounds (battle_id, card_one_info, card_two_info, result)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(SAVE_BATTLE_LOG)) {
            ps.setInt(1, round.getBattleId());
            ps.setString(2, round.getPlayerOneCardInfo());
            ps.setString(3, round.getPlayerTwoCardInfo());
            ps.setString(4, round.getResult());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save round.", e);
        }
    }

    @Override
    public List<BattleRound> readBattleLog(int id) {
        final String GET_BATTLE_LOG = """
                SELECT round_id, battle_id, card_one_info, card_two_info, result
                FROM rounds WHERE battle_id=?
                """;

        try (PreparedStatement ps = connection.prepareStatement(GET_BATTLE_LOG)) {
            ps.setInt(1, id);
            ps.execute();

            List<BattleRound> rounds = new ArrayList<>();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                rounds.add(new BattleRound(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)));
            }
            return rounds;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to read battle log.", e);
        }
    }
}
