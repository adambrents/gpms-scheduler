package configuration;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConnectionConfig {
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
                    System.err.println("Config file not found: " + CONFIG_FILE_PATH);
                }
            } catch (IOException e) {
                System.err.println("Error reading config file: " + e.getMessage());
            }
        }
        return dbConnection;
    }

    private void setConnectionAuth(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
