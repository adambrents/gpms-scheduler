package org.scheduler.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.repository.base.BaseRepository;
import org.scheduler.dto.TeacherDTO;
import org.scheduler.dto.UserDTO;

import java.sql.ResultSet;
import java.sql.Statement;

public class TeachersRepository extends BaseRepository<TeacherDTO> {

    private static final ObservableList<UserDTO> ALL_USER_DTOS = FXCollections.observableArrayList();
    private static final ObservableList<TeacherDTO> ALL_TEACHER_DTOS = FXCollections.observableArrayList();
    public static int submit(UserDTO userDTO){
        int valid = 0;
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM client_schedule.users;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                UserDTO allUserDTO = new UserDTO(
                    resultSet.getString("User_Name"),
                    resultSet.getInt("User_ID"),
                    resultSet.getString("Password")
                );
                ALL_USER_DTOS.add(allUserDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int i = 0;
        while (i < ALL_USER_DTOS.size()){
            boolean bothAreWrong = !userDTO.username().equals(ALL_USER_DTOS.get(i).username()) && !userDTO.password().equals(ALL_USER_DTOS.get(i).password());
            if(!userDTO.password().equals(ALL_USER_DTOS.get(i).password())){
                valid = 1;
                if (bothAreWrong){
                    valid = 3;

                }
            }
            else if(!userDTO.username().equals(ALL_USER_DTOS.get(i).username())){
                valid = 2;
                if (bothAreWrong){
                    valid = 3;
                }
            }
            else{
                valid = 0;
                break;
            }
            i++;
        }
        return valid;
    }

    /**
     * returns an array of all users
     * @return
     */
    public static ObservableList<UserDTO> getAllUsers(){
        ALL_USER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM client_schedule.users;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                UserDTO allUserDTO = new UserDTO(
                        resultSet.getString("User_Name"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Password")
                );
                ALL_USER_DTOS.add(allUserDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ALL_USER_DTOS;
    }

    /**
     * returns a user object in response to a username
     *
     * @param userName
     * @return
     */
    public static UserDTO getUserByName(String userName){
        ALL_USER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM client_schedule.users WHERE User_Name='" + userName + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return new UserDTO(
                        resultSet.getString("User_Name"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Password")
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns a user object in response to a userid
     * @param userId
     * @return
     */
    public static UserDTO getUserById(int userId){
        ALL_USER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM client_schedule.users WHERE User_ID=" + userId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return new UserDTO(
                        resultSet.getString("User_Name"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Password")
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ObservableList<TeacherDTO> getAllItems() {
        return null;
    }

    @Override
    public void updateItem(TeacherDTO item) {

    }

    @Override
    public void insertItem(TeacherDTO item) {

    }

    @Override
    public void deleteItem(TeacherDTO item) {

    }
}
