package org.mtcg.application.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource implements DbConnector {

    private final HikariDataSource ds;

    private DataSource() {
        HikariConfig config = new HikariConfig();

        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("serverName", "localhost");
        config.addDataSourceProperty("portNumber", "5432");
        config.addDataSourceProperty("databaseName", "mtcg-db");
        config.addDataSourceProperty("user", "mtcg-user");
        config.addDataSourceProperty("password", "mtcg-pass");

        ds = new HikariDataSource(config);
    }

    private static DataSource dataSource;

    // Always use a static DataSource; this ensures that we don't end up in some
    // kind of odd undefined situation where there is more than one active data
    // source.
    public static DataSource getInstance() {
        if (dataSource == null) {
            dataSource = new DataSource();
        }
        return dataSource;
    }

    // Note that java.sql.Connection extends AutoCloseable; this should be used
    // via the try-with-resources pattern.
    public Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("Database not available!", e);
        }
    }

}
