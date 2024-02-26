package org.scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.scheduler.controller.base.ControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.controller.lesson.AddLessonControllerController;
import org.scheduler.controller.lesson.AllLessonController;
import org.scheduler.controller.lesson.ModifyLessonControllerController;
import org.scheduler.controller.report.ReportsController;
import org.scheduler.controller.student.AddStudentController;
import org.scheduler.controller.student.ModifyStudentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.LessonsDTO;
import org.scheduler.repository.StudentsDTO;
import org.scheduler.viewmodels.Contact;
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
    private TableColumn<Contact, String> contactMonth;
    @FXML
    private TableColumn<Contact, String>  contactWeek;
    @FXML
    private TableColumn<Contact, String>  customerAddressColumn;
    @FXML
    private TableColumn<Contact, String>  customerMonth;
    @FXML
    private TableColumn<Contact, String>  customerNameColumn;
    @FXML
    private TableColumn<Contact, String>  customerWeek;
    @FXML
    private TableView<Student>  customersTable;
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
    private TableColumn<Student, String> customerIdColumn;
    @FXML
    private TableView<Lesson> weekTable;
    private Parent scene;
    private Lesson selectedLesson = null;
    private int userId;
    private final LessonsDTO lessonsDTO = new LessonsDTO();
    private final StudentsDTO studentsDTO = new StudentsDTO();
    /**
     * When the add customer button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddStudentScreen.fxml"));
        scene = loader.load();
        AddStudentController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * When the modify customer button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyCustomer(ActionEvent actionEvent) throws IOException {
        Student selectedStudent = customersTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null){
            return;
        }
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyStudentsScreen.fxml"));
        scene = loader.load();
        ModifyStudentController controller = loader.getController();
        controller.setCustomer(selectedStudent);
        controller.setUserId(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * When the delete customer button is clicked, validate request with user then proceed with deleting from db(and ui table)
     * Also validates delete eligibility and deletes any associated appointments if applicable
     * @param actionEvent
     */
    public void onDeleteCustomer(ActionEvent actionEvent) {
        Student selectedStudent = customersTable.getSelectionModel().getSelectedItem();
        if(selectedStudent != null) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Do you want to delete this customer and all associated appointments?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    if(lessonsDTO.checkForLessons(selectedStudent)){
                        studentsDTO.deleteCustomer(selectedStudent);
                        customersTable.setItems(studentsDTO.getAllCustomers());
                        return;
                    }
                    if(!lessonsDTO.checkForLessons(selectedStudent)){
                        lessonsDTO.deleteAppointment(selectedStudent.getId());
                        studentsDTO.deleteCustomer(selectedStudent);
                        customersTable.setItems(studentsDTO.getAllCustomers());
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
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddLessonsScreen.fxml"));
        scene = loader.load();
        AddLessonControllerController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
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
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyLessonsScreen.fxml"));
        scene = loader.load();
        ModifyLessonControllerController controller = loader.getController();
        controller.setAppointment(selectedLesson);
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
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
            alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLesson.getAppointmentID() + ", Type: " + selectedLesson.getType() + "?");
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
            alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedLesson.getAppointmentID() + ", Type: " + selectedLesson.getType() + "?");
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
     * @param user
     */
    public void setUser(int user) {
        userId = user;
    }
    /**
     * Loads reports screen
     * @param actionEvent
     * @throws IOException
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        scene = loader.load();
        ReportsController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * loads allappointments screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAllAppointments(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllLessonsScreen.fxml"));
        scene = loader.load();
        AllLessonController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * init method, sets values for customer and appointment ui tables from db
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Get data for Student Table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //sets table with customer data
        customersTable.setItems(studentsDTO.getAllCustomers());

        //Get data for Weekly LessonsDTO table
        idWeek.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleWeek.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionWeek.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationWeek.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactWeek.setCellValueFactory(new PropertyValueFactory<>("contactName"));
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
        contactMonth.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeMonth.setCellValueFactory(new PropertyValueFactory<>("type"));
        startMonth.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endMonth.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDate1.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerMonth.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userMonth.setCellValueFactory(new PropertyValueFactory<>("userID"));
        //sets table with monthly appt data
        monthTable.setItems(lessonsDTO.getMonthlyLessons());
    }
}
