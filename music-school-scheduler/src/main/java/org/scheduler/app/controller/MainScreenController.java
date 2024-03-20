package org.scheduler.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.controller.interfaces.ITableView;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.StudentDTO;
import org.scheduler.data.dto.properties.BookDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController extends ControllerBase implements IController, ITableView<LessonDTO>{
    private static final Logger _logger = LoggerFactory.getLogger(MainScreenController.class);
    private StudentDTO selectedStudentDTO;
    @FXML
    private TableColumn<LessonDTO, String> bookColumn;
    @FXML
    private TableColumn<LessonDTO, String> goldCupColumn;
    @FXML
    private TableColumn<LessonDTO, String> instrumentColumn;
    @FXML
    private TableColumn<LessonDTO, String> lessonTimeColumn;
    @FXML
    private TableColumn<LessonDTO, String> levelColumn;
    @FXML
    private TableColumn<LessonDTO, String> studentColumn;
    @FXML
    private TableColumn<LessonDTO, String> teacherColumn;
    @FXML
    private TableColumn<LessonDTO, String> isNewStudentColumn;
    @FXML
    private TableView<LessonDTO> weekTable;

    /**
     * when clicked, user is warned of deleting appointment, then appointment is deleted
     *
     * @param actionEvent
     */
    public void onCancelLesson(ActionEvent actionEvent) {
        LessonDTO selectedLessonDTO = weekTable.getSelectionModel().getSelectedItem();
        if (selectedLessonDTO == null) {
            return;
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText("Delete");
        alert1.setContentText("Are you sure you want to remove lesson with " + selectedLessonDTO.getStudent().getName() + " on " + selectedLessonDTO.getLessonTimeFormatted() + "?");
        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                lessonsRepository.deleteItem(selectedLessonDTO, JDBC.getConnection());
                reset();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            alert1.close();
        }
    }
    /**
     * sets userId
     */
    private void setUserId() {
        userId = this.userId;
    }

    /**
     * init method, sets values for customer and appointment ui tables from db
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reset();
    }

    @Override
    public List<PossibleError> buildPossibleErrors() {
        return null;
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
        Constants.PRIMARY_STAGE.close();
    }

    @Override
    public void onSubmit(ActionEvent actionEvent) throws IOException, SQLException {

    }

    @Override
    public void reset() {
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentFullName"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacherFullName"));
        lessonTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lessonTimeFormatted"));
        goldCupColumn.setCellValueFactory(new PropertyValueFactory<>("goldCup"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("levelName"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        instrumentColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));
        isNewStudentColumn.setCellValueFactory(new PropertyValueFactory<>("newStudent"));

        weekTable.setItems(lessonsRepository.getAllLessonData());
        this.setUserId();
    }

    @Override
    public void handleDoubleClickOnRow(LessonDTO item) throws SQLException, InstantiationException, IllegalAccessException {

    }

    public void onSignOut(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.LOGIN_SCREEN, 0);
    }
}
