package org.scheduler.app.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.ReportDTO;
import org.scheduler.data.dto.StudentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;
import static java.time.Month.DECEMBER;

public class ReportsController extends ControllerBase implements IController {

    private static final Logger _logger = LoggerFactory.getLogger(ReportsController.class);
    @FXML
    private Label appointmentNumber;
    @FXML
    private Label totalAppointments;
    @FXML
    private ComboBox<String> contactBox;
    @FXML
    private TableColumn<StudentDTO, String> customerID;
    @FXML
    private TableColumn<StudentDTO, String> description;
    @FXML
    private TableColumn<StudentDTO, String> endTime;
    @FXML
    private TableColumn<StudentDTO, String> id;
    @FXML
    private ComboBox<String> months;
    @FXML
    private TableColumn<LessonDTO, String> startDate;
    @FXML
    private TableColumn<LessonDTO, String> startTime;
    @FXML
    private TableView<LessonDTO> table;
    @FXML
    private TableColumn<LessonDTO, String> title;
    @FXML
    private TableColumn<LessonDTO, String> type;
    @FXML
    private ComboBox<String> types;
    @FXML
    private ComboBox<String> years;
    @FXML
    private ComboBox<String> report;

    // --Commented out by Inspection (2/21/2024 12:14 AM):private final ObservableList<String> reportsAvailable = FXCollections.observableArrayList();
    private int userId;
    private Parent scene;
    private final ObservableList<String> monthsList = FXCollections.observableArrayList();
    private final ObservableList<String> yearsList = FXCollections.observableArrayList();
    private ObservableList<LessonDTO> lessonDTOS = FXCollections.observableArrayList();
    private final ObservableList<String> reportsList = FXCollections.observableArrayList();
    private final ObservableList<ReportDTO> reportDTOS = FXCollections.observableArrayList();

    /**
     * Reloads the screen
     * @param actionEvent
     * @throws IOException
     */
    public void onReset(ActionEvent actionEvent) throws IOException {
        appointmentNumber.setText("0");
        super.loadNewScreen(Constants.FXML_ROUTES.MAIN_SCREEN, userId);
    }

    /**
     * Based on user selection, a report is run and results are shown on the right side of the filters
     * @param actionEvent
     */
    public void onSearch(ActionEvent actionEvent) {
        if (report.getValue().equalsIgnoreCase(reportDTOS.get(0).getReportName())) {
            if((types.getValue() == null) || (years.getValue() == null) || (months.getValue() == null) || (report.getValue() == null)
                    || (contactBox.getValue() == null)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("You must select a value in every combo box");
                alert.showAndWait();
                return;
            }
            String contact = contactBox.getValue();
            lessonDTOS = lessonsRepository.getStudentLessonsByName(contact);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
        }

        if (report.getValue().equalsIgnoreCase(reportDTOS.get(1).getReportName())){
            totalAppointments.setVisible(true);
            appointmentNumber.setVisible(true);
        }
        if (report.getValue().equalsIgnoreCase(reportDTOS.get(2).getReportName())){
            table.setDisable(false);
            if((types.getValue() == null) || (years.getValue() == null) || (months.getValue() == null) || (report.getValue() == null)
                    || (contactBox.getValue() == null)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("You must select a value in every combo box");
                alert.showAndWait();
                return;
            }
            String contact = contactBox.getValue();
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
        }
        String apptType = types.getValue();
        int year = parseInt(years.getValue());
        int yearNumber = 2024;
        while (yearNumber >= 2022){
            if(year == yearNumber){
                break;
            }
            else{
                --yearNumber;
            }
        }

        Month month = DECEMBER;
        int i = 12;
        while (i >= 1){
            if(month.toString().equalsIgnoreCase(months.getValue())){
                break;
            }
            else{
                --i;
                month = month.minus(1);
            }
        }

        LocalDateTime localDateTime = LocalDateTime.of(yearNumber,i,1,1,1);
        int x = lessonsRepository.getMonthTypeAsInt(localDateTime,apptType);
        String y = String.valueOf(x);
        appointmentNumber.setText(y);

        if (report.getValue().equalsIgnoreCase(reportDTOS.get(0).getReportName())){
            id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));

            table.setItems(lessonDTOS);
        }//TODO possible implementation - add additional if statements for each report and how table is manipulated accordingly
        else if (report.getValue().equalsIgnoreCase(reportDTOS.get(2).getReportName())) {
            id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            table.setItems(lessonDTOS);
        }
        else {
            //do nothing in case of else
        }

    }

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

    /**
     * Enables, disables, or removes fields that are not applicable based on report chosen
     * @param event
     */
    @FXML
    public void onChooseReport(ActionEvent event) {
        if (report.getValue().equalsIgnoreCase(reportDTOS.get(0).getReportName())){
            contactBox.setDisable(false);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
            table.setDisable(false);
        }
        if (report.getValue().equalsIgnoreCase(reportDTOS.get(1).getReportName())){
            contactBox.setDisable(true);
            totalAppointments.setVisible(true);
            appointmentNumber.setVisible(true);
            table.setDisable(true);
        }
        if (report.getValue().equalsIgnoreCase(reportDTOS.get(2).getReportName())){
            contactBox.setDisable(false);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
            table.setDisable(false);
        }
    }

    /**
     * loads all preset data into screen
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthsList.clear();
        yearsList.clear();
        try {//TODO Add reportDTOS to db and make this a getter call for all reportDTOS available
            ReportDTO addReportDTO = new ReportDTO("LessonDTO Schedule", 1);
            reportDTOS.add(addReportDTO);
            addReportDTO = new ReportDTO("Total StudentDTO LessonsRepository by Type", 2);
            reportDTOS.add(addReportDTO);
            addReportDTO = new ReportDTO("LessonDTO Schedule by Division", 3);
            reportDTOS.add(addReportDTO);
            int index = 0;
            while (index < reportDTOS.size()){
                reportsList.add(reportDTOS.get(index).getReportName());
                index++;
            }
            report.setItems(reportsList);
        }catch (Exception e){
            _logger.error("Error: " + e);
        }

        monthsList.add("January");monthsList.add("February");monthsList.add("March");monthsList.add("April");monthsList.add("May");monthsList.add("June");monthsList.add("July");monthsList.add("August");monthsList.add("September");monthsList.add("October");monthsList.add("November");monthsList.add("December");
        yearsList.add("2022");yearsList.add("2023");yearsList.add("2024");
        months.setItems(monthsList);
        years.setItems(yearsList);
        types.setItems(lessonsRepository.getTypes());
        int index = 0;
        ObservableList<StudentDTO> studentDTOS = null;
        try {
            studentDTOS = studentsRepository.getAllItems();
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throw new RuntimeException(throwables);
        }
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        while (index < studentDTOS.size()){
            String contactName = studentDTOS.get(index).getFullName();
            contactNames.add(contactName);
            ++index;
        }
        contactBox.setItems(contactNames);
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
}
