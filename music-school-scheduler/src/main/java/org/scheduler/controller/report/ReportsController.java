package org.scheduler.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.Contacts;
import org.scheduler.repository.LessonsDTO;
import org.scheduler.viewmodels.Contact;
import org.scheduler.viewmodels.Lesson;
import org.scheduler.viewmodels.Report;
import org.scheduler.viewmodels.Student;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
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
    private TableColumn<Student, String> customerID;
    @FXML
    private TableColumn<Student, String> description;
    @FXML
    private TableColumn<Student, String> endTime;
    @FXML
    private TableColumn<Student, String> id;
    @FXML
    private ComboBox<String> months;
    @FXML
    private TableColumn<Lesson, String> startDate;
    @FXML
    private TableColumn<Lesson, String> startTime;
    @FXML
    private TableView<Lesson> table;
    @FXML
    private TableColumn<Lesson, String> title;
    @FXML
    private TableColumn<Lesson, String> type;
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
    private ObservableList<Lesson> lessons = FXCollections.observableArrayList();
    private final ObservableList<String> reportsList = FXCollections.observableArrayList();
    private final ObservableList<Report> reports = FXCollections.observableArrayList();

    private final LessonsDTO lessonsDTO = new LessonsDTO();

    /**
     * Reloads the screen
     * @param event
     * @throws IOException
     */
    public void onReset(ActionEvent event) throws IOException {
        appointmentNumber.setText("0");
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        scene = loader.load();
        ReportsController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Based on user selection, a report is run and results are shown on the right side of the filters
     * @param actionEvent
     */
    public void onSearch(ActionEvent actionEvent) {
        if (report.getValue().equalsIgnoreCase(reports.get(0).getReportName())) {
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
            lessons = lessonsDTO.getContactLessons(contact);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
        }

        if (report.getValue().equalsIgnoreCase(reports.get(1).getReportName())){
            totalAppointments.setVisible(true);
            appointmentNumber.setVisible(true);
        }
        if (report.getValue().equalsIgnoreCase(reports.get(2).getReportName())){
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
        int x = lessonsDTO.getMonthTypeAsInt(localDateTime,apptType);
        String y = String.valueOf(x);
        appointmentNumber.setText(y);

        if (report.getValue().equalsIgnoreCase(reports.get(0).getReportName())){
            id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));

            table.setItems(lessons);
        }//TODO possible implementation - add additional if statements for each report and how table is manipulated accordingly
        else if (report.getValue().equalsIgnoreCase(reports.get(2).getReportName())) {
            id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            table.setItems(lessons);
        }
        else {
            //do nothing in case of else
        }

    }

    /**
     * Loads mainscreen
     * @param event
     * @throws IOException
     */
    @Override
    public void onCancel(ActionEvent event) {
        _primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        goBack();
    }

    /**
     * Enables, disables, or removes fields that are not applicable based on report chosen
     * @param event
     */
    @FXML
    public void onChooseReport(ActionEvent event) {
        if (report.getValue().equalsIgnoreCase(reports.get(0).getReportName())){
            contactBox.setDisable(false);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
            table.setDisable(false);
        }
        if (report.getValue().equalsIgnoreCase(reports.get(1).getReportName())){
            contactBox.setDisable(true);
            totalAppointments.setVisible(true);
            appointmentNumber.setVisible(true);
            table.setDisable(true);
        }
        if (report.getValue().equalsIgnoreCase(reports.get(2).getReportName())){
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
        try {//TODO Add reports to db and make this a getter call for all reports available
            Report addReport = new Report("Lesson Schedule", 1);
            reports.add(addReport);
            addReport = new Report("Total Student LessonsDTO by Type", 2);
            reports.add(addReport);
            addReport = new Report("Lesson Schedule by Division", 3);
            reports.add(addReport);
            int index = 0;
            while (index < reports.size()){
                reportsList.add(reports.get(index).getReportName());
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
        types.setItems(lessonsDTO.getTypes());
        int index = 0;
        ObservableList<Contact> contacts = Contacts.getAllContacts();
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        while (index < contacts.size()){
            String contactName = contacts.get(index).getContactName();
            contactNames.add(contactName);
            ++index;
        }
        contactBox.setItems(contactNames);
    }

}
