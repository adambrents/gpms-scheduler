package org.scheduler.controller.lesson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import org.scheduler.controller.MainScreenController;
import org.scheduler.controller.base.LessonControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.controller.interfaces.ICreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.Contacts;
import org.scheduler.repository.LessonsDTO;
import org.scheduler.repository.StudentsDTO;
import org.scheduler.repository.UsersDTO;
import org.scheduler.viewmodels.Contact;
import org.scheduler.viewmodels.Lesson;
import org.scheduler.viewmodels.Student;
import org.scheduler.viewmodels.User;

import java.net.URL;
import java.time.*;
import java.util.ResourceBundle;

public class AddLessonControllerController extends LessonControllerBase implements ICreate, IController {
    private static final Logger _logger = LoggerFactory.getLogger(MainScreenController.class);
    @FXML
    private ComboBox<String> contact;
    @FXML
    private ComboBox<String> employee;
    @FXML
    private ComboBox<String> student;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea description;
    @FXML
    private ComboBox<LocalTime> endTime;
    @FXML
    private Label errorText;
    @FXML
    private TextField location;
    @FXML
    private ComboBox<LocalTime> startTime;
    @FXML
    private TextField title;
    @FXML
    private TextField type;
    private int userId;
    boolean error;
    String label;
    private final ObservableList<String> customerNames = FXCollections.observableArrayList();
    private final ObservableList<String> contactNames = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();

    private final ObservableList<String> userNames = FXCollections.observableArrayList();

    private final LessonsDTO lessonsDTO = new LessonsDTO();
    private final StudentsDTO studentsDTO = new StudentsDTO();
    /**
     * when a date is selected, start and end times are populated
     * @param event
     */
    @FXML
    void onDate(ActionEvent event) {
        startTime.setItems(null);
        endTime.setItems(null);
        startTime.setValue(null);
        endTime.setValue(null);
        startTimes.clear();
        endTimes.clear();
        if(date.getValue() == null){

        }
        else{
            ObservableList<LocalDateTime> startDateTimes = getAllowedLessonStartTimes(date.getValue());

            int i = 0;
            while (i < startDateTimes.size()){
                startTimes.add(startDateTimes.get(i).toLocalTime());
                ++i;
            }

            if(startTimes.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("There are no appointments available on the date selected");
                    alert.showAndWait();
                    return;
            }

            startTime.setItems(startTimes);
        }
    }

    /**
     * when end time is selected, date and start time fields are checked to ensure a value is present
     * if value is present, end times will be added to drop down only if there are no collisions with other appointments
     * if collisions exist, all times equal to and after start times will not be displayed in drop down
     * @param event
     */
    @FXML
    void onMouseClickEnd(InputEvent event) {
        endTimes.clear();
        endTime.setItems(null);
        if(date.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must enter a date to see end times");
            alert.showAndWait();
        }
        else if(startTime.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must enter a start time to see end times");
            alert.showAndWait();
        }
        else{

            ObservableList<LocalDateTime> endDateTimes = getAllowedLessonEndTimes(date.getValue(), startTime.getValue());

            int i = 0;
            while (i < endDateTimes.size()){
                endTimes.add(endDateTimes.get(i).toLocalTime());
                ++i;
            }
            endTime.setItems(endTimes);
        }
    }
    /**
     * when start time is selected, date field is checked to ensure a value is present
     * @param event
     */
    @FXML
    void onMouseClickStart(InputEvent event) {
        endTime.setValue(null);
        if(date.getValue() == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must enter a date to see start times");
            alert.showAndWait();
        }
        else{

        }
    }

    /**
     * cancelling loads the Main Screen
     * @param event
     */
    @FXML
    public void onCancel(ActionEvent event) {
        _primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        goBack();
    }

    /**
     * Adding an appointment checks for appointment conflicts again, saves appointment to the db, and loads the main screen
     */
    public void onAdd(ActionEvent event) {
        error = false;
        label = "";
        if((title.getText() == null) || (description.getText() == null) || (type.getText() == null) || (date.getValue() == null)
                || (startTime.getValue() == null) ||  (endTime.getValue() == null) || (location.getText() == null) || (student.getValue() == null)
        ||(contact.getValue() == null)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must provide a value for every field");
            alert.showAndWait();
            return;
        }
        if (title.getText().length() > 50 || description.getText().length() > 50 || type.getText().length() > 50 || location.getText().length() > 50) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Text Box error");
            alert.setContentText("All text boxes must have less than 50 characters");
            alert.showAndWait();
            return;
        }
        if(title.getText().length() < 1){
            error = true;
            label = "Title";
        }
        if(description.getText().length() < 1){
            error = true;
            label = "Description";
        }
        if(type.getText().length() < 1){
            error = true;
            label = "Type";
        }
        if(location.getText().length() < 1){
            error = true;
            label = "Location";
        }
        if(error){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(label + " Field Error");
            alert.setContentText(label + " field must not be empty");
            alert.showAndWait();
            return;
        }
        if(employee.getValue() != null){
            setUser(UsersDTO.getUserByName(employee.getValue()).userId());
        }
        LocalDateTime startDateTime = LocalDateTime.of(date.getValue(), startTime.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(date.getValue(), endTime.getValue());

        Lesson lesson = new Lesson(
                lessonsDTO.getId(),
                title.getText(),
                description.getText(),
                location.getText(),
                type.getText(),
                startDateTime,
                endDateTime,
                studentsDTO.getCustomerId(student.getValue()),
                userId,
                Contacts.getContactID(contact.getValue()),
                contact.getValue());
        if (lessonsDTO.addAppointment(lesson)){
            errorText.setText("Successfully added lesson :)");
            title.clear();
            description.clear();
            location.clear();
            type.clear();
            startTime.valueProperty().set(null);
            endTime.valueProperty().set(null);
            student.valueProperty().set(null);
            contact.valueProperty().set(null);
            employee.valueProperty().set(null);
            date.setValue(null);
        }
    }

    /**
     * method loads preset values into all applicable fields and uses a lambda function to ensure dates don't conflict with the local business days of.
     * Justification: I need to ensure the available dates in the date picker are within the M-F business days of the business and user
     * the user's region
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int i = 0;
        customerNames.clear();
        ObservableList<Student> students = studentsDTO.getAllCustomers();
        while(i < students.size()){
            String customerName = students.get(i).getName();
            customerNames.add(i,customerName);
            i++;
        }
        student.setItems(customerNames);

        i = 0;
        contactNames.clear();
        ObservableList<Contact> contacts = Contacts.getAllContacts();
        while(i < contacts.size()){
            String contactName = contacts.get(i).getContactName();
            contactNames.add(i,contactName);
            i++;
        }
        contact.setItems(contactNames);

        i = 0;
        userNames.clear();
        ObservableList<User> allUsers = UsersDTO.getAllUsers();
        while(i < allUsers.size()){
            String userName = allUsers.get(i).username();
            userNames.add(i,userName);
            i++;
        }
        employee.setItems(userNames);

        startTime.setItems(startTimes);
        endTime.setItems(endTimes);

        //Lambda function - ensures the dates available do not conflict with local business days based on the user's region
        date.setDayCellFactory(datePicker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date,empty);
                setDisable(empty || date.isBefore(ZonedDateTime.now(ZoneId.of("America/New_York")).toLocalDate()));
            }
        });

    }
}
