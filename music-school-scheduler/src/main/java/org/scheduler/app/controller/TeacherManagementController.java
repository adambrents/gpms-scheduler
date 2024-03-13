package org.scheduler.app.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.handlers.base.IActionHandler;
import org.scheduler.app.controller.interfaces.*;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.TeacherDTO;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.mapping.TeacherInstrumentDTO;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.repository.interfaces.IRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.scheduler.app.constants.Constants.PRIMARY_STAGE;

public class TeacherManagementController extends ControllerBase implements IController, ICreate, IDelete, IUpdate, IExport {
    private static final Logger _logger = LoggerFactory.getLogger(TeacherManagementController.class);
    public Label lastNameLabel;
    public Label addressLine2Label;
    public Label postalCodeLabel;
    public Label phoneNumberLabel;
    public Label addressLine1Label;
    public Label firstNameLabel;
    public Label cityLabel;
    public Label emailLabel;
    public TextField firstNameTxt;
    public TextField lastNameTxt;
    public TextField addressLine1Txt;
    public TextField addressLine2Txt;
    public TextField cityTxt;
    public TextField postalTxt;
    public TextField phoneTxt;
    public TextArea errorText;
    public TextField emailTxt;
    public TableView<TeacherDTO> teachersTable;
    public TableColumn<TeacherDTO, String> teacherNameColumn;
    public TableColumn<TeacherDTO, String> teacherAddressColumn;
    public TableColumn<TeacherDTO, String> teacherPhoneColumn;
    public TableColumn<TeacherDTO, String> teacherEmailColumn;
    public TableColumn<TeacherDTO, Integer> teacherNumberStudentsColumn;
    public TableColumn<TeacherDTO, String> teacherInstrumentsColumn;
    public Label instrumentsLabel;
    public ListView<InstrumentDTO> instrumentListView;
    public List<InstrumentDTO> selectedInstruments = new ArrayList<>();
    public Label stateLabel;
    public TextField stateTxt;
    private int userId;
    private TeacherDTO teacherToBeUpdated;

    @Override
    public List<PossibleError> buildPossibleErrors() {
        List<PossibleError> possibleErrors = new ArrayList<>();
        possibleErrors.add(new PossibleError("First Name", firstNameTxt.getText(), firstNameLabel));
        possibleErrors.add(new PossibleError("Last Name", lastNameTxt.getText(), lastNameLabel));
        possibleErrors.add(new PossibleError("Phone", phoneTxt.getText(), phoneNumberLabel));
        possibleErrors.add(new PossibleError("Email", emailTxt.getText(), emailLabel));

        return possibleErrors;
    }

    @Override
    public String getNameForItem(Object item) {
        if (item instanceof InstrumentDTO) {
            return ((InstrumentDTO)item).getName();
        }
        else {
            return "";
        }
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            reset();
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void onSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if(teacherToBeUpdated != null){
            onUpdate(actionEvent);
        }
        else {
            onAdd(actionEvent);
        }
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
        if(!errorMessage.equals("")) {
            errorText.setText(errorMessage);
            errorText.setVisible(true);
        }
        else {
            try {

                TeacherDTO newTeacher = new TeacherDTO(
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
                        LocalDateTime.now(),
                        userId);

                List<InstrumentDTO> selectedItems = instrumentListView.getItems().stream()
                        .filter(instrumentDTO -> instrumentDTO.getSelected().get())
                        .toList();
//                teachersRepository.insertNewTeacher(newTeacher, instrumentActionHandler.getInstrumentList());
                teachersRepository.insertNewTeacher(newTeacher, selectedItems);
                reset();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reset() throws SQLException, InstantiationException, IllegalAccessException {
        clearAllTextFields(PRIMARY_STAGE.getScene().getRoot());
        teacherToBeUpdated = null;
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        teacherAddressColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        teacherPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        teacherEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        teacherNumberStudentsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfStudents"));
        teacherInstrumentsColumn.setCellValueFactory(new PropertyValueFactory<>("teacherInstruments"));

        teachersTable.setItems(teachersRepository.getAllItems());

//        super.populateListView(instrumentListView, instrumentRepository);
//
//        super.setUpListener(instrumentListView, instrumentActionHandler);

        teachersTable.setRowFactory(tv -> {
            TableRow<TeacherDTO> row = new TableRow<>();
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
        setUpInstrumentListener(instrumentListView);
        populateListView(instrumentListView);
    }
    public void populateListView(ListView<InstrumentDTO> listView) {
        try {
            listView.getItems().clear();

            listView.setItems(instrumentRepository.getAllItems());

            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            listView.setCellFactory(param -> new ListCell<InstrumentDTO>() {
                private final CheckBox checkBox = new CheckBox();

                @Override
                protected void updateItem(InstrumentDTO item, boolean empty) {
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
    private void setUpInstrumentListener(ListView<InstrumentDTO> instrumentListView){
        instrumentListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty property = new SimpleBooleanProperty();
            property.addListener((obs, wasSelected, isNowSelected) -> {
                if(isNowSelected){
                    item.setSelected(true);
//                    instrumentActionHandler.performConcreteListAction(true, item);
                    selectedInstruments.add(item);

                }
                if(wasSelected){
                    item.setSelected(false);
//                    instrumentActionHandler.performConcreteListAction(false, item);
                    selectedInstruments.remove(item);
                }
            });

            return property;
        }, new StringConverter<InstrumentDTO>() {
            @Override
            public String toString(InstrumentDTO object) {
                return object.getName();
            }

            @Override
            public InstrumentDTO fromString(String string) {
                return null;
            }
        }));
    }
    private void handleDoubleClickOnRow(TeacherDTO item) throws SQLException, InstantiationException, IllegalAccessException {
        firstNameTxt.setText(item.getFirstName());
        lastNameTxt.setText(item.getLastName());
        addressLine1Txt.setText(item.getAddressLine1());
        addressLine2Txt.setText(item.getAddressLine2());
        cityTxt.setText(item.getCity());
        stateTxt.setText(item.getState());
        postalTxt.setText(item.getPostalCode());
        phoneTxt.setText(item.getPhoneNumber());
        emailTxt.setText(item.getEmail());
        teacherToBeUpdated = item;
        List<TeacherInstrumentDTO> teacherMappings = teachersRepository.getAllTeacherInstrumentMappingsByTeacherId(item.getId());
        instrumentListView.getItems().forEach(instrument -> {
            for (TeacherInstrumentDTO teacherMapping : teacherMappings) {
                if (instrument.getId() == teacherMapping.getMappingToId()) {
                    instrument.setSelected(true);
                }
            }
        });
        instrumentListView.refresh();
    }

    @Override
    public void onDelete(ActionEvent actionEvent) {
        TeacherDTO teacherDTO = teachersTable.getSelectionModel().getSelectedItem();

        try {
            teachersRepository.deleteItem(teacherDTO, JDBC.getConnection());
            reset();
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onExportToCSV(ActionEvent actionEvent) {

    }

    @Override
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        String errorMessage = super.getErrorMessage();
        if(!errorMessage.equals("")){
            errorText.setText(errorMessage);
        }
        else {
            TeacherDTO newTeacher = new TeacherDTO(
                    teacherToBeUpdated.getId(),
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
                    userId);
            if(newTeacher.getId() != 0){
                try {
                    List<InstrumentDTO> selectedItems = instrumentListView.getItems().stream()
                            .filter(instrumentDTO -> instrumentDTO.getSelected().get())
                            .toList();
//                    teachersRepository.updateTeacher(newTeacher, instrumentListView.getItems());
                    teachersRepository.updateTeacher(newTeacher, selectedItems);
                    reset();
                } catch (SQLException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
