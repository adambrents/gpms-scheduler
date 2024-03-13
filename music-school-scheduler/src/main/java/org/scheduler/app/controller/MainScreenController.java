package org.scheduler.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.StudentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController extends ControllerBase implements IController {
    private static final Logger _logger = LoggerFactory.getLogger(MainScreenController.class);
    @FXML
    private TableColumn<StudentDTO, String> studentAddressColumn;
    @FXML
    private TableColumn<StudentDTO, String> studentNameColumn;
    @FXML
    private TableView<StudentDTO> studentTable;
    @FXML
    private Button exit;
    @FXML
    private TableColumn<StudentDTO, String> studentIdColumn;
    @FXML
    private TableView<LessonDTO> weekTable;
    private LessonDTO selectedLessonDTO = null;
    private StudentDTO selectedStudentDTO = null;
    private int userId;
    /**
     * when clicked, user is warned of deleting appointment, then appointment is deleted
     *
     * @param actionEvent
     */
    public void onCancelLesson(ActionEvent actionEvent) {
        selectedLessonDTO = weekTable.getSelectionModel().getSelectedItem();
        if (selectedLessonDTO == null) {
            return;
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText("Delete");
        alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLessonDTO.getId() + ", Type: " + selectedLessonDTO.getType() + "?");
        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                lessonsRepository.deleteItem(selectedLessonDTO, JDBC.getConnection());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            weekTable.setItems(lessonsRepository.getWeeklyLessons());
        }
        else {
            alert1.close();
        }
    }
    /**
     * sets userId
     */
    private void setUserId() {
        userId = _userId;
    }

    /**
     * init method, sets values for customer and appointment ui tables from db
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Get data for Weekly LessonsRepository table
//        idWeek.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
//        titleWeek.setCellValueFactory(new PropertyValueFactory<>("title"));
//        descriptionWeek.setCellValueFactory(new PropertyValueFactory<>("description"));
//        locationWeek.setCellValueFactory(new PropertyValueFactory<>("location"));
//        studentWeek.setCellValueFactory(new PropertyValueFactory<>("contactName"));
//        typeWeek.setCellValueFactory(new PropertyValueFactory<>("type"));
//        startWeek.setCellValueFactory(new PropertyValueFactory<>("startTime"));
//        endWeek.setCellValueFactory(new PropertyValueFactory<>("endTime"));
//        startDate.setCellValueFactory(new PropertyValueFactory<>("date"));
//        customerWeek.setCellValueFactory(new PropertyValueFactory<>("customerID"));
//        userWeek.setCellValueFactory(new PropertyValueFactory<>("userID"));

        //sets table with weekly appt data
        weekTable.setItems(lessonsRepository.getWeeklyLessons());
        this.setUserId();
    }

    @Override
    public List<PossibleError> buildPossibleErrors() {
        return null;
    }

    @Override
    public String getNameForItem(Object item) {
        return "";
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void onAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Music Scheduler");
        alert.setContentText("Version 1.0\nDeveloped by Adam Brents.\nFor support, contact me at adam.brents1@gmail.com");
        alert.showAndWait();
    }
    public void onReports(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.REPORTS_SCRN, userId);
    }
    public void onAllLessons(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.ALL_LESSON_SCRN, userId);
    }
    public void onPropertyManagement(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.PROP_MGMT_SCRN, userId);
    }
    public void onLessonManagement(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.ADD_LESSON_SCRN, userId);
    }
    public void onStudentManagement(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.STUDENT_MGMT_SCRN, userId);
    }
    public void onUserManagement(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.USER_MGMT_SCRN, userId);
    }
    public void onTeacherManagement(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.TEACHER_MGMT_SCRN, userId);
    }
    public void onGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    @Override
    public void onSubmit(ActionEvent actionEvent) throws IOException, SQLException {

    }
}
