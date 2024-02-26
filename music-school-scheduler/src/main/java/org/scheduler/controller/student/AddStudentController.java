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
import org.scheduler.controller.interfaces.ICreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.StudentsDTO;
import org.scheduler.viewmodels.Student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddStudentController extends ControllerBase implements ICreate, IController {
    private static final Logger _logger = LoggerFactory.getLogger(AddStudentController.class);

    public TableView<Student> customersTable;

    public TableColumn<Student, String> customerNameColumn;

    public TableColumn<Student, String> customerAddressColumn;
    public TableColumn<Student, String> customerPhoneColumn;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField addressTxt;
    public TextField postalTxt;
    public TextField phoneTxt;

    public Label errorText;
    private int userId;
    private final StudentsDTO studentsDTO = new StudentsDTO();

    public void setUser(int user) {
        userId = user;
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

    public void onExit(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        Parent scene = loader.load();
        MainScreenController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * validates if required fields have values and adds customer to database
     * @param event
     */
    public void onAdd(ActionEvent event){
        boolean valid = true;
        while (valid) {
            if(nameTxt.getText() == ""){
                errorText.setText("Name field MUST be filled out");
                valid = false;
            }
            else if(addressTxt.getText() == ""){
                errorText.setText("Address field MUST be filled out");
                valid = false;
            }
            else if(postalTxt.getText() == "" && postalTxt.getText().matches("[0-9]{5}")){
                errorText.setText("Postal field MUST be filled out");
                valid = false;
            }
            else if(phoneTxt.getText() == "" && phoneTxt.getText().matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$")){
                errorText.setText("Phone field MUST be filled out");
                valid = false;
            }
            else{
                try {
                    Student student = new Student(
                            studentsDTO.getId(),
                            nameTxt.getText(),
                            addressTxt.getText(),
                            postalTxt.getText(),
                            phoneTxt.getText());
                    studentsDTO.addCustomer(student);
                    errorText.setText("Successfully added Student");
                    customersTable.setItems(studentsDTO.getAllCustomers());
                    onExit(event);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    valid = false;
                }
            }
        }
    }

    /**
     * prepopulates dropdown values and populates customer table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //Get data for Student Table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        //sets table with customer data
        customersTable.setItems(studentsDTO.getAllCustomers());

    }
}
