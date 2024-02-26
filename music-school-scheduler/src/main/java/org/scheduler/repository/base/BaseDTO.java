package org.scheduler.repository.base;

import org.scheduler.constants.Constants;
import org.scheduler.repository.configuration.model.JDBC;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDTO {
    public final String _database = Constants.CONNECTION_CONFIG.getDbConnection().getDb();

    public static Statement getStatement() throws SQLException {
        return JDBC.getConnection().createStatement();
    }    
}
