package org.mtcg.application.util;

import java.sql.Connection;

public interface DbConnector {
    Connection getConnection();
}
