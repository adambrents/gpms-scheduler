package org.scheduler.app.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Paint;
import org.scheduler.app.controller.base.ControllerBase;
import org.scheduler.app.controller.interfaces.*;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static org.scheduler.app.constants.Constants.ERROR_RED;

public class UserManagementController extends ControllerBase implements IController, ICreate, IUpdate, IDelete, ITableView<UserDTO> {
    private static final Logger _logger = LoggerFactory.getLogger(UserManagementController.class);
    public TableView<UserDTO> userTable;
    public TableColumn<UserDTO, String> userNameColumn;
    public TableColumn<UserDTO, Boolean> userActiveColumn;
    public Label userNameLabel;
    public Label passwordLabel;
    public Label retypePasswordLabel;
    public TextField userNameTextField;
    public TextField passwordTextField;
    public TextField retypePasswordTextField;
    public CheckBox isActiveCheckbox;
    
    private UserDTO userToBeUpdated;

    @Override
    public List<PossibleError> buildPossibleErrors() {

        List<PossibleError> possibleErrors = new ArrayList<>();

        possibleErrors.add(new PossibleError("Username", userNameTextField.getText(), userNameLabel));
        possibleErrors.add(new PossibleError("Password", passwordTextField.getText(), passwordLabel));
        possibleErrors.add(new PossibleError("Retype Password", retypePasswordTextField.getText(), retypePasswordLabel));

        return possibleErrors;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset();
        userTable.setRowFactory(tv -> {
            TableRow<UserDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    handleDoubleClickOnRow(row.getItem());
                }
            });
            return row;
        });
        isActiveCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && userToBeUpdated != null) {
                passwordTextField.setText("jkInSDXeQ2");
                passwordTextField.setDisable(true);
                retypePasswordTextField.setText("jkInSDXeQ2");
                retypePasswordTextField.setDisable(true);
            }
            else {
                passwordTextField.setText("");
                passwordTextField.setDisable(false);
                retypePasswordTextField.setText("");
                retypePasswordTextField.setDisable(false);
            }
        });
    }

    @Override
    public void handleDoubleClickOnRow(UserDTO item) {
        userNameTextField.setText(item.getName());
        isActiveCheckbox.setSelected(!item.getIsActive());
        userToBeUpdated = item;
    }
    @Override
    public void reset() {
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userActiveColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        userActiveColumn.setCellFactory(column -> new TableCell<UserDTO, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item ? "Yes" : "No");
                }
            }
        });
        userTable.setItems(usersRepository.getAllItems());
        labelsToReset.clear();
        Collections.addAll(
                labelsToReset,
                userNameLabel,
                passwordLabel,
                retypePasswordLabel
        );
        super.reset();
    }
    @Override
    public void onGoBack(ActionEvent event) {
        try{
            super.goBack(this.userId);
        }
        catch (IOException e){
            _logger.error("Could not load resource!", e);
        }
    }
    public void onSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if(userToBeUpdated != null){
            onUpdate(actionEvent);
        }
        else {
            onAdd(actionEvent);
        }
    }
    @Override
    public void onAdd(ActionEvent event) throws IOException, SQLException {
        String errorMessage = this.getErrorMessage();
        if(!errorMessage.equals("")){
            errorText.setText(errorMessage);
            errorText.setVisible(true);
        }
        else {
            usersRepository.insertItem(new UserDTO(
                    userNameTextField.getText(),
                    passwordTextField.getText(),
                    LocalDateTime.now(),
                    userId,
                    LocalDateTime.now(),
                    userId,
                    !isActiveCheckbox.isSelected()
            ), JDBC.getConnection());
            reset();
        }
    }

    @Override
    public String getErrorMessage() {
        StringBuilder errorMessageBuilder = new StringBuilder(super.getErrorMessage());

        if (!Objects.equals(passwordTextField.getText(), retypePasswordTextField.getText())) {
            errorMessageBuilder.append("\nPasswords do not match!");
        }

        return errorMessageBuilder.toString();
    }

    @Override
    public void onDelete(ActionEvent actionEvent) {
        UserDTO userDTO = userTable.getSelectionModel().getSelectedItem();

        try {
            usersRepository.deleteItem(userDTO, JDBC.getConnection());
            reset();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdate(ActionEvent actionEvent) throws IOException, SQLException {
        String errorMessage = super.getErrorMessage();
        if(!errorMessage.equals("")){
            errorText.setText(errorMessage);
        }
        else {
            UserDTO newUser = new UserDTO(
                    userToBeUpdated.getId(),
                    userNameTextField.getText(),
                    passwordTextField.getText(),
                    LocalDateTime.now(),
                    userId,
                    !isActiveCheckbox.isSelected()
            );
            if(newUser.getId() != 0){
                usersRepository.updateItem(newUser, JDBC.getConnection());
                reset();
            }
            userToBeUpdated = null;
        }
    }
}
