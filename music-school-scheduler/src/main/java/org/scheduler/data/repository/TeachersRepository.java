package org.scheduler.data.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.TeacherDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TeachersRepository extends BaseRepository<TeacherDTO> {
    private final Logger _logger = LoggerFactory.getLogger(TeachersRepository.class);
    private static final ObservableList<TeacherDTO> ALL_TEACHER_DTOS = FXCollections.observableArrayList();


    /**
     * returns a user object in response to a username
     *
     * @param teacherName
     * @return
     */
    public static TeacherDTO getTeacherByName(String teacherName){
        ALL_TEACHER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM music_school.teachers WHERE Teacher_Name='" + teacherName + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return new TeacherDTO(
                        resultSet.getInt("Teacher_Id"),
                        resultSet.getString("Teacher_First_Name"),
                        resultSet.getString("Teacher_Last_Name"),
                        resultSet.getString("Address_Line_1"),
                        resultSet.getString("Address_Line_2"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Email")
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns a user object in response to a userid
     * @param teacherId
     * @return
     */
    public static TeacherDTO getTeacherById(int teacherId){
        ALL_TEACHER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM music_school.teachers WHERE Teacher_Id=" + teacherId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return new TeacherDTO(
                        resultSet.getInt("Teacher_Id"),
                        resultSet.getString("Teacher_First_Name"),
                        resultSet.getString("Teacher_Last_Name"),
                        resultSet.getString("Address_Line_1"),
                        resultSet.getString("Address_Line_2"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Email")
                );

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ObservableList<TeacherDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(TeacherDTO.class));
    }

    @Override
    public void updateItem(TeacherDTO item) throws SQLException {
        super.update(item);
    }

    @Override
    public int insertItem(TeacherDTO item) throws SQLException {
        super.insert(item);
        return 0;
    }

    @Override
    public void deleteItem(TeacherDTO item) throws SQLException {
        super.delete(item);
    }
}
