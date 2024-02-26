package org.scheduler.repository.configuration.model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.scheduler.utilities.GsonHelper.loadConfig;

public class ConnectionConfig {
    private static final Logger _logger = LoggerFactory.getLogger(ConnectionConfig.class);
    private DbConnection dbConnection;

    public synchronized DbConnection getDbConnection() {
        if (dbConnection == null) {
            ConnectionConfig config = loadConfig(ConnectionConfig.class);
            setConnectionAuth(config.dbConnection);
        }
        return dbConnection;
    }

    private void setConnectionAuth(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
