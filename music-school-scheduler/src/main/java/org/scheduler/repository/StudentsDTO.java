package org.scheduler.repository;

import org.scheduler.constants.Constants;
import org.scheduler.repository.base.BaseDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.scheduler.viewmodels.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsDTO extends BaseDTO {

    private final Logger _logger = LoggerFactory.getLogger(StudentsDTO.class);
    private final ObservableList<Student> allStudents = FXCollections.observableArrayList();
    private final ObservableList<String> allCountries = FXCollections.observableArrayList();
    private int lastID = 0;

    /**
     * gets a list of all students in the db
     * @return
     */
    public ObservableList<Student> getAllStudents() {
        try(Statement statement = getStatement()){
            allStudents.clear();
            String query = String.format(
                       "SELECT students.%s, "
                            + "students.%s, "
                            + "students.%s, "
                            + "students.%s, "
                            + "students.%s, "
                            + "students.%s, "
                            + "students.%s "
                            + "FROM %s.%s "
                            + "WHERE students.%s IS NOT NULL;",
                    "Student_ID",
                    "Student_First_Name",
                    "Student_Last_Name",
                    "Address_Line_1",
                    "Address_Line_2",
                    "Postal_Code",
                    "Phone",
                    _database,
                    Constants.DbTables.STUDENTS,
                    "Student_First_Name"
            );

            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                Student student = new Student(
                        resultSet.getInt("Student_ID"),
                        resultSet.getString("Student_First_Name"),
                        resultSet.getString("Student_Last_Name"),
                        resultSet.getString("Address_Line_1"),
                        resultSet.getString("Address_Line_2"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"));

                allStudents.add(student);
            }
            
            return allStudents;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * updates a student with new values from input student
     *
     * @param student
     */
    public void updateStudent(Student student) {
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "UPDATE %s.%s "
                    + "SET Student_First_Name = '%s', "
                    + "Student_Last_Name = '%s', "
                    + "Address_Line_1 = '%s', "
                    + "Address_Line_2 = '%s', "
                    + "Phone = '%s', "
                    + "Postal_Code = '%s' "
                    + "WHERE %s.Student_ID = %d AND Student_First_Name IS NOT NULL;",
                    _database,
                    Constants.DbTables.STUDENTS,
                    student.getFirstName(),
                    student.getLastName(),
                    student.getAddressLine1(),
                    student.getAddressLine2(),
                    student.getPhoneNumber(),
                    student.getPostalCode(),
                    Constants.DbTables.STUDENTS,
                    student.getId()
            );
            statement.executeQuery(query);
            
        } catch (Exception e) {
            _logger.error("Student update failed! Exception: " + e);
        }
    }

    /**
     * gets a new customer id when adding new students via UI
     *
     * @return
     */
    public int getLastStudentId() {
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "SELECT MAX(Student_ID) "
                            + "FROM %s.%s;",
                    _database,
                    Constants.DbTables.STUDENTS
            );
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt(1) > lastID) {
                    lastID = resultSet.getInt(1);
                } else {
                    //do nothing if the id is less than last id
                }
            }
            lastID++;
            

            return lastID;
        } catch (SQLException sqlException) {
            return -1;
        }
    }

    /**
     * adds a student to the db
     *
     * @param student
     */
    public void addStudent(Student student) {
        try(Statement statement = getStatement()) {
            String query = String.format(
                    "INSERT INTO %s.%s (" +
                            "Student_ID, " +
                            "Student_First_Name, " +
                            "Student_Last_Name, " +
                            "Address_Line_1, " +
                            "Address_Line_2, " +
                            "Postal_Code, " +
                            "Phone) " +
                    "VALUES(%s, '%s', '%s', '%s', '%s');",
                    _database,
                    Constants.DbTables.STUDENTS,
                    student.getId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getAddressLine1(),
                    student.getAddressLine2(),
                    student.getPostalCode(),
                    student.getPhoneNumber()
            );

            statement.executeQuery(query);
            
        }
        catch (SQLException sqlException){
            _logger.error("Adding student failed!", sqlException);
        }
    }

    /**
     * gets a customer name based on customer ID
     *
     * @param customerID
     * @return
     */
    public String getStudentNameFromId(int customerID){
        try(Statement statement = getStatement()){
            String query = String.format(
                    "SELECT Student_First_Name + ' ' + Student_Last_Name "
                            + "FROM %s.%s "
                            + "WHERE Student_ID = %s;",
                    _database,
                    Constants.DbTables.STUDENTS,
                    customerID
            );

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                return resultSet.getString(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * gets a customer ID based on customer name
     *
     * @param customerName
     * @return
     */
    public List<Integer> getStudentIdsFromName(String customerName){
        List<Integer> studentIds = new ArrayList<>();
        String query = String.format(
                "SELECT Student_ID FROM %s.%s WHERE Student_First_Name = '%s' AND Student_First_Name IS NOT NULL;",
                _database,
                Constants.DbTables.STUDENTS,
                customerName
        );
        try(Statement statement = getStatement()){


            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                studentIds.add(resultSet.getInt("Student_ID"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentIds;
    }

    /**
     * soft deletes a customer from the db
     *
     * @param selectedStudent
     */
    public void deleteStudent(Student selectedStudent){
        String query = String.format(
                "DELETE FROM %s.%s WHERE Student_ID = %s;",
                _database,
                Constants.DbTables.STUDENTS,
                selectedStudent.getId()
        );
        try(Statement statement = getStatement()){
            statement.executeQuery(query);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
