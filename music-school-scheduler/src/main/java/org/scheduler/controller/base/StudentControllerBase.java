package org.scheduler.controller.base;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.scheduler.repository.StudentsRepository;
import org.scheduler.dto.StudentDTO;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class StudentControllerBase extends ControllerBase {
    public final StudentsRepository studentsRepository = new StudentsRepository();
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
        studentsTable.setItems(studentsRepository.getAllItems());

    }

}
