package org.scheduler.controller.lesson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import org.scheduler.constants.Constants;
import org.scheduler.controller.MainScreenController;
import org.scheduler.controller.base.LessonControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.controller.interfaces.ICreate;
import org.scheduler.repository.LessonsRepository;
import org.scheduler.dto.LessonDTO;
import org.scheduler.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.StudentsRepository;
import org.scheduler.repository.UsersRepository;
import org.scheduler.dto.StudentDTO;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.ResourceBundle;

public class AddLessonControllerController extends LessonControllerBase implements ICreate, IController {
    private static final Logger _logger = LoggerFactory.getLogger(MainScreenController.class);
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
    private TextField type;
    private int userId;
    boolean error;
    String label;
    private final ObservableList<String> customerNames = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();

    private final ObservableList<String> userNames = FXCollections.observableArrayList();

    private final LessonsRepository lessonsRepository = new LessonsRepository();
    private final StudentsRepository studentsRepository = new StudentsRepository();
    private final UsersRepository usersRepository = new UsersRepository();
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
        super.goBack();
    }

    /**
     * Adding an appointment checks for appointment conflicts again, saves appointment to the db, and loads the main screen
     */
    public void onAdd(ActionEvent actionEvent) throws IOException {
        error = false;
        label = "";
        if((description.getText() == null) || (type.getText() == null) || (date.getValue() == null)
                || (startTime.getValue() == null) ||  (endTime.getValue() == null) || (location.getText() == null)
                || (student.getValue() == null)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must provide a value for every field");
            alert.showAndWait();
            return;
        }
        if (description.getText().length() > 50 || type.getText().length() > 50 || location.getText().length() > 50) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Text Box error");
            alert.setContentText("All text boxes must have less than 50 characters");
            alert.showAndWait();
            return;
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
        LocalDateTime startDateTime = LocalDateTime.of(date.getValue(), startTime.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(date.getValue(), endTime.getValue());

        LessonDTO lessonDTO = new LessonDTO(
                lessonsRepository.getId(),
                description.getText(),
                location.getText(),
                type.getText(),
                startDateTime,
                endDateTime,
                userId);
        lessonsRepository.insertItem(lessonDTO);
        errorText.setText("Successfully added lessonDTO :)");
        description.clear();
        location.clear();
        type.clear();
        startTime.valueProperty().set(null);
        endTime.valueProperty().set(null);
        student.valueProperty().set(null);
        employee.valueProperty().set(null);
        date.setValue(null);
        try {
            wait(1000);
        }
        catch (InterruptedException e)
        {
            _logger.error("Error while waiting! Exception: e", e);
        }

        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.MAIN_SCREEN, usersRepository.getUserByName(employee.getValue()).userId());
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
        ObservableList<StudentDTO> studentDTOS = studentsRepository.getAllItems();
        while(i < studentDTOS.size()){
            String customerName = studentDTOS.get(i).getFirstName();
            customerNames.add(i,customerName);
            i++;
        }
        student.setItems(customerNames);

        i = 0;
        userNames.clear();
        ObservableList<UserDTO> allUserDTOS = usersRepository.getAllItems();
        while(i < allUserDTOS.size()){
            String userName = allUserDTOS.get(i).username();
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
