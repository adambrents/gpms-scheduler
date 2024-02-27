package org.scheduler.controller.student;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.scheduler.constants.Constants;
import org.scheduler.controller.base.StudentControllerBase;
import org.scheduler.controller.interfaces.IController;
import org.scheduler.controller.interfaces.IUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.repository.StudentsRepository;
import org.scheduler.dto.StudentDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyStudentController extends StudentControllerBase implements IUpdate, IController {
    private static final Logger _logger = LoggerFactory.getLogger(ModifyStudentController.class);
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
    private final StudentsRepository studentsRepository = new StudentsRepository();

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
                    StudentDTO studentDTO = new StudentDTO(
                            customerId,
                            nameTxt.getText(),
                            nameTxt.getText(),//TODO add lastname
                            addressTxt.getText(),
                            addressTxt.getText(),//TODO add address 2
                            postalTxt.getText(),
                            phoneTxt.getText());
                    studentsRepository.updateItem(studentDTO);
                    studentsTable.setItems(studentsRepository.getAllItems());
                    valid = false;
                    super.loadNewScreen(actionEvent, Constants.FXML_ROUTES.MAIN_SCREEN, userId);
                } catch (Exception e) {
                    e.printStackTrace();
                    valid = false;
                }
            }
        }
    }

    /**
     * sets the userId
     */
    private void setUserId() {
        this.userId = _userId;
    }

    /**
     * sets the customer on load
     */
    private void setStudent() {
        customerId = _studentDTOToBeModified.getId();
        idTxt.setText(Integer.toString(_studentDTOToBeModified.getId()));
        nameTxt.setText(_studentDTOToBeModified.getFirstName());
        addressTxt.setText(_studentDTOToBeModified.getAddressLine1());
        postalTxt.setText(_studentDTOToBeModified.getPostalCode());
        phoneTxt.setText(_studentDTOToBeModified.getPhoneNumber());
    }
    @Override
    public void setControllerProperties(Object data) {
        super.setControllerProperties(data);
        if (data instanceof StudentDTO) {
            this._studentDTOToBeModified = (StudentDTO) data;
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