package org.scheduler.app.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.*;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.base.DTOMappingBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.mapping.*;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.properties.LevelDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.data.dto.StudentDTO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class StudentManagementController extends ControllerBase implements ICreate, IUpdate, IDelete, IController, IExport, ITableView<StudentDTO> {
    private static final Logger _logger = LoggerFactory.getLogger(StudentManagementController.class);
    public TableColumn<StudentDTO, String> studentNameColumn = new TableColumn<>();
    public TableColumn<StudentDTO, String> studentAddressColumn = new TableColumn<>();
    public TableColumn<StudentDTO, String> studentPhoneColumn = new TableColumn<>();
    public TableColumn<StudentDTO, String> studentBooksColumn;
    public TableColumn<StudentDTO, String> studentLevelsColumn;
    public TableColumn<StudentDTO, String> studentIsGoldCupColumn;
    public TableColumn<StudentDTO, String> studentInstrumentsColumn;
    public TableColumn<StudentDTO, String> studentTeachersColumn;
    public TableView<StudentDTO> studentsTable = new TableView<>();
    public TextField firstNameTxt;
    public TextField addressLine1Txt;
    public TextField postalTxt;
    public TextField phoneTxt;
    public TextField lastNameTxt;
    public TextField addressLine2Txt;
    public TextField cityTxt;
    public TextField emailTxt;
    public TextField stateTxt;
    public TextField studentSearch;
    public CheckBox goldCupCheckBox;
    public Label lastNameLabel;
    public Label addressLine2Label;
    public Label postalCodeLabel;
    public Label phoneNumberLabel;
    public Label addressLine1Label;
    public Label firstNameLabel;
    public Label cityLabel;
    public Label emailLabel;
    public Label stateLabel;

    private StudentDTO studentToBeUpdated;
    private ObservableList<StudentDTO> allStudents;


    /**
     * on cancel loads the main screen
     *
     * @param actionEvent
     */
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
        if(studentToBeUpdated != null){
            onUpdate(actionEvent);
        }
        else {
            onAdd(actionEvent);
        }
    }

    /**
     * validates if required fields have values and adds customer to database
     * @param event
     */
    public void onAdd(ActionEvent event){
        String errorMessage = getErrorMessage();
        if(!errorMessage.equals("")) {
            errorText.setText(errorMessage);
            errorText.setVisible(true);
        }
        else {
            try {

                StudentDTO newStudent = new StudentDTO(
                                firstNameTxt.getText(),
                                lastNameTxt.getText(),
                                addressLine1Txt.getText(),
                                addressLine2Txt.getText(),
                                postalTxt.getText(),
                                phoneTxt.getText(),
                                LocalDateTime.now(),
                                userId,
                                LocalDateTime.now(),
                                userId,
                                goldCupCheckBox.isSelected());

                studentsRepository.insertNewStudent(newStudent);
                reset();
                studentsTable.setItems(studentsRepository.getAllItems());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDelete(ActionEvent actionEvent) {
        StudentDTO studentDTO = studentsTable.getSelectionModel().getSelectedItem();

        try {
            studentsRepository.deleteStudent(studentDTO, JDBC.getConnection());
            reset();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        String errorMessage = super.getErrorMessage();
        if (!errorMessage.equals("")) {
            errorText.setText(errorMessage);
        } else {
            StudentDTO newStudent = new StudentDTO(
                    studentToBeUpdated.getId(),
                    firstNameTxt.getText(),
                    lastNameTxt.getText(),
                    addressLine1Txt.getText(),
                    addressLine2Txt.getText(),
                    postalTxt.getText(),
                    cityTxt.getText(),
                    stateTxt.getText(),
                    phoneTxt.getText(),
                    emailTxt.getText(),
                    LocalDateTime.now(),
                    userId,
                    goldCupCheckBox.isSelected());

            if (newStudent.getId() != 0) {
                try {
                    studentsRepository.updateStudent(newStudent);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                reset();
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
        reset();
        studentsTable.setRowFactory(tv -> {
            TableRow<StudentDTO> row = new TableRow<>();
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
        possibleErrors.add(new PossibleError("city", cityTxt.getText(), cityLabel));
        possibleErrors.add(new PossibleError("postalCode", postalTxt.getText(), postalCodeLabel));
        possibleErrors.add(new PossibleError("phoneNumber", phoneTxt.getText(), phoneNumberLabel));

        return possibleErrors;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    //region private methods
    @Override
    public void reset() {
        studentToBeUpdated = null;
        allStudents = studentsRepository.getAllStudentData();
        configureTableColumns();
        loadTableData();

        studentSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });
        labelsToReset.clear();
        Collections.addAll(labelsToReset, firstNameLabel, lastNameLabel, addressLine1Label, cityLabel, postalCodeLabel, phoneNumberLabel);
        super.reset();
    }

    private void configureTableColumns(){
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        studentAddressColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        studentPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        studentBooksColumn.setCellValueFactory(new PropertyValueFactory<>("studentBookNames"));
        studentLevelsColumn.setCellValueFactory(new PropertyValueFactory<>("studentLevelNames"));
        studentInstrumentsColumn.setCellValueFactory(new PropertyValueFactory<>("studentInstrumentNames"));
        studentTeachersColumn.setCellValueFactory(new PropertyValueFactory<>("studentTeacherNames"));
        studentIsGoldCupColumn.setCellValueFactory(new PropertyValueFactory<>("isGoldCup"));


        studentsTable.setRowFactory(tv -> {
            TableRow<StudentDTO> row = new TableRow<>();
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
    }

    private void loadTableData(){
        studentsTable.setItems(allStudents);
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

    @Override
    public void handleDoubleClickOnRow(StudentDTO item) throws SQLException, InstantiationException, IllegalAccessException {
        reset();
        firstNameTxt.setText(item.getFirstName());
        lastNameTxt.setText(item.getLastName());
        addressLine1Txt.setText(item.getAddressLine1());
        addressLine2Txt.setText(item.getAddressLine2());
        cityTxt.setText(item.getCity());
        stateTxt.setText(item.getState());
        postalTxt.setText(item.getPostalCode());
        phoneTxt.setText(item.getPhoneNumber());
        emailTxt.setText(item.getEmail());
        goldCupCheckBox.setSelected(item.isGoldCup());
        studentToBeUpdated = item;
    }
    private boolean matchesSearchTerms(StudentDTO student, List<String> searchWords) {
        String studentText = student.toString().toLowerCase();
        return searchWords.stream().allMatch(studentText::contains);
    }


//    private <T extends ISqlConvertible> void setupListViewListener(ListView<T> instrumentListView, List<T> selectionList){
//        instrumentListView.setCellFactory(CheckBoxListCell.forListView(item -> {
//            BooleanProperty property = new SimpleBooleanProperty();
//            property.addListener((obs, wasSelected, isNowSelected) -> {
//                if(isNowSelected){
//                    item.setSelected(true);
//                    selectionList.add(item);
//
//                }
//                if(wasSelected){
//                    item.setSelected(false);
//                    selectionList.remove(item);
//                }
//            });
//
//            return property;
//        }, new StringConverter<T>() {
//            @Override
//            public String toString(T object) {
//                return object.getName();
//            }
//
//            @Override
//            public T fromString(String string) {
//                return null;
//            }
//        }));
//    }


//    private <T extends ISqlConvertible, E extends BaseRepository> void populateListView(ListView<T> listView, E repo) {
//        listView.getItems().clear();
//
//        listView.setItems(repo.getAllItems());
//
//        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//
//        listView.setCellFactory(param -> new ListCell<T>() {
//            private final CheckBox checkBox = new CheckBox();
//
//            @Override
//            protected void updateItem(T item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item == null) {
//                    setText(null);
//                    setGraphic(null);
//                } else {
//                    setText(item.getName());
//
//                    checkBox.setSelected(item.getSelected().get());
//
//                    checkBox.setOnAction(e -> {
//                        item.setSelected(checkBox.isSelected());
//                    });
//
//                    setGraphic(checkBox);
//                }
//            }
//        });
//
//    }
    //endregion
}
