package org.scheduler.utilities;

import com.google.gson.Gson;

import static org.scheduler.constants.Constants.CONFIGURATION_STRING;


public class GsonHelper {
    private static final Gson gson = new Gson();
    public static <T> T loadConfig(Class<T> configClass) {
        if (CONFIGURATION_STRING != null){
            return gson.fromJson(CONFIGURATION_STRING, configClass);
        }
        else {
            return null;
        }
    }
}
