package org.scheduler.repository.configuration.context;

import org.scheduler.repository.configuration.model.JDBC;

import java.sql.SQLException;
import java.sql.Statement;

public class JDBCContext {
    /**
     * gets statement to run queries in db
     * @return
     */
    public static Statement getStatement(){
        try {
            return JDBC.getConnection().createStatement();

        }catch (SQLException e){
            return null;
        }
    }
}
