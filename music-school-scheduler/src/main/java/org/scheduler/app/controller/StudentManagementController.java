package org.scheduler.app.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import org.scheduler.data.dto.TeacherDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static org.scheduler.app.constants.Constants.PRIMARY_STAGE;

public class StudentManagementController extends ControllerBase implements ICreate, IUpdate, IDelete, IController, IExport {
    private static final Logger _logger = LoggerFactory.getLogger(StudentManagementController.class);
    public TableColumn<StudentDTO, String> studentNameColumn = new TableColumn<>();
    public TableColumn<StudentDTO, String> studentAddressColumn = new TableColumn<>();
    public TableColumn<StudentDTO, String> studentPhoneColumn = new TableColumn<>();
    public TableView<StudentDTO> studentsTable = new TableView<>();
    public TextField firstNameTxt;
    public TextField addressLine1Txt;
    public TextField postalTxt;
    public TextField phoneTxt;

    public TextArea errorText;
    public TextField lastNameTxt;
    public TextField addressLine2Txt;
    public CheckBox goldCupCheckBox;
    public TableColumn<StudentDTO, String> studentBooksColumn;
    public TableColumn<StudentDTO, String> studentLevelsColumn;
    public TableColumn<StudentDTO, String> studentIsGoldCupColumn;
    public TableColumn<StudentDTO, String> studentInstrumentsColumn;
    public TableColumn<StudentDTO, String> studentTeachersColumn;
    public Label lastNameLabel;
    public Label addressLine2Label;
    public Label postalCodeLabel;
    public Label phoneNumberLabel;
    public Label addressLine1Label;
    public Label firstNameLabel;
    public Label cityLabel;
    public TextField cityTxt;
    public Label emailLabel;
    public TextField emailTxt;
    public Label stateLabel;
    public TextField stateTxt;
    private int userId;
    private StudentDTO studentToBeUpdated;
    public ListView<BookDTO> bookListView;
    private final List<BookDTO> selectedBooks = new ArrayList<>();
    public ListView<InstrumentDTO> instrumentListView;
    private final List<InstrumentDTO> selectedInstruments = new ArrayList<>();
    public ListView<LevelDTO> levelListView;
    private final List<LevelDTO> selectedLevels = new ArrayList<>();
    public ListView<TeacherDTO> teacherListView;
    private final List<TeacherDTO> selectedTeachers = new ArrayList<>();


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

                studentsRepository.insertNewStudent(newStudent, teacherActionHandler.getTeacherList(), instrumentActionHandler.getInstrumentList(),
                                                    bookActionHandler.getBookList(), levelActionHandler.getLevelList());
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
                List<InstrumentDTO> instruments = instrumentListView.getItems().stream().toList();
                List<TeacherDTO> teachers = teacherListView.getItems().stream().toList();
                List<BookDTO> books = bookListView.getItems().stream().toList();
                List<LevelDTO> levels = levelListView.getItems().stream().toList();
                try {
                    studentsRepository.updateStudent(newStudent,teachers, instruments, books, levels);
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
        super.populateListView(bookListView, bookRepository);
        super.setUpListener(bookListView, bookActionHandler);

        super.populateListView(levelListView, levelRepository);
        super.setUpListener(levelListView, levelActionHandler);

        super.populateListView(teacherListView, teachersRepository);
        super.setUpListener(teacherListView, teacherActionHandler);

        super.populateListView(instrumentListView, instrumentRepository);
        super.setUpListener(instrumentListView, instrumentActionHandler);

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

//        possibleErrors.add(new PossibleError("firstName", firstNameTxt.getText(), firstNameLabel));
//        possibleErrors.add(new PossibleError("lastName", lastNameTxt.getText(), lastNameLabel));
//        possibleErrors.add(new PossibleError("addressLine1", addressLine1Txt.getText(), addressLine1Label));
//        possibleErrors.add(new PossibleError("city", cityTxt.getText(), cityLabel));
//        possibleErrors.add(new PossibleError("postalCode", postalTxt.getText(), postalCodeLabel));
//        possibleErrors.add(new PossibleError("phoneNumber", phoneTxt.getText(), phoneNumberLabel));

        return new ArrayList<>();
    }

    @Override
    public String getNameForItem(Object item) {
        if (item instanceof BookDTO) {
            return ((BookDTO)item).getName();
        } else if (item instanceof InstrumentDTO) {
            return ((InstrumentDTO)item).getName();
        } else if (item instanceof LevelDTO) {
            return ((LevelDTO)item).getName();
        } else {
            return "";
        }
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    //region private methods
    private void reset() {
        clearAllTextFields(PRIMARY_STAGE.getScene().getRoot());
        goldCupCheckBox.setSelected(false);
        studentToBeUpdated = null;
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentAddressColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        studentPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        studentBooksColumn.setCellValueFactory(new PropertyValueFactory<>("studentBooks"));
        studentLevelsColumn.setCellValueFactory(new PropertyValueFactory<>("studentLevels"));
        studentInstrumentsColumn.setCellValueFactory(new PropertyValueFactory<>("studentInstruments"));
        studentTeachersColumn.setCellValueFactory(new PropertyValueFactory<>("studentTeachers"));
        studentIsGoldCupColumn.setCellValueFactory(new PropertyValueFactory<>("isGoldCup"));

        try {
            studentsTable.setItems(studentsRepository.getAllItems());
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throw new RuntimeException(throwables);
        }

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
        setupListViewListener(instrumentListView, selectedInstruments);
        setupListViewListener(bookListView, selectedBooks);
        setupListViewListener(levelListView, selectedLevels);
        setupListViewListener(teacherListView, selectedTeachers);
        populateListView(instrumentListView, instrumentRepository);
        populateListView(bookListView, bookRepository);
        populateListView(levelListView, levelRepository);
        populateListView(teacherListView, teachersRepository);
    }

    private <T extends ISqlConvertible> void setupListViewListener(ListView<T> instrumentListView, List<T> selectionList){
        instrumentListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty property = new SimpleBooleanProperty();
            property.addListener((obs, wasSelected, isNowSelected) -> {
                if(isNowSelected){
                    item.setSelected(true);
//                    instrumentActionHandler.performConcreteListAction(true, item);
                    selectionList.add(item);

                }
                if(wasSelected){
                    item.setSelected(false);
//                    instrumentActionHandler.performConcreteListAction(false, item);
                    selectionList.remove(item);
                }
            });

            return property;
        }, new StringConverter<T>() {
            @Override
            public String toString(T object) {
                return object.getName();
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        }));
    }

    private void handleDoubleClickOnRow(StudentDTO item) throws SQLException, InstantiationException, IllegalAccessException {
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

        List<DTOMappingBase> studentInstruments = studentsRepository.getIdsForStudentProperties(item.getId());

        instrumentListView.getItems().forEach(instrument -> {
            for (DTOMappingBase studentMapping : studentInstruments) {
                if (studentMapping instanceof StudentInstrumentDTO && instrument.getId() == studentMapping.getMappingToId()) {
                    instrument.setSelected(true);
                }
            }
        });
        bookListView.getItems().forEach(book -> {
            for (DTOMappingBase studentMapping : studentInstruments) {
                if (studentMapping instanceof StudentBookDTO && book.getId() == studentMapping.getMappingToId()) {
                    book.setSelected(true);
                }
            }
        });

        levelListView.getItems().forEach(level -> {
            for (DTOMappingBase studentMapping : studentInstruments) {
                if (studentMapping instanceof StudentLevelDTO && level.getId() == studentMapping.getMappingToId()) {
                    level.setSelected(true);
                }
            }
        });

        teacherListView.getItems().forEach(teacher -> {
            for (DTOMappingBase studentMapping : studentInstruments) {
                if (studentMapping instanceof StudentTeacherDTO && teacher.getId() == studentMapping.getMappingToId()) {
                    teacher.setSelected(true);
                }
            }
        });
    }

    private <T extends ISqlConvertible, E extends BaseRepository> void populateListView(ListView<T> listView, E repo) {
        try {
            listView.getItems().clear();

            listView.setItems(repo.getAllItems());

            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            listView.setCellFactory(param -> new ListCell<T>() {
                private final CheckBox checkBox = new CheckBox();

                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getName());

                        checkBox.setSelected(item.getSelected().get());

                        checkBox.setOnAction(e -> {
                            item.setSelected(checkBox.isSelected());
                        });

                        setGraphic(checkBox);
                    }
                }
            });

        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    //endregion
}
