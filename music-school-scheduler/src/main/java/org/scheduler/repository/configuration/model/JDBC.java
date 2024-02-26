package org.scheduler.repository.configuration.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.scheduler.constants.Constants.CONNECTION_CONFIG;

public abstract class JDBC {
    private static final DbConnection DB_CONNECTION = CONNECTION_CONFIG.getDbConnection();
    private static final String JDBC_URL = String.format("jdbc:mysql://%s:%d/%s?connectionTimeZone=SERVER",
            DB_CONNECTION.getServerName(),
            DB_CONNECTION.getServerPort(),
            DB_CONNECTION.getDb());
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_CONNECTION.getUserName(), DB_CONNECTION.getPassword());
    }
}
