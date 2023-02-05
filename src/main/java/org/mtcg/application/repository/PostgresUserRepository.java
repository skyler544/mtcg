package org.mtcg.application.repository;

import org.mtcg.application.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mtcg.application.config.DataSource;

public class PostgresUserRepository implements UserRepository {

    private static final String SETUP_TABLE = """
                CREATE TABLE IF NOT EXISTS users(
                    id serial primary key,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL
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
    public void save(User user) {
        // TODO: implement me
    }

    @Override
    public User findUserByUsername(String username) {
        // TODO: implement me
        return null;
    }
}
