package org.scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.scheduler.constants.Constants;
import org.scheduler.controller.base.ControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.repository.LessonsRepository;
import org.scheduler.repository.UsersRepository;
import org.scheduler.dto.LessonDTO;
import org.scheduler.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final LessonsRepository lessonsRepository = new LessonsRepository();
    private final UsersRepository usersRepository = new UsersRepository();

    /**
     * Validates if user exists and if information was entered correctly. Loads main screen
     * Displays a notification of any upcoming appointments within 15 minutes
     * writes a log to the login attempts log
     * Lambda expression to handle user response if error with login. Reloads login page on OK
     * Justification: This is so that a user can retry their last action if it is an external exception
     * @param actionEvent
     */
    public void onSubmit(ActionEvent actionEvent) {
        //gets resource bundles for use further down
        Locale locale = Locale.getDefault();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ResourceBundle", locale);

        _logger.debug("Class Path: " + System.getProperty("java.class.path"));
        //records login activity/attempts

        String usrnm = usernameField.getText();
        String pssword = passwordField.getText();
        UserDTO userDTO = new UserDTO(usrnm, 0, pssword);

        int valid = usersRepository.isLoginMatchUser(userDTO);
        if (valid == 0) {
            try{
                _logger.debug("Current working directory" + System.getProperty("userDTO.dir"));
                _logger.info("Successful Login UserName: " + usernameField.getText());
                super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.MAIN_SCREEN, userDTO.userId());

                LessonDTO appt15Min = lessonsRepository.getLessonsNext15Minutes(LocalDateTime.now());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if(appt15Min != null){
                    alert.setContentText("You have the following lesson(s) in the next 15 minutes: \n\nLessonDTO ID: " + appt15Min.getLessonID()
                            + "\nDate: " + appt15Min.getStart().toLocalDate() + "\nTime: " + appt15Min.getStart().toLocalTime());
                }
                else {
                    alert.setContentText("You have no lessons within 15 minutes");
                }
                alert.show();
            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Error: See console for more details");
                e.printStackTrace();
                //Lambda expression to handle userDTO response. Reloads login page on OK
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        _logger.info("Alerting!");
                        Parent main = null;
                        try {
                            main = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
                            Scene scene = new Scene(main);
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }));
            }

        }
        else if (valid == 1) {
            passwordError.setText(resourceBundle.getString("PasswordError"));
            usernameError.setText("");
            _logger.error("Unsuccessful Login UserName: " + usernameField.getText() + " Invalid password");
        }
        else if (valid == 2){
            _logger.error("Unsuccessful Login UserName: " + usernameField.getText());
            usernameError.setText(resourceBundle.getString("UsernameError"));
            passwordError.setText("");
        }
        else if (valid == 3){
            _logger.error("Unsuccessful Login UserName: " + usernameField.getText() + " Invalid username and password");
            usernameError.setText(resourceBundle.getString("UsernamePwError"));
            passwordError.setText("");
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


        ZoneId zone = ZoneId.systemDefault();
        zoneID.setText(zone.toString());
    }

    /**
     * exits the application
     */
    @Override
    public void onCancel(ActionEvent event) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
