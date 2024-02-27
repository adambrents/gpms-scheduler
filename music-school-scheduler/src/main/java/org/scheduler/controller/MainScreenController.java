package org.scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.scheduler.constants.Constants;
import org.scheduler.controller.base.ControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.repository.LessonsRepository;
import org.scheduler.repository.StudentsRepository;
import org.scheduler.dto.LessonDTO;
import org.scheduler.dto.StudentDTO;
import org.scheduler.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private TableColumn<StudentDTO, String> studentMonth;
    @FXML
    private TableColumn<StudentDTO, String> studentWeek;
    @FXML
    private TableColumn<StudentDTO, String> studentAddressColumn;
    @FXML
    private TableColumn<StudentDTO, String>  customerMonth;
    @FXML
    private TableColumn<StudentDTO, String> studentNameColumn;
    @FXML
    private TableColumn<StudentDTO, String>  customerWeek;
    @FXML
    private TableView<StudentDTO> studentTable;
    @FXML
    private Button deleteCustomer;
    @FXML
    private TableColumn<LessonDTO, String> descriptionMonth;
    @FXML
    private TableColumn<LessonDTO, String> descriptionWeek;
    @FXML
    private TableColumn<LessonDTO, String> endMonth;
    @FXML
    private TableColumn<LessonDTO, String> endWeek;
    @FXML
    private Button exit;
    @FXML
    private TableColumn<LessonDTO, String> idMonth;
    @FXML
    private TableColumn<LessonDTO, String> idWeek;
    @FXML
    private TableColumn<LessonDTO, String> locationMonth;
    @FXML
    private TableColumn<LessonDTO, String> locationWeek;
    @FXML
    private Button modifyCustomer;
    @FXML
    private TableView<LessonDTO> monthTable;
    @FXML
    private Button onAddAppointment;
    @FXML
    private Button onDeleteAppointment;
    @FXML
    private Button onModifyAppointment;
    @FXML
    private TableColumn<LessonDTO, String> startDate;
    @FXML
    private TableColumn<LessonDTO, String> startDate1;
    @FXML
    private TableColumn<LessonDTO, String> startMonth;
    @FXML
    private TableColumn<LessonDTO, String> startWeek;
    @FXML
    private Tab thisMonthTab;
    @FXML
    private Tab thisWeekTab;
    @FXML
    private TableColumn<LessonDTO, String> titleMonth;
    @FXML
    private TableColumn<LessonDTO, String> titleWeek;
    @FXML
    private TableColumn<LessonDTO, String> typeMonth;
    @FXML
    private TableColumn<LessonDTO, String> typeWeek;
    @FXML
    private TableColumn<UserDTO, String> userMonth;
    @FXML
    private TableColumn<UserDTO, String> userWeek;
    @FXML
    private TableColumn<StudentDTO, String> studentIdColumn;
    @FXML
    private TableView<LessonDTO> weekTable;
    private LessonDTO selectedLessonDTO = null;
    private StudentDTO selectedStudentDTO = null;
    private int userId;
    private final LessonsRepository lessonsRepository = new LessonsRepository();
    private final StudentsRepository studentsRepository = new StudentsRepository();
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
        _studentDTOToBeModified = studentTable.getSelectionModel().getSelectedItem();

        if (_studentDTOToBeModified == null) {
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
        StudentDTO selectedStudentDTO = studentTable.getSelectionModel().getSelectedItem();
        if(selectedStudentDTO != null) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Do you want to delete this customer and all associated appointments?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    if(lessonsRepository.isStudentHaveLessons(selectedStudentDTO)){
                        studentsRepository.deleteItem(selectedStudentDTO);
                        studentTable.setItems(studentsRepository.getAllItems());
                        return;
                    }
                    if(!lessonsRepository.isStudentHaveLessons(selectedStudentDTO)){
                        lessonsRepository.deleteAllLessonsForStudent(selectedStudentDTO.getId());
                        studentsRepository.deleteItem(selectedStudentDTO);
                        studentTable.setItems(studentsRepository.getAllItems());
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
            selectedLessonDTO = weekTable.getSelectionModel().getSelectedItem();
        }
        else if(thisMonthTab.isSelected()) {
            selectedLessonDTO = monthTable.getSelectionModel().getSelectedItem();
        }
        if (selectedLessonDTO == null) {
            return;
        }
        if (selectedLessonDTO.getStart().isBefore(LocalDateTime.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You may not alter an appointment that has already occurred :(");
            alert.showAndWait();
            return;
        }
        _lessonDTOToBeModified = selectedLessonDTO;

        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.MOD_LESSON_SCRN, userId);
    }
    /**
     * when clicked, user is warned of deleting appointment, then appointment is deleted
     *
     * @param actionEvent
     */
    public void onDeleteAppointment(ActionEvent actionEvent) {
        if (thisWeekTab.isSelected()) {
            selectedLessonDTO = weekTable.getSelectionModel().getSelectedItem();
            if (selectedLessonDTO == null) {
                return;
            }
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLessonDTO.getLessonID() + ", Type: " + selectedLessonDTO.getType() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK) {
                lessonsRepository.deleteItem(selectedLessonDTO);
                weekTable.setItems(lessonsRepository.getWeeklyLessons());
            }
            else {
                alert1.close();
            }
        } else if (thisMonthTab.isSelected()) {
            selectedLessonDTO = monthTable.getSelectionModel().getSelectedItem();
            if (selectedLessonDTO == null) {
                return;
            }
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLessonDTO.getLessonID() + ", Type: " + selectedLessonDTO.getType() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK){
                lessonsRepository.deleteItem(selectedLessonDTO);
                monthTable.setItems(lessonsRepository.getMonthlyLessons());
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
        //Get data for StudentDTO Table
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        studentAddressColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //sets table with customer data
        studentTable.setItems(studentsRepository.getAllItems());

        //Get data for Weekly LessonsRepository table
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
        weekTable.setItems(lessonsRepository.getWeeklyLessons());

        //Get data for Monthly LessonsRepository table
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
        monthTable.setItems(lessonsRepository.getMonthlyLessons());
        this.setUserId();
    }
}
