package org.scheduler.app.configuration;

import org.scheduler.app.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConfigurationReaderHelper {
    private static final Logger _logger = LoggerFactory.getLogger(ConfigurationReaderHelper.class);
    public static String getConfigAsString() {
        StringBuilder contentBuilder = new StringBuilder();
        _logger.info("Reading config file!");
        try (InputStream inputStream = ConfigurationReaderHelper.class
                .getClassLoader()
                .getResourceAsStream(Constants.CONFIG_FILE_PATH)) {
            if (inputStream == null) {
                URL resourceUrl = ConfigurationReaderHelper.class.getClassLoader().getResource(Constants.CONFIG_FILE_PATH);
                throw new IOException("Config file not found: " + Constants.CONFIG_FILE_PATH + "; URL attempted: " + resourceUrl);
            }
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null; // or consider rethrowing the exception or handling it as per your error handling policy
        }
        return contentBuilder.toString();
    }
}
