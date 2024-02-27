package org.scheduler.repository;

import org.scheduler.repository.base.BaseRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.repository.configuration.model.DB_TABLES;
import org.scheduler.dto.StudentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsRepository extends BaseRepository<StudentDTO> {

    private final Logger _logger = LoggerFactory.getLogger(StudentsRepository.class);
    private final ObservableList<StudentDTO> allStudentDTOS = FXCollections.observableArrayList();
    private final ObservableList<String> allCountries = FXCollections.observableArrayList();
    private int lastID = 0;

    /**
     * gets a list of all students in the db
     * @return
     */
    @Override
    public ObservableList<StudentDTO> getAllItems() {
        try{
            return FXCollections.observableArrayList(super.getAllItemsFromType(StudentDTO.class));

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * updates a studentDTO with new values from input studentDTO
     *
     * @param studentDTO
     */
    @Override
    public void updateItem(StudentDTO studentDTO) {
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
                    DB_TABLES.STUDENTS,
                    studentDTO.getFirstName(),
                    studentDTO.getLastName(),
                    studentDTO.getAddressLine1(),
                    studentDTO.getAddressLine2(),
                    studentDTO.getPhoneNumber(),
                    studentDTO.getPostalCode(),
                    DB_TABLES.STUDENTS,
                    studentDTO.getId()
            );
            statement.executeQuery(query);
            
        } catch (Exception e) {
            _logger.error("StudentDTO update failed! Exception: " + e);
        }
    }

    /**
     * adds a studentDTO to the db
     *
     * @param studentDTO
     */
    @Override
    public void insertItem(StudentDTO studentDTO) {
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
                    DB_TABLES.STUDENTS,
                    studentDTO.getId(),
                    studentDTO.getFirstName(),
                    studentDTO.getLastName(),
                    studentDTO.getAddressLine1(),
                    studentDTO.getAddressLine2(),
                    studentDTO.getPostalCode(),
                    studentDTO.getPhoneNumber()
            );

            statement.executeQuery(query);

        }
        catch (SQLException sqlException){
            _logger.error("Adding studentDTO failed!", sqlException);
        }
    }

    /**
     * hard deletes a customer from the db
     *
     * @param selectedStudentDTO
     */
    public void deleteItem(StudentDTO selectedStudentDTO){
        String query = String.format(
                "DELETE FROM %s.%s WHERE Student_ID = %s;",
                _database,
                DB_TABLES.STUDENTS,
                selectedStudentDTO.getId()
        );
        try(Statement statement = getStatement()){
            statement.executeQuery(query);

        } catch (Exception e) {
            e.printStackTrace();
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
                    DB_TABLES.STUDENTS
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
                    DB_TABLES.STUDENTS,
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
                DB_TABLES.STUDENTS,
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

}
