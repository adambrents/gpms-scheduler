package org.scheduler.app.controller.lesson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.base.LessonControllerBase;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.controller.interfaces.IUpdate;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.repository.LessonsRepository;
import org.scheduler.data.repository.StudentsRepository;
import org.scheduler.data.repository.UsersRepository;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.StudentDTO;
import org.scheduler.data.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyLessonController extends LessonControllerBase implements IUpdate, IController {

    private static final Logger _logger = LoggerFactory.getLogger(ModifyLessonController.class);
    @FXML
    private ComboBox<String> contact;

    @FXML
    private ComboBox<String> student;

    @FXML
    private ComboBox<String> employee;
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
    @FXML
    private Button update;
    private final ObservableList<String> customerNames = FXCollections.observableArrayList();
    private final ObservableList<String> contactNames = FXCollections.observableArrayList();
    private static int userId;
    private final ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();
    private int appointmentId;

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
            ObservableList<LocalDateTime> startDateTimes = getAllowedLessonStartTimes(LocalDate.from(date.getValue()));
            int i = 0;
            while (i < startDateTimes.size()){
                startTimes.add(startDateTimes.get(i).toLocalTime());
                ++i;
            }
            if(startTimes.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("There are no other appointments available on the date selected");
                alert.showAndWait();
            }
            if(startTime.getValue() != null){
                startTimes.add(LocalTime.parse(startTime.getValue().toString()));
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
            ObservableList<LocalDateTime> endDateTimes = getAllowedLessonEndTimes(LocalDate.from(date.getValue()), LocalTime.parse(startTime.getValue().toString()));
            int i = 0;
            while (i < endDateTimes.size()){
                endTimes.add(endDateTimes.get(i).toLocalTime());
                ++i;
            }
            if(endTime.getValue() != null){
                endTimes.add(LocalTime.parse(endTime.getValue().toString()));
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
     * loads all appointment information into the modify appointment screen to be changed and reviewed
     *
     * Lambda function - ensures the dates available do not conflict with local business days based on the user's region
     * @param selectedLessonDTO
     */
    public void setAppointment(LessonDTO selectedLessonDTO) {
        int i = 0;
        try {
            while(i < studentsRepository.getAllItems().size()){
                ObservableList<StudentDTO> studentDTOS = studentsRepository.getAllItems();
                String customerName = studentDTOS.get(i).getFirstName();
                customerNames.add(i,customerName);
                i++;
            }
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throw new RuntimeException(throwables);
        }
        student.setItems(customerNames);
        student.setValue(studentsRepository.getStudentNameFromId(selectedLessonDTO.getStudentID()));
        i = 0;
        userNames.clear();
        ObservableList<UserDTO> allUserDTOS = null;
        try {
            allUserDTOS = usersRepository.getAllItems();
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throw new RuntimeException(throwables);
        }
        while(i < allUserDTOS.size()){
            String userName = allUserDTOS.get(i).username();
            userNames.add(i,userName);
            i++;
        }
        employee.setItems(userNames);
        employee.setValue(usersRepository.getUserById(selectedLessonDTO.getUserID()).username());
        description.setText(selectedLessonDTO.getDescription());
        type.setText(selectedLessonDTO.getType());
        startTime.setValue(selectedLessonDTO.getStartTime());
        endTime.setValue(selectedLessonDTO.getEndTime());
        date.setValue(selectedLessonDTO.getStart().toLocalDate());
        location.setText(selectedLessonDTO.getLocation());
        appointmentId = selectedLessonDTO.getLessonID();
        //checks to see if the selected date is before current date
        int index = 0;
        int validStartTimeCount = getAllowedLessonStartTimes(date.getValue()).size();
        startTimes.clear();
        if(startTime.getValue() != null){
            startTimes.add(LocalTime.from(LocalTime.parse(startTime.getValue().toString())));
        }
        while (index < validStartTimeCount){
            LocalDateTime localDateTime = getAllowedLessonStartTimes(LocalDate.from(date.getValue())).get(index);
            startTimes.add(localDateTime.toLocalTime());
            index++;
        }
        index = 0;
        int validEndTimeCount = getAllowedLessonEndTimes(LocalDate.from(date.getValue()),LocalTime.parse(startTime.getValue().toString())).size();
        endTimes.clear();
        if(endTime.getValue() != null){
            endTimes.add(LocalTime.from(LocalTime.parse(endTime.getValue().toString())));
        }
        while (index < validEndTimeCount){
            LocalDateTime localDateTime = getAllowedLessonEndTimes(LocalDate.from(date.getValue()),LocalTime.parse(startTime.getValue().toString())).get(index);
            endTimes.add(localDateTime.toLocalTime());
            index++;
        }
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

    /**
     * cancelling loads the Main Screen
     * @param actionEvent
     * @throws IOException
     */
    public void onGoBack(ActionEvent actionEvent){
        try{
            super.goBack(userId);
        }
        catch (IOException e){
            _logger.error("Could not load resource!", e);
        }
    }

    /**
     * Updating an appointment checks for appointment conflicts again, saves appointment to the db, and loads the main screen
     * @param actionEvent
     */
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        boolean error = false;
        String label = "";
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
        LocalDateTime startDateTime = LocalDateTime.of(date.getValue(), startTime.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(date.getValue(), endTime.getValue());
        try {
            lessonsRepository.updateItem(new LessonDTO(
                    appointmentId,
                    description.getText(),
                    location.getText(),
                    type.getText(),
                    startDateTime,
                    endDateTime,
                    userId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        errorText.setText("Successfully updated lessonDTO :)");
        super.loadNewScreen(Constants.FXML_ROUTES.MAIN_SCREEN, usersRepository.getUserByName(employee.getValue()).userId());
    }
    /**
     * method loads preset values into all applicable fields
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public List<PossibleError> buildPossibleErrors() {
        return null;
    }

    @Override
    public String getNameForItem(Object item) {
        return "";
    }
}
