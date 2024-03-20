package org.scheduler.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.repository.UsersRepository;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController extends ControllerBase implements IController {

    private static final Logger _logger = LoggerFactory.getLogger(LoginScreenController.class);
    public Label zoneID;
    public Label loginForm;
    public Button submit;
    public Label username;
    public Label password;
    public Label usernameError;
    public Label passwordError;
    public TextField usernameField;
    public TextField passwordField;
    public Button exit;
    private final Locale locale = Locale.getDefault();
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("ResourceBundle", locale);
    private UserDTO userDTO;

    /**
     * Validates if user exists and if information was entered correctly. Loads main screen
     * Displays a notification of any upcoming appointments within 15 minutes
     * writes a log to the login attempts log
     * Lambda expression to handle user response if error with login. Reloads login page on OK
     * Justification: This is so that a user can retry their last action if it is an external exception
     * @param actionEvent
     */
    public void onSubmit(ActionEvent actionEvent) {
        _logger.debug("Class Path: " + System.getProperty("java.class.path"));

        userDTO = new UserDTO(usernameField.getText(), 0, passwordField.getText(), false);

        UsersRepository.LoginResult valid = usersRepository.isLoginMatchUser(userDTO);

        handleLoginResult(valid, actionEvent);
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void handleLoginResult(UsersRepository.LoginResult valid, ActionEvent actionEvent) {
        switch (valid) {
            case SUCCESS:
                handleSuccessfulLogin(actionEvent);
                break;
            case INACTIVE:
                handleLoginError("InactiveUserError", false, false);
                break;
            case WRONG_PASSWORD:
                handleLoginError("PasswordError", true, false);
                break;
            case WRONG_USERNAME:
                handleLoginError("UsernameError", false, true);
                break;
            case WRONG_BOTH:
                handleLoginError("UsernamePwError", true, true);
                break;
            case DEFAULT:
            default:
                throw new RuntimeException("Unknown error occurred, validity was never assigned!");
        }
    }

    private void handleSuccessfulLogin(ActionEvent actionEvent) {
        logSuccess();
        switchToMainScreen(actionEvent);
//        checkForUpcomingLessons();
    }

    private void logSuccess() {
        _logger.info("Successful Login UserName: " + usernameField.getText());
    }

    private void switchToMainScreen(ActionEvent actionEvent) {
        try {
            super.loadNewScreen(Constants.FXML_ROUTES.MAIN_SCREEN, userDTO.getId());
        } catch (Exception e) {
            showErrorDialog("Error switching to main screen. Please try again.", actionEvent);
            _logger.error("Error switching to main screen", e);
        }
    }

//    private void checkForUpcomingLessons() {
//        try {
//            LessonDTO upcomingLesson = lessonsRepository.getLessonsNext15Minutes(LocalDateTime.now());
//            showLessonAlert(upcomingLesson);
//        } catch (Exception e) {
//            _logger.error("Error fetching upcoming lessons", e);
//        }
//    }

    private void showLessonAlert(LessonDTO lesson) {
        Alert alert = lesson != null ? createLessonAlert(lesson) : createNoLessonAlert();
        alert.show();
    }

    private Alert createLessonAlert(LessonDTO lesson) {
        return new Alert(Alert.AlertType.INFORMATION, "You have the following lesson(s) in the next 15 minutes: \n\nLessonDTO ID: " + lesson.getId()
                + "\nDate: " /*+ lesson.getStart().toLocalDate()*/ + "\nTime: " /*+ lesson.getStart().toLocalTime()*/);
    }

    private Alert createNoLessonAlert() {
        return new Alert(Alert.AlertType.INFORMATION, "You have no lessons within 15 minutes");
    }

    private void handleLoginError(String resourceKey, boolean passwordErrorVisible, boolean usernameErrorVisible) {
        if (passwordErrorVisible) {
            passwordError.setText(resourceBundle.getString(resourceKey));
        } else {
            passwordError.setText("");
        }

        if (usernameErrorVisible) {
            usernameError.setText(resourceBundle.getString(resourceKey));
        } else {
            usernameError.setText("");
        }

        logError(resourceKey);
    }

    private void logError(String errorType) {
        _logger.error("Unsuccessful Login UserName: " + usernameField.getText() + " " + resourceBundle.getString(errorType));
    }

    private void showErrorDialog(String message, ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> reloadLoginPage(actionEvent));
    }

    private void reloadLoginPage(ActionEvent actionEvent) {
        try {
            Parent main = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
            Scene scene = new Scene(main);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            _logger.error("Error reloading the login page", ex);
        }
    }

    /**
     * Sets fields to user's system specific details
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("ResourceBundle", locale);
        username.setText(resourceBundle.getString("Username"));
        loginForm.setText(resourceBundle.getString("Login"));
        password.setText(resourceBundle.getString("Password"));
        submit.setText(resourceBundle.getString("Submit"));

        submit.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((observable, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        setupKeyListener(newScene);
                    }
                });
            }
        });

        ZoneId zone = ZoneId.systemDefault();
        zoneID.setText(zone.toString());
    }
    private void setupKeyListener(Scene scene) {
        Stage primaryStage = (Stage) scene.getWindow();

        primaryStage.getScene().setOnKeyPressed(this::handleKeyPress);
    }
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            _logger.info("Enter key pressed!");
            onSubmit(new ActionEvent());
        }
    }

    /**
     * exits the application
     */
    @Override
    public void onGoBack(ActionEvent event) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    @Override
    public List<PossibleError> buildPossibleErrors() {
        return null;
    }

    @Override
    public void setUserId(int userId) {

    }
}
