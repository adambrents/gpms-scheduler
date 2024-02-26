package org.scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.scheduler.constants.Constants;
import org.scheduler.controller.base.ControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.LessonsDTO;
import org.scheduler.repository.StudentsDTO;
import org.scheduler.viewmodels.Lesson;
import org.scheduler.viewmodels.Student;
import org.scheduler.viewmodels.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController extends ControllerBase implements IController {
    private static final Logger _logger = LoggerFactory.getLogger(MainScreenController.class);
    @FXML
    private Button addCustomer;
    @FXML
    private TableColumn<Student, String> studentMonth;
    @FXML
    private TableColumn<Student, String> studentWeek;
    @FXML
    private TableColumn<Student, String> studentAddressColumn;
    @FXML
    private TableColumn<Student, String>  customerMonth;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    private TableColumn<Student, String>  customerWeek;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private Button deleteCustomer;
    @FXML
    private TableColumn<Lesson, String> descriptionMonth;
    @FXML
    private TableColumn<Lesson, String> descriptionWeek;
    @FXML
    private TableColumn<Lesson, String> endMonth;
    @FXML
    private TableColumn<Lesson, String> endWeek;
    @FXML
    private Button exit;
    @FXML
    private TableColumn<Lesson, String> idMonth;
    @FXML
    private TableColumn<Lesson, String> idWeek;
    @FXML
    private TableColumn<Lesson, String> locationMonth;
    @FXML
    private TableColumn<Lesson, String> locationWeek;
    @FXML
    private Button modifyCustomer;
    @FXML
    private TableView<Lesson> monthTable;
    @FXML
    private Button onAddAppointment;
    @FXML
    private Button onDeleteAppointment;
    @FXML
    private Button onModifyAppointment;
    @FXML
    private TableColumn<Lesson, String> startDate;
    @FXML
    private TableColumn<Lesson, String> startDate1;
    @FXML
    private TableColumn<Lesson, String> startMonth;
    @FXML
    private TableColumn<Lesson, String> startWeek;
    @FXML
    private Tab thisMonthTab;
    @FXML
    private Tab thisWeekTab;
    @FXML
    private TableColumn<Lesson, String> titleMonth;
    @FXML
    private TableColumn<Lesson, String> titleWeek;
    @FXML
    private TableColumn<Lesson, String> typeMonth;
    @FXML
    private TableColumn<Lesson, String> typeWeek;
    @FXML
    private TableColumn<User, String> userMonth;
    @FXML
    private TableColumn<User, String> userWeek;
    @FXML
    private TableColumn<Student, String> studentIdColumn;
    @FXML
    private TableView<Lesson> weekTable;
    private Lesson selectedLesson = null;
    private Student selectedStudent = null;
    private int userId;
    private final LessonsDTO lessonsDTO = new LessonsDTO();
    private final StudentsDTO studentsDTO = new StudentsDTO();
    /**
     * When the add customer button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.ADD_STUDENT_SCRN, userId);
    }
    /**
     * When the modify customer button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyStudent(ActionEvent actionEvent) throws IOException {
        _studentToBeModified = studentTable.getSelectionModel().getSelectedItem();

        if (_studentToBeModified == null) {
            return;
        }

        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.MOD_STUDENT_SCRN, userId);
    }
    /**
     * When the delete customer button is clicked, validate request with user then proceed with deleting from db(and ui table)
     * Also validates delete eligibility and deletes any associated appointments if applicable
     * @param actionEvent
     */
    public void onDeleteCustomer(ActionEvent actionEvent) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if(selectedStudent != null) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Do you want to delete this customer and all associated appointments?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    if(lessonsDTO.checkForLessons(selectedStudent)){
                        studentsDTO.deleteStudent(selectedStudent);
                        studentTable.setItems(studentsDTO.getAllStudents());
                        return;
                    }
                    if(!lessonsDTO.checkForLessons(selectedStudent)){
                        lessonsDTO.deleteAppointment(selectedStudent.getId());
                        studentsDTO.deleteStudent(selectedStudent);
                        studentTable.setItems(studentsDTO.getAllStudents());
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error");
                        alert.setContentText("Issue deleting customer - see console for details");
                    }
                }catch (Exception e){
                    _logger.error(e.getMessage());
                }
            } else {
                alert1.close();
            }
        }
    }

    /**
     * When the exit button is clicked, exit application
     * @param actionEvent
     */
    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    /**
     * When the add appointment button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.ADD_LESSON_SCRN, userId);
    }
    /**
     * When the modify appointment button is clicked, load corresponding screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyAppointment(ActionEvent actionEvent) throws IOException {
        if (thisWeekTab.isSelected()) {
            selectedLesson = weekTable.getSelectionModel().getSelectedItem();
        }
        else if(thisMonthTab.isSelected()) {
            selectedLesson = monthTable.getSelectionModel().getSelectedItem();
        }
        if (selectedLesson == null) {
            return;
        }
        if (selectedLesson.getStart().isBefore(LocalDateTime.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You may not alter an appointment that has already occurred :(");
            alert.showAndWait();
            return;
        }
        _lessonToBeModified = selectedLesson;

        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.MOD_LESSON_SCRN, userId);
    }
    /**
     * when clicked, user is warned of deleting appointment, then appointment is deleted
     *
     * @param actionEvent
     */
    public void onDeleteAppointment(ActionEvent actionEvent) {
        if (thisWeekTab.isSelected()) {
            selectedLesson = weekTable.getSelectionModel().getSelectedItem();
            if (selectedLesson == null) {
                return;
            }
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLesson.getLessonID() + ", Type: " + selectedLesson.getType() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK) {
                lessonsDTO.deleteAppointment(selectedLesson);
                weekTable.setItems(lessonsDTO.getWeeklyLessons());
            }
            else {
                alert1.close();
            }
        } else if (thisMonthTab.isSelected()) {
            selectedLesson = monthTable.getSelectionModel().getSelectedItem();
            if (selectedLesson == null) {
                return;
            }
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLesson.getLessonID() + ", Type: " + selectedLesson.getType() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK){
                lessonsDTO.deleteAppointment(selectedLesson);
                monthTable.setItems(lessonsDTO.getMonthlyLessons());
            }
            else {
                alert1.close();
            }
        }
    }
    /**
     * sets userId
     */
    private void setUserId() {
        userId = _userId;
    }
    /**
     * Loads reports screen
     * @param actionEvent
     * @throws IOException
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.REPORTS_SCRN, userId);
    }
    /**
     * loads allappointments screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAllAppointments(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.ALL_LESSON_SCRN, userId);
    }
    /**
     * init method, sets values for customer and appointment ui tables from db
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Get data for Student Table
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        studentAddressColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //sets table with customer data
        studentTable.setItems(studentsDTO.getAllStudents());

        //Get data for Weekly LessonsDTO table
        idWeek.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleWeek.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionWeek.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationWeek.setCellValueFactory(new PropertyValueFactory<>("location"));
        studentWeek.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeWeek.setCellValueFactory(new PropertyValueFactory<>("type"));
        startWeek.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endWeek.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerWeek.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userWeek.setCellValueFactory(new PropertyValueFactory<>("userID"));

        //sets table with weekly appt data
        weekTable.setItems(lessonsDTO.getWeeklyLessons());

        //Get data for Monthly LessonsDTO table
        idMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleMonth.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionMonth.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationMonth.setCellValueFactory(new PropertyValueFactory<>("location"));
        studentMonth.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeMonth.setCellValueFactory(new PropertyValueFactory<>("type"));
        startMonth.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endMonth.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDate1.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerMonth.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userMonth.setCellValueFactory(new PropertyValueFactory<>("userID"));
        //sets table with monthly appt data
        monthTable.setItems(lessonsDTO.getMonthlyLessons());
        this.setUserId();
    }
}
