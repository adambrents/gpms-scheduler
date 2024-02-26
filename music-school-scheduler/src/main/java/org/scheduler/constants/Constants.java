package org.scheduler.constants;

import org.scheduler.configuration.ConfigurationReaderHelper;
import org.scheduler.repository.configuration.model.ConnectionConfig;
import org.scheduler.utilities.navmanagement.NavigationHistory;

public class Constants {
    public static final String CONFIG_FILE_PATH = "config.json";
    public static final String CONFIGURATION_STRING = ConfigurationReaderHelper.getConfigAsString();
    public static final ConnectionConfig CONNECTION_CONFIG = new ConnectionConfig();
    public static final NavigationHistory NAVIGATION_HISTORY = new NavigationHistory();

    public class DbTables{
        public static final String STUDENTS = "students";
        public static final String USERS = "users";
        public static final String TEACHERS = "teachers";
        public static final String LESSONS = "lessons";
    }
}
