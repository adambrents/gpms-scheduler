package org.scheduler.controller.base;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.scheduler.repository.StudentsDTO;
import org.scheduler.viewmodels.Student;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class StudentControllerBase extends ControllerBase {
    public final StudentsDTO studentsDTO = new StudentsDTO();
    public TableColumn<Student, String> studentNameColumn = new TableColumn<>();
    public TableColumn<Student, String> studentAddressColumn = new TableColumn<>();
    public TableColumn<Student, String> studentPhoneColumn = new TableColumn<>();
    public TableView<Student> studentsTable = new TableView<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //Get data for Student Table
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentAddressColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        studentPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        //sets table with customer data
        studentsTable.setItems(studentsDTO.getAllStudents());

    }

}
