package org.scheduler.controller.student;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.scheduler.constants.Constants;
import org.scheduler.controller.MainScreenController;
import org.scheduler.controller.base.StudentControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.controller.interfaces.ICreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.viewmodels.Student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddStudentController extends StudentControllerBase implements ICreate, IController {
    private static final Logger _logger = LoggerFactory.getLogger(AddStudentController.class);

    public TextField idTxt;
    public TextField nameTxt;
    public TextField addressTxt;
    public TextField postalTxt;
    public TextField phoneTxt;

    public Label errorText;
    private int userId;

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
        super.goBack();
    }

    public void onExit(ActionEvent actionEvent) throws IOException{
        super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.MAIN_SCREEN, userId);
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
                            studentsDTO.getLastStudentId(),
                            nameTxt.getText(),
                            nameTxt.getText(),
                            addressTxt.getText(),
                            addressTxt.getText(),
                            postalTxt.getText(),
                            phoneTxt.getText());
                    studentsDTO.addStudent(student);
                    errorText.setText("Successfully added Student");
                    studentsTable.setItems(studentsDTO.getAllStudents());
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
        super.initialize(url, resourceBundle);
    }
}
