package org.scheduler.app.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.*;
import org.scheduler.app.controller.student.StudentManagementController;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.dto.StudentDTO;
import org.scheduler.data.dto.TeacherStudentInstrumentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class TeacherManagementController extends ControllerBase implements IController, ICreate, IDelete, IUpdate, IExport {
    private static final Logger _logger = LoggerFactory.getLogger(TeacherManagementController.class);
    public Label lastNameLabel;
    public Label addressLine2Label;
    public Label postalCodeLabel;
    public Label phoneNumberLabel;
    public Label addressLine1Label;
    public Label firstNameLabel;
    public Label cityLabel;
    public TextField firstNameTxt;
    public TextField lastNameTxt;
    public TextField addressLine1Txt;
    public TextField addressLine2Txt;
    public TextField cityTxt;
    public TextField postalTxt;
    public TextField phoneTxt;
    public TextArea errorText;
    private int userId;
    @Override
    public List<PossibleError> buildPossibleErrors() {
        return null;
    }

    @Override
    public String getNameForItem(Object item) {
        return null;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void onGoBack(ActionEvent event) {
        try{
            super.goBack(userId);
        }
        catch (IOException e){
            _logger.error("Could not load resource!", e);
        }
    }

    @Override
    public void onAdd(ActionEvent event) throws IOException {
        String errorMessage = getErrorMessage();
        if(errorMessage != null) {
            errorText.setText(errorMessage);
            errorText.setVisible(true);
        }
        else {
            try {

                int teacherId = studentsRepository.insertItem(
                        new StudentDTO(
                                firstNameTxt.getText(),
                                firstNameTxt.getText(),
                                addressLine1Txt.getText(),
                                addressLine2Txt.getText(),
                                postalTxt.getText(),
                                phoneTxt.getText(),
                                LocalDateTime.now(),
                                userId,
                                LocalDateTime.now(),
                                userId,
                                bookComboBox.getSelectionModel().getSelectedItem().bookId(),
                                levelComboBox.getSelectionModel().getSelectedItem().levelId(),
                                goldCupCheckBox.isSelected()));

                TeacherStudentInstrumentDTO teacherStudentInstrument = new TeacherStudentInstrumentDTO(
                        teacherComboBox.getSelectionModel().getSelectedItem().getTeacherId(),
                        studentId,
                        instrumentComboBox.getSelectionModel().getSelectedItem().instrumentId()
                );
                studentsRepository.insertTeacherStudentInstrument(teacherStudentInstrument);
                studentsTable.setItems(studentsRepository.getAllItems());
                onGoBack(event);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDelete(ActionEvent actionEvent) {

    }

    @Override
    public void onExportToCSV(ActionEvent actionEvent) {

    }

    @Override
    public void onUpdate(ActionEvent actionEvent) throws IOException {

    }
}
