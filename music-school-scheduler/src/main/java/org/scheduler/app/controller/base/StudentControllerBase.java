package org.scheduler.app.controller.base;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.scheduler.data.dto.StudentDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class StudentControllerBase extends ControllerBase {
    public TableColumn<StudentDTO, String> studentNameColumn = new TableColumn<>();
    public TableColumn<StudentDTO, String> studentAddressColumn = new TableColumn<>();
    public TableColumn<StudentDTO, String> studentPhoneColumn = new TableColumn<>();
    public TableView<StudentDTO> studentsTable = new TableView<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //Get data for StudentDTO Table
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentAddressColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        studentPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        //sets table with customer data
        try {
            studentsTable.setItems(studentsRepository.getAllItems());
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throw new RuntimeException(throwables);
        }

    }

    @Override
    public String getNameForItem(Object item) {
        return "";
    }

    @Override
    public void setUserId(int userId) {

    }
}
