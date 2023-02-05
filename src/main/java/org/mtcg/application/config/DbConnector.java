package org.mtcg.application.config;

import java.sql.Connection;

public interface DbConnector {
    Connection getConnection();
}
