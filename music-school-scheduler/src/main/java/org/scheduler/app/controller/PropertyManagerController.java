package org.scheduler.app.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.ICreate;
import org.scheduler.app.controller.interfaces.IDelete;
import org.scheduler.app.controller.interfaces.IUpdate;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.dto.properties.PropertyDTO;
import org.scheduler.data.repository.properties.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import static org.scheduler.app.constants.Constants.ERROR_RED;
import static org.scheduler.app.constants.Constants.PROPERTIES.*;

public class PropertyManagerController extends ControllerBase implements ICreate, IUpdate, IDelete {
    private static final Logger _logger = LoggerFactory.getLogger(PropertyManagerController.class);
    private final PropertyRepository propertyRepository = new PropertyRepository();

    public Label errorText;
    public MenuButton typeMenu;
    public TextField nameTextField;
    public TableView<PropertyDTO> propertyTable;
    public TableColumn<PropertyDTO, String> propertyTypeNameColumn;
    public TableColumn<PropertyDTO, String> propertyNameColumn;
    public Label propertyTypeLabel;
    public Label nameTextLabel;
    private String selectedPropertyType;
//    private String _selectedPropertyType;

    @Override
    public void onGoBack(ActionEvent actionEvent) {
        try{
            super.goBack(_userId);
        }
        catch (IOException e){
            _logger.error("Could not load resource!", e);
        }
    }

    @Override
    public void onAdd(ActionEvent actionEvent) throws IOException {
        String errorMessage = super.getErrorMessage();
        if(errorMessage != null){
            errorText.setText(errorMessage);
            errorText.setTextFill(Paint.valueOf(ERROR_RED));
        }
        else {
            PropertyDTO newProperty = new PropertyDTO(selectedPropertyType, nameTextField.getText());
            performAction(Constants.CRUD.CREATE, newProperty);
        }
    }

    @Override
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        String errorMessage = super.getErrorMessage();
        if(errorMessage != null){
            errorText.setText(errorMessage);
            errorText.setTextFill(Paint.valueOf(ERROR_RED));
        }
        else {
            PropertyDTO newProperty = new PropertyDTO(selectedPropertyType, nameTextField.getText());
            setPropertyId(newProperty);
            if(newProperty.getPropertyId() != 0){
                performAction(Constants.CRUD.UPDATE, newProperty);
            }
        }
    }
    @Override
    public void onDelete(ActionEvent actionEvent) {

        PropertyDTO newProperty = propertyTable.getSelectionModel().getSelectedItem();
//        setPropertyId(newProperty);
        if(newProperty.getPropertyId() != 0){
            performAction(Constants.CRUD.DELETE, newProperty);
        }
    }

    private void performAction(Constants.CRUD action, PropertyDTO newProperty){
        try {
            propertyRepository.performPropertyAction(newProperty, action);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reset();
    }

    private void setPropertyId(PropertyDTO newProperty){
        newProperty.setPropertyId(propertyRepository.getAllProperties()
                .stream()
                .filter(prop -> Objects.equals(prop.getPropertyTypeName(), nameTextField.getText()))
                .findFirst()
                .orElse(new PropertyDTO(selectedPropertyType, nameTextField.getText()))
                .getPropertyId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset();
        populateTypeMenu();
    }
    private void reset(){
        nameTextField.clear();
        populateTypeMenu();
        propertyNameColumn.setCellValueFactory(new PropertyValueFactory<>("propertyName"));
        propertyTypeNameColumn.setCellValueFactory(new PropertyValueFactory<>("propertyTypeName"));

        propertyTable.setItems(propertyRepository.getAllProperties());
    }
    private void populateTypeMenu() {
        populateMenu(typeMenu, Constants.PLACEHOLDER_TEXT.MENU_ITEM, Arrays.asList(BOOK, LEVEL, INSTRUMENT));
    }

    private void populateMenu(MenuButton typeMenu, String placeholderText, List<String> list) {
        for (String item : list) {
            typeMenu.setText(placeholderText);
            MenuItem menuItem = new MenuItem(item);
            typeMenu.getItems().add(menuItem);
            menuItem.setOnAction(e -> selectedPropertyType = item);
        }
    }

    public List<PossibleError> buildPossibleErrors(){
        List<PossibleError> possibleErrors = new ArrayList<>();

        possibleErrors.add(new PossibleError("Name", nameTextField.getText(), nameTextLabel));
        possibleErrors.add(new PossibleError("Type", selectedPropertyType, propertyTypeLabel));

        return possibleErrors;
    }

    @Override
    public String getNameForItem(Object item) {
        return "";
    }

    @Override
    public void setUserId(int userId) {

    }
}
