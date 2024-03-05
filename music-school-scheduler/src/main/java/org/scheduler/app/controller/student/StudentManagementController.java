package org.scheduler.app.controller.student;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.scheduler.app.controller.base.StudentControllerBase;
import org.scheduler.app.controller.interfaces.*;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.dto.TeacherDTO;
import org.scheduler.data.dto.TeacherStudentInstrumentDTO;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.properties.LevelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.data.dto.StudentDTO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentManagementController extends StudentControllerBase implements ICreate, IUpdate, IDelete, IController, IExport {
    private static final Logger _logger = LoggerFactory.getLogger(StudentManagementController.class);
    public TextField firstNameTxt;
    public TextField addressLine1Txt;
    public TextField postalTxt;
    public TextField phoneTxt;

    public TextArea errorText;
    public TextField lastNameTxt;
    public TextField addressLine2Txt;
    public ComboBox<BookDTO> bookComboBox;
    public ComboBox<InstrumentDTO> instrumentComboBox;
    public ComboBox<LevelDTO> levelComboBox;
    public ComboBox<TeacherDTO> teacherComboBox;
    public CheckBox goldCupCheckBox;
    public TableColumn<StudentDTO, String> studentCurrentBookColumn;
    public TableColumn<StudentDTO, String> studentCurrentLevelColumn;
    public TableColumn<StudentDTO, String> studentIsGoldCupColumn;
    public Label lastNameLabel;
    public Label addressLine2Label;
    public Label postalCodeLabel;
    public Label phoneNumberLabel;
    public Label addressLine1Label;
    public Label firstNameLabel;
    public Label cityLabel;
    public TextField cityTxt;
    private int userId;

    public void setUser(int user) {
        userId = user;
    }

    /**
     * on cancel loads the main screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onGoBack(ActionEvent actionEvent) {
        try{
            super.goBack(userId);
        }
        catch (IOException e){
            _logger.error("Could not load resource!", e);
        }
    }

    /**
     * validates if required fields have values and adds customer to database
     * @param event
     */
    public void onAdd(ActionEvent event){
        String errorMessage = getErrorMessage();
        if(errorMessage != null) {
            errorText.setText(errorMessage);
            errorText.setVisible(true);
        }
        else {
            try {

                int studentId = studentsRepository.insertItem(
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
                studentsRepository.updateTeacherStudentInstrument(teacherStudentInstrument);
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
    public void onUpdate(ActionEvent actionEvent) throws IOException {

    }
    /**
     * prepopulates dropdown values and populates customer table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        super.initialize(url, resourceBundle);

        try {
            populateComboBox(bookComboBox, new ArrayList<>(bookRepository.getAllItems()));
            populateComboBox(instrumentComboBox, new ArrayList<>(instrumentRepository.getAllItems()));
            populateComboBox(levelComboBox, new ArrayList<>(levelRepository.getAllItems()));
            populateComboBox(teacherComboBox, new ArrayList<>(teachersRepository.getAllItems()));
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onExportToCSV(ActionEvent actionEvent) {
        
    }

    @Override
    public List<PossibleError> buildPossibleErrors() {
        List<PossibleError> possibleErrors = new ArrayList<>();

        possibleErrors.add(new PossibleError("firstName", firstNameTxt.getText(), firstNameLabel));
        possibleErrors.add(new PossibleError("lastName", lastNameTxt.getText(), lastNameLabel));
        possibleErrors.add(new PossibleError("addressLine1", addressLine1Txt.getText(), addressLine1Label));
        possibleErrors.add(new PossibleError("addressLine2", addressLine2Txt.getText(), addressLine2Label));
        possibleErrors.add(new PossibleError("city", cityTxt.getText(), cityLabel));
        possibleErrors.add(new PossibleError("postalCode", postalTxt.getText(), postalCodeLabel));
        possibleErrors.add(new PossibleError("phoneNumber", phoneTxt.getText(), phoneNumberLabel));

        return possibleErrors;
    }

    @Override
    public String getNameForItem(Object item) {
        if (item instanceof BookDTO) {
            return ((BookDTO)item).name();
        } else if (item instanceof InstrumentDTO) {
            return ((InstrumentDTO)item).instrumentName();
        } else if (item instanceof LevelDTO) {
            return ((LevelDTO)item).name();
        } else {
            return "";
        }
    }

}
