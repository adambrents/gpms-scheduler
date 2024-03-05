package org.scheduler.data.repository;

import org.scheduler.data.dto.TeacherStudentInstrumentDTO;
import org.scheduler.data.repository.base.BaseRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.dto.StudentDTO;
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
    public ObservableList<StudentDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(StudentDTO.class));
    }
    @Override
    public void updateItem(StudentDTO item) throws SQLException {
        super.update(item);
    }

    @Override
    public int insertItem(StudentDTO item) throws SQLException {
        super.insert(item);
        return 0;
    }

    @Override
    public void deleteItem(StudentDTO item) throws SQLException {
        super.delete(item);
    }
    /**
     * gets a new customer id when adding new students via UI
     *
     * @return
     */
    public int getLastStudentId() {
        try(Statement statement = getStatement()){
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

    public int insertTeacherStudentInstrument(TeacherStudentInstrumentDTO teacherStudentInstrument) throws SQLException {
        return super.insert(teacherStudentInstrument);
    }

    public void updateTeacherStudentInstrument(TeacherStudentInstrumentDTO teacherStudentInstrument) throws SQLException {
        super.update(teacherStudentInstrument);
    }
}
