package org.scheduler.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.models.errors.PossibleError;
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
     * When the add customer button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.STUDENT_MGMT_SCRN, userId);
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
                        lessonsRepository.deleteAllLessonsForStudent(selectedStudentDTO.getStudentId());
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
    public void onGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    /**
     * When the add appointment button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.ADD_LESSON_SCRN, userId);
    }

    /**
     * when clicked, user is warned of deleting appointment, then appointment is deleted
     *
     * @param actionEvent
     */
    public void onDeleteAppointment(ActionEvent actionEvent) {
        selectedLessonDTO = weekTable.getSelectionModel().getSelectedItem();
        if (selectedLessonDTO == null) {
            return;
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText("Delete");
        alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLessonDTO.getLessonID() + ", Type: " + selectedLessonDTO.getType() + "?");
        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                lessonsRepository.deleteItem(selectedLessonDTO);
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
     * Loads reports screen
     * @param actionEvent
     * @throws IOException
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.REPORTS_SCRN, userId);
    }
    /**
     * loads allappointments screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAllAppointments(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.ALL_LESSON_SCRN, userId);
    }

    public void onPropertyManagement(ActionEvent actionEvent) throws IOException {
        super.loadNewScreen(Constants.FXML_ROUTES.PROP_MGMT_SCRN, userId);
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
        try {
            studentTable.setItems(studentsRepository.getAllItems());
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throw new RuntimeException(throwables);
        }

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

    }

    public void onAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Music Scheduler");
        alert.setContentText("Version 1.0\nDeveloped by Adam Brents.\nFor support, contact me at adam.brents1@gmail.com");
        alert.showAndWait();
    }
}
