package org.scheduler.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.ReportDTO;
import org.scheduler.data.dto.StudentDTO;
import org.scheduler.data.dto.TeacherDTO;
import org.scheduler.data.dto.interfaces.IReportable;
import org.scheduler.data.dto.reports.InstrumentLessonsRatioDTO;
import org.scheduler.data.dto.reports.NumberStudentsInstrumentsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import static org.scheduler.data.dto.ReportDTO.Reports.*;

public class ReportsController<T extends IReportable>  extends ControllerBase implements IController {

    private static final Logger _logger = LoggerFactory.getLogger(ReportsController.class);
    
    public Label count;
    public Label countLabel;
    public ComboBox<TeacherDTO> teacherComboBox;
    public TableColumn<InstrumentLessonsRatioDTO, Integer> lessonCountColumn;
    public TableColumn<InstrumentLessonsRatioDTO, String> instrumentColumn;
    public TableColumn<InstrumentLessonsRatioDTO, String> teacherColumn;
    public TableView<InstrumentLessonsRatioDTO> ratioTable;
    public ComboBox<ReportDTO> reportComboBox;
    public ComboBox<Year> years;
    public ComboBox<Month> months;
    private final Year currentYear = Year.now();
    private final List<Year> yearsList = FXCollections.observableArrayList();
    private final ObservableList<ReportDTO> allReports = FXCollections.observableArrayList();
    public TextField studentSearch;
    public ToolBar studentsTableToolBar;
    public TableView<StudentDTO> studentsTable;
    public TableColumn<StudentDTO, String> studentNameColumn;
    public TableColumn<StudentDTO, String>  studentAddressColumn;
    public TableColumn<StudentDTO, String>  studentPhoneColumn;
    public TableColumn<StudentDTO, String>  studentBooksColumn;
    public TableColumn<StudentDTO, String>  studentLevelsColumn;
    public TableColumn<StudentDTO, String>  studentInstrumentsColumn;
    public TableColumn<StudentDTO, String>  studentTeachersColumn;
    public TableColumn<StudentDTO, String>  studentIsGoldCupColumn;
    public TableView<LessonDTO> lessonsTable;
    public TableColumn<LessonDTO, String>  lessonTeacherNameColumn;
    public TableColumn<LessonDTO, String> lessonStudentNameColumn;
    public TableColumn<LessonDTO, String> dayTimeColumn;
    public TableColumn<LessonDTO, String> lessonInstrumentColumn;
    public TableView<NumberStudentsInstrumentsDTO> studentInstrumentsTable;
    public TableColumn<NumberStudentsInstrumentsDTO, String> groupedInstrumentNameColumn;
    public TableColumn<NumberStudentsInstrumentsDTO, Integer> studentCountColumn;
    public Label reportNameLabel;

    private ObservableList<StudentDTO> allStudents;

    /**
     * Loads mainscreen
     * @param actionEvent
     * @throws IOException
     */
    @Override
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
        if(reportComboBox.getValue() != null){
            switch (reportComboBox.getValue().getReportEnum()){
                case INSTRUMENT_LESSONS_RATIO -> {
                    reportNameLabel.setText(INSTRUMENT_LESSONS_RATIO.name());
                    ObservableList<InstrumentLessonsRatioDTO> instrumentLessonsRatioReport = FXCollections.observableArrayList();
                    instrumentLessonsRatioReport.addAll(teachersRepository.getTeacherInstrumentToLessonsRatio());
                    populateRatioTableView(instrumentLessonsRatioReport);
                }
                case NUMBER_STUDENTS_INSTRUMENTS -> {
                    reportNameLabel.setText(NUMBER_STUDENTS_INSTRUMENTS.name());
                    ObservableList<NumberStudentsInstrumentsDTO> numberStudentsInstrumentsReport = FXCollections.observableArrayList();
                    numberStudentsInstrumentsReport.addAll(instrumentRepository.getStudentsPerInstrumentReport());
                    populateStudentsToInstrumentsTableView(numberStudentsInstrumentsReport);
                }
                case GOLDCUP -> {
                    reportNameLabel.setText(GOLDCUP.name());
                    ObservableList<StudentDTO> goldCupStudentsReport = allStudents;
                    goldCupStudentsReport = FXCollections.observableArrayList(goldCupStudentsReport
                            .stream()
                            .filter(StudentDTO::isGoldCup)
                            .toList());
                    populateGoldCupTableView(goldCupStudentsReport);
                }
                case NEW_STUDENTS -> {
                    reportNameLabel.setText(NEW_STUDENTS.name());
                    ObservableList<LessonDTO> newStudentLessonsReport = lessonsRepository.getAllLessonData();
                    newStudentLessonsReport = FXCollections.observableArrayList(newStudentLessonsReport
                            .stream()
                            .filter(lesson -> lesson.getScheduledLesson().isNewStudent())
                            .toList());
                    populateNewStudentTableView(newStudentLessonsReport);
                }
            }
        }

    }

    private void populateNewStudentTableView(ObservableList<LessonDTO> newStudentLessonsReport) {
        lessonStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentFullName"));
        lessonTeacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherFullName"));
        dayTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lessonTimeFormatted"));
        lessonInstrumentColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));

        lessonsTable.setItems(newStudentLessonsReport);

        lessonsTable.setVisible(true);
    }

    private void populateGoldCupTableView(ObservableList<StudentDTO> goldCupStudentsReport) {
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        studentAddressColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        studentPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        studentBooksColumn.setCellValueFactory(new PropertyValueFactory<>("studentBookNames"));
        studentLevelsColumn.setCellValueFactory(new PropertyValueFactory<>("studentLevelNames"));
        studentInstrumentsColumn.setCellValueFactory(new PropertyValueFactory<>("studentInstrumentNames"));
        studentTeachersColumn.setCellValueFactory(new PropertyValueFactory<>("studentTeacherNames"));
        studentIsGoldCupColumn.setCellValueFactory(new PropertyValueFactory<>("isGoldCup"));
        studentsTable.setItems(goldCupStudentsReport);
        studentsTable.setVisible(true);
        studentsTableToolBar.setVisible(true);
    }

    private void populateStudentsToInstrumentsTableView(ObservableList<NumberStudentsInstrumentsDTO> numberStudentsInstrumentsReport) {
        groupedInstrumentNameColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));
        studentCountColumn.setCellValueFactory(new PropertyValueFactory<>("studentCount"));

        studentInstrumentsTable.setItems(numberStudentsInstrumentsReport);

        studentInstrumentsTable.setVisible(true);
    }

    private void populateRatioTableView(ObservableList<InstrumentLessonsRatioDTO> instrumentLessonsRatioReport) {
        lessonCountColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfLessons"));
        instrumentColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacherFullName"));
        ratioTable.setItems(instrumentLessonsRatioReport);
        ratioTable.setVisible(true);
    }


    private boolean validateFields(boolean isYearsNull, boolean isMonthsNull, boolean isReportNull, boolean isTeacherNull) {
        StringBuilder errorMessage = new StringBuilder();
        if(isTeacherNull){
            errorMessage.append("Teacher is missing!");
        }
        if (isMonthsNull){
            errorMessage.append("\nMonth is missing!");
        }
        if(isYearsNull){
            errorMessage.append("\nYear is missing!");
        }
        if(isReportNull){
            errorMessage.append("\nReport is missing!");
        }
        if(errorMessage.toString() != ""){
            errorText.setText(errorMessage.toString());
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void reset() {
        reportNameLabel.setText("");
        count.setText("0");
        errorText.setVisible(false);
        ratioTable.setVisible(false);
        studentsTable.setVisible(false);
        lessonsTable.setVisible(false);
        studentInstrumentsTable.setVisible(false);
        studentsTableToolBar.setVisible(false);
        allStudents = studentsRepository.getAllStudentData();
        studentSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });
    }
    /**
     * loads all preset data into screen
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yearsList.clear();
        Collections.addAll(allReports,
                new ReportDTO("Teacher Instruments:Lessons Ratio Report", INSTRUMENT_LESSONS_RATIO),
                new ReportDTO("Total Students by Instruments Report", ReportDTO.Reports.NUMBER_STUDENTS_INSTRUMENTS),
                new ReportDTO("GoldCup Students Report", GOLDCUP),
                new ReportDTO("New Students Report", NEW_STUDENTS)
        );

        for (int i = 0; i < 5; i++) {
            yearsList.add(currentYear.plusYears(i));
        }
        months.setItems(FXCollections.observableList(Arrays.stream(Month.values()).toList()));
        years.setItems(FXCollections.observableList(yearsList));

        reportComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue != null){
                reset();
            }
        });

        populateComboBox(reportComboBox, allReports);
        populateComboBox(teacherComboBox, teachersRepository.getAllItems());
        reset();
    }

    @Override
    public List<PossibleError> buildPossibleErrors() {
        return null;
    }

    @Override
    public void setUserId(int userId) {

    }
    private void handleSearch() {
        String searchText = studentSearch.getText().toLowerCase().trim();
        List<String> searchWords = new ArrayList<>();
        if (searchText.startsWith("\"") && searchText.endsWith("\"") && searchText.length() > 2) {
            searchWords.add(searchText.substring(1, searchText.length() - 1));
        } else if (!searchText.equals("\"\"")) {
            searchWords.addAll(Arrays.asList(searchText.split("\\s+")));
        }
        List<StudentDTO> filteredList = allStudents.stream()
                .filter(student -> matchesSearchTerms(student, searchWords))
                .collect(Collectors.toList());

        studentsTable.setItems(FXCollections.observableArrayList(filteredList));
    }
    private boolean matchesSearchTerms(StudentDTO student, List<String> searchWords) {
        String studentText = student.toString().toLowerCase();
        return searchWords.stream().allMatch(studentText::contains);
    }
}
