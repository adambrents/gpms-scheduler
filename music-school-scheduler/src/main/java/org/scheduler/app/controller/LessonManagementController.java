package org.scheduler.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseButton;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.*;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.LessonScheduledDTO;
import org.scheduler.data.dto.StudentDTO;
import org.scheduler.data.dto.TeacherDTO;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.properties.LevelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class LessonManagementController extends ControllerBase implements ICreate, IUpdate, IDelete, IExport, IController, ITableView<LessonDTO> {
    private static final Logger _logger = LoggerFactory.getLogger(MainScreenController.class);
    public TableView<LessonDTO> lessonsTable;
    public TableColumn<LessonDTO, String> studentNameColumn;
    public TableColumn<LessonDTO, String> teacherNameColumn;
    public TableColumn<LessonDTO, String> instrumentColumn;
    public TableColumn<LessonDTO, String> dayTimeColumn;
    public Label startTimeLabel;
    public Label endTimeLabel;
    public ComboBox<StudentDTO> studentComboBox;
    public Label studentLabel;
    public ComboBox<LocalTime> startTimeComboBox;
    public ComboBox<LocalTime> endTimeComboBox;
    public Label teacherLabel;
    public ComboBox<TeacherDTO> teacherComboBox;
    public Label notesLabel;
    public TextArea notesTextArea;
    public Label instrumentLabel;
    public ComboBox<InstrumentDTO> instrumentComboBox;
    public ToggleGroup daysOfWeek;
    public RadioButton mondayRadioButton;
    public RadioButton saturdayRadioButton;
    public RadioButton fridayRadioButton;
    public RadioButton thursdayRadioButton;
    public RadioButton wednesdayRadioButton;
    public RadioButton tuesdayRadioButton;
    public Label bookLabel;
    public ComboBox<BookDTO> bookComboBox;
    public Label levelLabel;
    public ComboBox<LevelDTO> levelComboBox;
    public CheckBox isNewStudentCheckbox;
    private LessonDTO lessonToBeUpdated;
    private DayOfWeek selectedDayOfTheWeek;

    /**
     * cancelling loads the Main Screen
     * @param actionEvent
     */
    @FXML
    public void onGoBack(ActionEvent actionEvent) {
        try{
            super.goBack(userId);
        }
        catch (IOException e){
            _logger.error("Could not load resource!", e);
        }
    }

    @Override
    public void onSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if(lessonToBeUpdated != null){
            onUpdate(actionEvent);
        }
        else {
            onAdd(actionEvent);
        }
    }

    /**
     * Adding an appointment checks for appointment conflicts again, saves appointment to the db, and loads the main screen
     */
    public void onAdd(ActionEvent actionEvent) throws IOException {
        String errorMessage = super.getErrorMessage();
        if(!errorMessage.equals("")) {
            errorText.setText(errorMessage);
            errorText.setVisible(true);
        }
        else {
            try {
                LessonScheduledDTO scheduledLesson = new LessonScheduledDTO(
                        notesTextArea.getText(),
                        startTimeComboBox.getValue(),
                        endTimeComboBox.getValue(),
                        selectedDayOfTheWeek,
                        LocalDateTime.now(),
                        userId,
                        LocalDateTime.now(),
                        userId,
                        isNewStudentCheckbox.isSelected(),
                        bookComboBox.getValue(),
                        levelComboBox.getValue(),
                        instrumentComboBox.getValue(),
                        0
                );

                LessonDTO newLesson = new LessonDTO(
                        studentComboBox.getValue(),
                        teacherComboBox.getValue(),
                        scheduledLesson,
                        LocalDateTime.now(),
                        userId,
                        LocalDateTime.now(),
                        userId);
                lessonsRepository.addLesson(newLesson, JDBC.getConnection());
                reset();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            errorText.setText("Successfully added lessonDTO :)");
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
        reset();
        lessonsTable.setRowFactory(tv -> {
            TableRow<LessonDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    try {
                        handleDoubleClickOnRow(row.getItem());
                    } catch (SQLException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });

        studentComboBox.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            populateTeacherComboBox();
        });

        daysOfWeek.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedDayOfTheWeek = DayOfWeek.valueOf(((RadioButton) newValue).getText().toUpperCase());
                populateStartTimesComboBox();
            }
        });

        startTimeComboBox.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            populateEndTimesComboBox();
        });

        populateComboBox(studentComboBox, studentsRepository.getAllStudentData());
        populateComboBox(instrumentComboBox, instrumentRepository.getAllItems());
        populateComboBox(bookComboBox, bookRepository.getAllItems());
        populateComboBox(levelComboBox, levelRepository.getAllItems());
    }

    @Override
    public List<PossibleError> buildPossibleErrors() {
        List<PossibleError> possibleErrors = new ArrayList<>();
        String studentComboBoxValue = studentComboBox.getValue() == null ? "" : studentComboBox.getValue().getFullName();
        String teacherComboBoxValue = teacherComboBox.getValue() == null ? "" : teacherComboBox.getValue().getFullName();
        String instrumentComboBoxValue = instrumentComboBox.getValue() == null ? "" : instrumentComboBox.getValue().getName();
        String startTimeComboBoxValue = startTimeComboBox.getValue() == null ? "" : startTimeComboBox.getValue().toString();
        String levelComboBoxValue = levelComboBox.getValue() == null ? "" : levelComboBox.getValue().getName();
        String bookComboBoxValue = bookComboBox.getValue() == null ? "" : bookComboBox.getValue().getName();
        possibleErrors.add(new PossibleError("Student", studentComboBoxValue, studentLabel));
        possibleErrors.add(new PossibleError("Teacher", teacherComboBoxValue, teacherLabel));
        possibleErrors.add(new PossibleError("Instrument", instrumentComboBoxValue, instrumentLabel));
        possibleErrors.add(new PossibleError("Start Time", startTimeComboBoxValue, startTimeLabel));
        possibleErrors.add(new PossibleError("Level", levelComboBoxValue, levelLabel));
        possibleErrors.add(new PossibleError("Book", bookComboBoxValue, bookLabel));

        return possibleErrors;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void onDelete(ActionEvent actionEvent) {
        LessonDTO lessonDTO = lessonsTable.getSelectionModel().getSelectedItem();

        try {
            lessonsRepository.deleteItem(lessonDTO, JDBC.getConnection());
            reset();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onExportToCSV(ActionEvent actionEvent) {

    }

    @Override
    public void onUpdate(ActionEvent actionEvent) throws IOException, SQLException {
        String errorMessage = super.getErrorMessage();
        if(!errorMessage.equals("")){
            errorText.setText(errorMessage);
        }
        else {
            LessonScheduledDTO scheduledLesson = new LessonScheduledDTO(
                    lessonToBeUpdated.getScheduledLesson().getId(),
                    notesTextArea.getText(),
                    startTimeComboBox.getValue(),
                    endTimeComboBox.getValue(),
                    selectedDayOfTheWeek,
                    LocalDateTime.now(),
                    userId,
                    isNewStudentCheckbox.isSelected(),
                    bookComboBox.getValue(),
                    levelComboBox.getValue(),
                    instrumentComboBox.getValue(),
                    lessonToBeUpdated.getId()
            );

            LessonDTO newLesson = new LessonDTO(
                    lessonToBeUpdated.getId(),
                    studentComboBox.getValue(),
                    teacherComboBox.getValue(),
                    scheduledLesson,
                    LocalDateTime.now(),
                    userId);
            if(newLesson.getId() != 0){
                try {
                    lessonsRepository.updateLesson(newLesson, JDBC.getConnection());
                    reset();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void handleDoubleClickOnRow(LessonDTO item) throws SQLException, InstantiationException, IllegalAccessException {
        reset();
        studentComboBox.setValue(item.getStudent());
        teacherComboBox.setValue(item.getTeacher());
        instrumentComboBox.setValue(item.getScheduledLesson().getInstrument());
        bookComboBox.setValue(item.getScheduledLesson().getBook());
        levelComboBox.setValue(item.getScheduledLesson().getLevel());
        selectDay(item.getScheduledLesson().getDayOfWeek());
        startTimeComboBox.setValue(item.getScheduledLesson().getStart());
        populateEndTimesComboBox();
        notesTextArea.setText(item.getScheduledLesson().getDescription());
        lessonToBeUpdated = item;
    }
    public void selectDay(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                mondayRadioButton.setSelected(true);
                break;
            case TUESDAY:
                tuesdayRadioButton.setSelected(true);
                break;
            case WEDNESDAY:
                wednesdayRadioButton.setSelected(true);
                break;
            case THURSDAY:
                thursdayRadioButton.setSelected(true);
                break;
            case FRIDAY:
                fridayRadioButton.setSelected(true);
                break;
            case SATURDAY:
                saturdayRadioButton.setSelected(true);
                break;
            default:
                daysOfWeek.selectToggle(null);
                break;
        }
    }

    @Override
    public void reset() {
        lessonToBeUpdated = null;
        daysOfWeek.selectToggle(null);
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentFullName"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherFullName"));
        dayTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lessonTimeFormatted"));
        instrumentColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));

        lessonsTable.setItems(lessonsRepository.getAllLessonData());

        labelsToReset.clear();

        Collections.addAll(
                labelsToReset,
                studentLabel,
                teacherLabel,
                instrumentLabel,
                startTimeLabel,
                levelLabel,
                bookLabel
        );
        super.reset();
    }

    private void populateTeacherComboBox() {
        StudentDTO selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            List<TeacherDTO> teachers = teachersRepository.getAllItems();
            populateComboBox(teacherComboBox, teachers);
        } else {
            teacherComboBox.getItems().clear();
        }
    }
    private void populateInstrumentComboBox() {
        StudentDTO selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
        TeacherDTO selectedTeacher = teacherComboBox.getSelectionModel().getSelectedItem();

        if (selectedStudent != null && selectedTeacher != null) {
            List<InstrumentDTO> instruments = instrumentRepository.getInstrumentsByTeacherStudent(selectedStudent.getId(), selectedTeacher.getId());
            populateComboBox(instrumentComboBox, instruments);
        } else {
            instrumentComboBox.getItems().clear();
        }
    }

    private void populateStartTimesComboBox() {
        StudentDTO selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
        TeacherDTO selectedTeacher = teacherComboBox.getSelectionModel().getSelectedItem();

        if (selectedStudent != null && selectedTeacher != null) {
            List<LocalTime> localTimes = lessonsRepository.getPossibleStartTimesForTeacherAndStudent(selectedStudent.getId(), selectedTeacher.getId(), selectedDayOfTheWeek);
            populateComboBoxTimes(startTimeComboBox, localTimes);
        } else {
            startTimeComboBox.getItems().clear();
        }
    }

    private void populateEndTimesComboBox() {
        populateComboBoxTimes(endTimeComboBox, new ArrayList<>());

        LocalTime startTime = startTimeComboBox.getSelectionModel().getSelectedItem();
        if (startTime != null) {
            LocalTime endTime = startTime.plusMinutes(30);
            endTimeComboBox.getItems().clear();
            endTimeComboBox.getItems().add(endTime);
            endTimeComboBox.getSelectionModel().selectFirst();
            endTimeComboBox.setDisable(true);
        } else {
            endTimeComboBox.getItems().clear();
            endTimeComboBox.setDisable(false);
        }
    }

    /**
     * when end time is selected, date and start time fields are checked to ensure a value is present
     * if value is present, end times will be added to drop down only if there are no collisions with other appointments
     * if collisions exist, all times equal to and after start times will not be displayed in drop down
     * @param event
     */
    @FXML
    private void onMouseClickEnd(InputEvent event) {
//        endTimes.clear();
//        endTime.setItems(null);
//        if(date.getValue() == null){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Error");
//            alert.setContentText("You must enter a date to see end times");
//            alert.showAndWait();
//        }
//        else if(startTime.getValue() == null){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Error");
//            alert.setContentText("You must enter a start time to see end times");
//            alert.showAndWait();
//        }
//        else{
//
//            ObservableList<LocalDateTime> endDateTimes = getAllowedLessonEndTimes(date.getValue(), startTime.getValue());
//
//            int i = 0;
//            while (i < endDateTimes.size()){
//                endTimes.add(endDateTimes.get(i).toLocalTime());
//                ++i;
//            }
//            endTime.setItems(endTimes);
//        }
    }
    /**
     * when start time is selected, date field is checked to ensure a value is present
     * @param event
     */
    @FXML
    private void onMouseClickStart(InputEvent event) {
//        endTime.setValue(null);
//        if(date.getValue() == null){
//
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Error");
//            alert.setContentText("You must enter a date to see start times");
//            alert.showAndWait();
//        }
//        else{
//
//        }
    }

}
