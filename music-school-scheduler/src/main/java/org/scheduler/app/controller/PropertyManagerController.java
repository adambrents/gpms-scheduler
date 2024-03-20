package org.scheduler.app.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Paint;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.ICreate;
import org.scheduler.app.controller.interfaces.IDelete;
import org.scheduler.app.controller.interfaces.IUpdate;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.properties.LevelDTO;
import org.scheduler.data.repository.interfaces.IRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import static org.scheduler.app.constants.Constants.ERROR_RED;
import static org.scheduler.app.constants.Constants.PRIMARY_STAGE;
import static org.scheduler.app.constants.Constants.PROPERTIES.*;

public class PropertyManagerController<T extends ISqlConvertible> extends ControllerBase implements ICreate, IUpdate, IDelete {
    private static final Logger _logger = LoggerFactory.getLogger(PropertyManagerController.class);
    public MenuButton typeMenu;
    public TextField nameTextField;
    public TableView<ISqlConvertible> propertyTable;
    public TableColumn<ISqlConvertible, String> propertyTypeNameColumn;
    public TableColumn<ISqlConvertible, String> propertyNameColumn;
    public Label propertyTypeLabel;
    public Label nameTextLabel;
    public Label clearLabel;
    private ISqlConvertible selectedPropertyType;
    private ISqlConvertible propertyToBeUpdated;
    
    private IRepository<ISqlConvertible> repository;

    @Override
    public void onGoBack(ActionEvent actionEvent) {
        try{
            super.goBack(this.userId);
        }
        catch (IOException e){
            _logger.error("Could not load resource!", e);
        }
    }

    public void onSubmit(ActionEvent actionEvent) throws IOException {
        if(propertyToBeUpdated != null){
            onUpdate(actionEvent);
        }
        else {
            onAdd(actionEvent);
        }
    }

    @Override
    public void onAdd(ActionEvent actionEvent) throws IOException {
        String errorMessage = super.getErrorMessage();
        if(!errorMessage.equals("")){
            errorText.setText(errorMessage);
            errorText.setVisible(true);
        }
        else {
            buildNewProperty(selectedPropertyType);
            performAction(Constants.CRUD.CREATE, selectedPropertyType);
        }
    }

    private void buildNewProperty(ISqlConvertible item) {
        item.setName(nameTextField.getText());
    }

    @Override
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        String errorMessage = super.getErrorMessage();
        if(!errorMessage.equals("")){
            errorText.setText(errorMessage);
        }
        else {
            if(propertyToBeUpdated != null && propertyToBeUpdated.getId() != 0){
                buildNewProperty(propertyToBeUpdated);
                performAction(Constants.CRUD.UPDATE, propertyToBeUpdated);
            }
        }
        propertyToBeUpdated = null;
    }
    @Override
    public void onDelete(ActionEvent actionEvent) {
        ISqlConvertible newProperty = propertyTable.getSelectionModel().getSelectedItem();
        if(newProperty.getId() != 0){
            performAction(Constants.CRUD.DELETE, newProperty);
        }
    }

    private void performAction(Constants.CRUD action, ISqlConvertible newProperty){
        try {
            switch (action){
                case CREATE -> newProperty.getRepository().insertItem(newProperty, JDBC.getConnection());
                case UPDATE -> newProperty.getRepository().updateItem(newProperty, JDBC.getConnection());
                case DELETE -> newProperty.getRepository().deleteItem(newProperty, JDBC.getConnection());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reset();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset();

        propertyTable.setRowFactory(tv -> {
            TableRow<ISqlConvertible> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    handleDoubleClickOnRow(row.getItem());
                }
            });
            return row;
        });

        clearLabel.setOnMouseClicked(event -> {
            reset();
        });
        clearLabel.setOnMouseEntered(event -> clearLabel.setStyle("-fx-text-fill: blue; -fx-cursor: hand;")); // Change text color on hover
        clearLabel.setOnMouseExited(event -> clearLabel.setStyle("-fx-text-fill: black; ")); // Revert to normal text color

    }

    private void handleDoubleClickOnRow(ISqlConvertible item) {
        nameTextField.setText(item.getName());
        typeMenu.setText(determineTypeName(item).getValue());
        typeMenu.setDisable(true);
        propertyToBeUpdated = item;
    }
    @Override
    public void reset() {
        populateTypeMenu();
        propertyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        propertyTypeNameColumn.setCellValueFactory(cellData -> determineTypeName(cellData.getValue()));
        propertyTable.setItems(getAllProperties());
        labelsToReset.clear();
        Collections.addAll(labelsToReset, nameTextLabel, propertyTypeLabel);
        super.reset();
    }
    private SimpleStringProperty determineTypeName(ISqlConvertible item){
        String typeName = "";
        if (item instanceof LevelDTO) {
            typeName = Level.name();
        } else if (item instanceof BookDTO) {
            typeName = Book.name();
        } else if (item instanceof InstrumentDTO) {
            typeName = Instrument.name();
        }
        return new SimpleStringProperty(typeName);
    }

    private ObservableList<ISqlConvertible> getAllProperties() {
        ObservableList<ISqlConvertible> propertyList = FXCollections.observableArrayList();
        propertyList.addAll(levelRepository.getAllItems());
        propertyList.addAll(bookRepository.getAllItems());
        propertyList.addAll(instrumentRepository.getAllItems());

        return propertyList;
    }

    private void populateTypeMenu() {
        populateMenu(typeMenu, Arrays.asList(new BookDTO(), new LevelDTO(), new InstrumentDTO()));
    }

    private void populateMenu(MenuButton typeMenu, List<ISqlConvertible> list) {
        typeMenu.getItems().clear();
        typeMenu.setText(Constants.PLACEHOLDER_TEXT.MENU_ITEM);
        for (ISqlConvertible item : list) {
            MenuItem menuItem = new MenuItem(determineTypeName(item).getValue());
            typeMenu.getItems().add(menuItem);
            menuItem.setOnAction(e -> {
                selectedPropertyType = item;
                typeMenu.setText(determineTypeName(item).getValue());
            });
        }
    }


    public List<PossibleError> buildPossibleErrors(){
        List<PossibleError> possibleErrors = new ArrayList<>();

        String typeMenuValue = typeMenu.getText() == Constants.PLACEHOLDER_TEXT.MENU_ITEM ? "" : typeMenu.getText();

        possibleErrors.add(new PossibleError("Name", nameTextField.getText(), nameTextLabel));
        possibleErrors.add(new PossibleError("Type", typeMenuValue, propertyTypeLabel));

        return possibleErrors;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
