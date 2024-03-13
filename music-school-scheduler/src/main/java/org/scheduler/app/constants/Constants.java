package org.scheduler.app.constants;

import javafx.stage.Stage;
import org.scheduler.app.configuration.ConfigurationReaderHelper;
import org.scheduler.data.configuration.ConnectionConfig;
import org.scheduler.app.utilities.navmanagement.NavigationHistory;

public class Constants {
    public static final String CONFIG_FILE_PATH = "config.json";
    public static final String CONFIGURATION_STRING = ConfigurationReaderHelper.getConfigAsString();
    public static final ConnectionConfig CONNECTION_CONFIG = new ConnectionConfig();
    public static final NavigationHistory NAVIGATION_HISTORY = new NavigationHistory();
    public static final String ERROR_RED = "f80202";
    public static Stage PRIMARY_STAGE;

    public enum CRUD{
        CREATE, READ, UPDATE, DELETE
    }

    public class PLACEHOLDER_TEXT{
        public static final String MENU_ITEM = "Choose an option";
        public static final String MENU_ITEM_BOOK = "Choose a book";
        public static final String MENU_ITEM_INSTRUMENT = "Choose an instrument";
        public static final String MENU_ITEM_LEVEL = "Choose a level";
    }

    public class FXML_ROUTES {
        public static final String ADD_LESSON_SCRN = "/view/AddLessonsScreen.fxml";
        public static final String MOD_LESSON_SCRN = "/view/ModifyLessonsScreen.fxml";
        public static final String ALL_LESSON_SCRN = "/view/AllLessonsScreen.fxml";
        public static final String STUDENT_MGMT_SCRN = "/view/StudentManagementScreen.fxml";
        public static final String LOGIN_SCREEN = "/view/LoginScreen.fxml";
        public static final String MAIN_SCREEN = "/view/MainScreen.fxml";
        public static final String REPORTS_SCRN = "/view/ReportsScreen.fxml";
        public static final String PROP_MGMT_SCRN = "/view/PropertyManagerScreen.fxml";
        public static final String TEACHER_MGMT_SCRN = "/view/TeacherManagementScreen.fxml";
        public static final String USER_MGMT_SCRN = "/view/UserManagementScreen.fxml";

    }
    public enum PROPERTIES{
        Book, Level, Teacher, Instrument
    }
    public enum MAPPINGS{
        StudentBook, StudentLevel, StudentTeacher, StudentInstrument, TeacherInstrument, StudentRecital
    }
}
