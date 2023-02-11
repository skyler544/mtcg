package org.mtcg.application.repository;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mtcg.application.config.DataSource;

public class PostgresUserRepository implements UserRepository {

    private static final String SETUP_TABLE = """
                CREATE TABLE IF NOT EXISTS users(
                    token TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL,
                    name TEXT,
                    bio TEXT,
                    image TEXT
                );
            """;

    public PostgresUserRepository() {
        try (PreparedStatement ps = DataSource.getInstance().getConnection().prepareStatement(SETUP_TABLE)) {
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to set up table.", e);
        }
    }

    @Override
    public void persist(Credentials credentials) throws IllegalStateException {
        final String ADD_USER = """
                INSERT INTO users (token, username, password) VALUES (?, ?, ?)
            """;
        final var user = new User(credentials);
        try (PreparedStatement ps = DataSource.getInstance().getConnection().prepareStatement(ADD_USER)) {
            ps.setString(1, user.getToken());
            ps.setString(2, user.getCredentials().getUsername());
            ps.setString(3, user.getCredentials().getPassword());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save user.", e);
        }
    }

    @Override
    public String findUserByUsername(String username) throws IllegalStateException {
        final String FIND_USER = """
                SELECT token, username FROM users WHERE username=?
                        """;

        try (PreparedStatement ps = DataSource.getInstance().getConnection().prepareStatement(FIND_USER)) {
            ps.setString(1, username);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                // return the token
                return rs.getString(1);
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query by username.", e);
        }
    }
}
