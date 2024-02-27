package org.scheduler.constants;

import org.scheduler.configuration.ConfigurationReaderHelper;
import org.scheduler.repository.configuration.model.ConnectionConfig;
import org.scheduler.utilities.navmanagement.NavigationHistory;

public class Constants {
    public static final String CONFIG_FILE_PATH = "config.json";
    public static final String CONFIGURATION_STRING = ConfigurationReaderHelper.getConfigAsString();
    public static final ConnectionConfig CONNECTION_CONFIG = new ConnectionConfig();
    public static final NavigationHistory NAVIGATION_HISTORY = new NavigationHistory();

    public class FXML_ROUTES {
        public static final String ADD_LESSON_SCRN = "/view/AddLessonsScreen.fxml";
        public static final String MOD_LESSON_SCRN = "/view/ModifyLessonsScreen.fxml";
        public static final String ALL_LESSON_SCRN = "/view/AllLessonsScreen.fxml";
        public static final String ADD_STUDENT_SCRN = "/view/AddStudentScreen.fxml";
        public static final String MOD_STUDENT_SCRN = "/view/ModifyStudentsScreen.fxml";
        public static final String LOGIN_SCREEN = "/view/LoginScreen.fxml";
        public static final String MAIN_SCREEN = "/view/MainScreen.fxml";
        public static final String REPORTS_SCRN = "/view/ReportsScreen.fxml";

    }
}
