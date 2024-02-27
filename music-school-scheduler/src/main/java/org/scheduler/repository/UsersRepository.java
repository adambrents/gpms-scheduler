package org.scheduler.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.repository.base.BaseRepository;
import org.scheduler.repository.configuration.model.DB_TABLES;
import org.scheduler.dto.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersRepository extends BaseRepository<UserDTO> {

    private static final ObservableList<UserDTO> ALL_USER_DTOS = FXCollections.observableArrayList();
    /**
     * returns an array of all users
     * @return
     */
    @Override
    public ObservableList<UserDTO> getAllItems(){
        try{
            return FXCollections.observableArrayList(super.getAllItemsFromType(UserDTO.class));
        } catch (SQLException e){

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateItem(UserDTO item) {

    }

    @Override
    public void insertItem(UserDTO item) {

    }

    @Override
    public void deleteItem(UserDTO item) {

    }

    /**
     * Validates userDTO credentials with the db and returns a numeric value for what credential element is incorrect/correct
     *
     * @param userDTO
     * @return
     */

    public int isLoginMatchUser(UserDTO userDTO){
        int valid = 0;
        String sql = String.format(
                "SELECT * FROM %s.%s;",
                _database,
                DB_TABLES.USERS
        );

        try(Statement statement = getStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                ALL_USER_DTOS.add(buildUserFromResultSet(resultSet));
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
     * returns a user object in response to a username
     *
     * @param userName
     * @return
     */
    public UserDTO getUserByName(String userName){
        ALL_USER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM client_schedule.users WHERE User_Name='" + userName + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return buildUserFromResultSet(resultSet);
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
    public UserDTO getUserById(int userId){
        ALL_USER_DTOS.clear();
        try(Statement statement = getStatement()) {
            String sql = "SELECT * FROM client_schedule.users WHERE User_ID=" + userId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return buildUserFromResultSet(resultSet);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private UserDTO buildUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserDTO(
                resultSet.getString("User_Name"),
                resultSet.getInt("User_ID"),
                resultSet.getString("Password")
        );
    }
}
