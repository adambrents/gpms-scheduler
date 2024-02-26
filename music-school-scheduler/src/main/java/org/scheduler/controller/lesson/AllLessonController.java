package org.scheduler.controller.lesson;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.scheduler.constants.Constants;
import org.scheduler.controller.base.LessonControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.repository.LessonsDTO;
import org.scheduler.viewmodels.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class AllLessonController extends LessonControllerBase implements IController {
    private static final Logger _logger = LoggerFactory.getLogger(AllLessonController.class);
    @FXML
    private TableView<Lesson> allLessonsTable;
    @FXML
    private TableColumn<Lesson, String> contactId;
    @FXML
    private TableColumn<Lesson, String> student;
    @FXML
    private TableColumn<Lesson, String> description;
    @FXML
    private TableColumn<Lesson, Date> end;
    @FXML
    private TableColumn<Lesson, String> lessonId;
    @FXML
    private TableColumn<Lesson, String> location;
    @FXML
    private TableColumn<Lesson, Date> start;
    @FXML
    private TableColumn<Lesson, Date> startDate;
    @FXML
    private TableColumn<Lesson, String> title;
    @FXML
    private TableColumn<Lesson, String> type;
    @FXML
    private TableColumn<Lesson, String> user;
    private int userId;

    private Parent scene;

    private Lesson selectedLesson = null;
    private final LessonsDTO lessonsDTO = new LessonsDTO();


    /**
     * loads all appointments into table for user to main.java.view
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lessonId.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactId.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        student.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        user.setCellValueFactory(new PropertyValueFactory<>("userID"));

        allLessonsTable.setItems(lessonsDTO.getAllLessons());
    }

    /**
     * Loads the add appointment screen
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    void onAdd(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.ADD_LESSON_SCRN, userId);
    }

    /**
     * Warns user of deleting the appointment, then deletes the appointment from the database
     * @param event
     */
    @FXML
    void onDelete(ActionEvent event) {
        selectedLesson = (allLessonsTable.getSelectionModel().getSelectedItem());
        if (selectedLesson == null) {
            return;
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText("Delete");
        alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLesson.getLessonID() + ", Type: " + selectedLesson.getType() + "?");
        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == ButtonType.OK) {
            lessonsDTO.deleteAppointment(selectedLesson);
            allLessonsTable.setItems(lessonsDTO.getAllLessons());
        }
        else {
            alert1.close();
        }

    }

    /**
     * loads the main screen
     * @param event
     * @throws IOException
     */
    @FXML
    @Override
    public void onCancel(ActionEvent event) {
        _primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        super.goBack();
    }

    /**
     * When user clicks the modify button, the modify appointment screen will load
     * @param actionEvent
     * @throws IOException
     */
    public void onModify(ActionEvent actionEvent) throws IOException {
        if (allLessonsTable.getSelectionModel() == null) {
            return;
        }
        else {
            selectedLesson = (allLessonsTable.getSelectionModel().getSelectedItem());
        }
        if (selectedLesson.getStart().isBefore(LocalDateTime.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You may not alter an appointment that has already occurred :(");
            alert.showAndWait();
            return;
        }

        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.MOD_LESSON_SCRN, userId);
    }
}
