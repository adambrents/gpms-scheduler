package configuration;

import com.google.gson.Gson;
import controller.LoginScreenController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConnectionConfig {
    private static final Logger _logger = LoggerFactory.getLogger(LoginScreenController.class);
    private static final String CONFIG_FILE_PATH = "configuration/config.json";
    private DbConnection dbConnection;

    public synchronized DbConnection getConnectionAuth() {
        String var = "Current working directory: " + System.getProperty("user.dir");
        if (dbConnection == null) {
            Gson gson = new Gson();
            try (InputStream inputStream = ConnectionConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
                if (inputStream != null) {
                    try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                        ConnectionConfig config = gson.fromJson(reader, ConnectionConfig.class);
                        setConnectionAuth(config.dbConnection);
                    }
                } else {
                    _logger.error("Config file not found: " + CONFIG_FILE_PATH);
                }
            } catch (IOException e) {
                _logger.error("Error reading config file: " + e.getMessage());
            }
        }
        return dbConnection;
    }

    private void setConnectionAuth(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
