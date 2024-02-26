package org.scheduler.controller.student;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.scheduler.controller.MainScreenController;
import org.scheduler.controller.base.ControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.controller.interfaces.IUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.StudentsDTO;
import org.scheduler.viewmodels.Student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyStudentController extends ControllerBase implements IUpdate, IController {
    private static final Logger _logger = LoggerFactory.getLogger(ModifyStudentController.class);
    public TableColumn<Student, String> customerNameColumn;
    public TableColumn<Student, String> customerAddressColumn;
    public TableColumn<Student, String> customerPhoneColumn;
    public TableView<Student> customersTable;
    public Student selectedStudent;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField addressTxt;
    public TextField postalTxt;
    public Label division;
    public TextField phoneTxt;

    public Label errorText;
    private Parent scene;
    private int customerId;
    private int userId;
    private final StudentsDTO studentsDTO = new StudentsDTO();

    /**
     * prepopulates dropdown values and populates customer table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Get data for Student Table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        //sets table with customer data
        customersTable.setItems(studentsDTO.getAllCustomers());

    }

    /**
     * @param selectedStudent
     */
    public void setCustomer(Student selectedStudent) {

        this.selectedStudent = selectedStudent;
        customerId = selectedStudent.getId();
        idTxt.setText(Integer.toString(selectedStudent.getId()));
        nameTxt.setText(selectedStudent.getName());
        addressTxt.setText(selectedStudent.getAddress());
        postalTxt.setText(selectedStudent.getPostalCode());
        phoneTxt.setText(selectedStudent.getPhoneNumber());
    }

    /**
     * on cancel loads the main screen
     *
     * @param event
     * @throws IOException
     */
    public void onCancel(ActionEvent event) {
        _primaryStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        goBack();
    }

    /**
     * validates if required fields have values and updates customer in the database
     * @param actionEvent
     */
    public void onUpdate(ActionEvent actionEvent) {
        boolean valid = true;
        while (valid){
            if(nameTxt.getText() == ""){
                errorText.setText("Name field MUST be filled out");
                valid = false;
            }
            else if(addressTxt.getText() == ""){
                errorText.setText("Address field MUST be filled out");
                valid = false;
            }
            else if(postalTxt.getText() == ""){
                errorText.setText("Postal field MUST be filled out");
                valid = false;
            }
            else if(phoneTxt.getText() == ""){
                errorText.setText("Phone field MUST be filled out");
                valid = false;
            }
            else {
                try {
                    Student student = new Student(customerId,nameTxt.getText(),addressTxt.getText(),postalTxt.getText(),phoneTxt.getText());
                    studentsDTO.updateCustomer(student);
                    customersTable.setItems(studentsDTO.getAllCustomers());
                    valid = false;
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
                    scene = loader.load();
                    MainScreenController controller = loader.getController();
                    controller.setUser(userId);
                    stage.setScene(new Scene(scene));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    valid = false;
                }
            }
        }
    }

    /**
     * sets the userId
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}