package org.mtcg.application.repository;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.User;
import org.mtcg.application.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mtcg.application.util.DataSource;

public class PostgresUserRepository implements UserRepository {
    private static final Connection connection = DataSource.getInstance().getConnection();

    private static final String SETUP_TABLE = """
                CREATE TABLE IF NOT EXISTS users(
                    token TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL,
                    name TEXT,
                    bio TEXT,
                    image TEXT,
                    coins INTEGER
                );
            """;

    public PostgresUserRepository() {
        try (PreparedStatement ps = connection.prepareStatement(SETUP_TABLE)) {
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to set up table.", e);
        }
    }

    @Override
    public void saveCredentials(Credentials credentials) throws IllegalStateException {
        final String ADD_USER = """
                INSERT INTO users (token, username, password, coins) VALUES (?, ?, ?, ?)
                    """;
        final var user = new User(credentials);
        try (PreparedStatement ps = connection.prepareStatement(ADD_USER)) {
            ps.setString(1, user.getToken());
            ps.setString(2, user.getCredentials().getUsername());
            ps.setString(3, user.getCredentials().getPassword());
            ps.setInt(4, user.getCoins());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save user.", e);
        }
    }

    @Override
    public User findUserByUsername(String username) throws IllegalStateException {
        final String FIND_USER = """
                SELECT username, password FROM users WHERE username=?
                            """;

        try (PreparedStatement ps = connection.prepareStatement(FIND_USER)) {
            ps.setString(1, username);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                return new User(new Credentials(rs.getString(1), rs.getString(2)));
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query by username.", e);
        }
    }

    @Override
    public void saveUserProfile(String token, UserProfile userProfile) throws IllegalStateException {
        final String SET_USER_PROFILE = """
                UPDATE users SET name=?, bio=?, image=? WHERE token=?
                """;
        try (PreparedStatement ps = connection.prepareStatement(SET_USER_PROFILE)) {
            ps.setString(1, userProfile.getName());
            ps.setString(2, userProfile.getBio());
            ps.setString(3, userProfile.getImage());
            ps.setString(4, token);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update user profile.", e);
        }
    }

    @Override
    public UserProfile findUserProfile(String token) throws IllegalStateException {
        final String GET_USER_PROFILE = """
                SELECT name, bio, image FROM users WHERE token=?
                """;
        try (PreparedStatement ps = connection.prepareStatement(GET_USER_PROFILE)) {
            ps.setString(1, token);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                // return the token
                return new UserProfile(rs.getString(1), rs.getString(2), rs.getString(3));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve user profile.", e);
        }
    }

    @Override
    public void saveUserCoins(String token, int coins) throws IllegalStateException {
        final String SET_USER_COINS = """
                UPDATE users SET coins=? WHERE token=?
                """;
        try (PreparedStatement ps = connection.prepareStatement(SET_USER_COINS)) {
            ps.setInt(1, coins);
            ps.setString(2, token);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update user coins.", e);
        }
    }

    @Override
    public int getUserCoins(String token) throws IllegalStateException {
        final String GET_USER_COINS = """
                SELECT coins FROM users WHERE token=?
                """;
        try (PreparedStatement ps = connection.prepareStatement(GET_USER_COINS)) {
            ps.setString(1, token);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve user coins.", e);
        }
    }
}
