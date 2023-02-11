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
                    id serial primary key,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL,
                    token TEXT NOT NULL
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
                INSERT INTO users (username, password, token) VALUES (?, ?, ?)
                        """;
        final var user = new User(credentials);
        try (PreparedStatement ps = DataSource.getInstance().getConnection().prepareStatement(ADD_USER)) {
            ps.setString(1, user.getCredentials().getUsername());
            ps.setString(2, user.getCredentials().getPassword());
            ps.setString(3, user.getToken());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save user.", e);
        }
    }

    @Override
    public User findUserByUsername(String username) throws IllegalStateException {
        // TODO: is it necessary to get the password here?
        // seems like a bad design to force having a password when using this method
        // only for checking if the user already exists.
        final String FIND_USER = """
                SELECT username, password token FROM users WHERE username=?
                        """;

        try (PreparedStatement ps = DataSource.getInstance().getConnection().prepareStatement(FIND_USER)) {
            ps.setString(1, username);

            if (ps.execute()) {
                ResultSet rs = ps.getResultSet();
                rs.next();
                var credentials = new Credentials(rs.getString(2), rs.getString(3));
                return new User(credentials);
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query by username.", e);
        }
    }
}
