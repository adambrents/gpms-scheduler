package org.scheduler.data.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.repository.base.BaseRepository;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UsersRepository extends BaseRepository<UserDTO> {

    private final Logger _logger = LoggerFactory.getLogger(UsersRepository.class);
    private static final ObservableList<UserDTO> ALL_USER_DTOS = FXCollections.observableArrayList();
    /**
     * returns an array of all users
     * @return
     */
    @Override
    public ObservableList<UserDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(UserDTO.class));
    }

    @Override
    public void updateItem(UserDTO item) throws SQLException {
        super.update(item);
    }

    @Override
    public int insertItem(UserDTO item) throws SQLException {
        super.insert(item);
        return 0;
    }

    @Override
    public void deleteItem(UserDTO item) throws SQLException {
        super.delete(item);
    }

    /**
     * Validates userDTO credentials with the db and returns a numeric value for what credential element is incorrect/correct
     *
     * @param userDTO
     * @return
     */

    public LoginResult isLoginMatchUser(UserDTO userDTO) {
        // Assuming _database and DB_TABLES.USERS are properly sanitized and trusted inputs
        String sql = "SELECT Password, User_ID FROM " + _database + "." + DB_TABLES.USERS + " WHERE User_Name = ?;";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userDTO.username());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String dbPassword = resultSet.getString("Password");
                    if (dbPassword.equals(userDTO.password())) {
                        userDTO.setUserId(resultSet.getInt("User_ID"));
                        return LoginResult.SUCCESS;
                    } else {
                        return LoginResult.WRONG_PASSWORD;
                    }
                } else {
                    return LoginResult.WRONG_USERNAME;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoginResult.DEFAULT;
        }
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
    public enum LoginResult{
        DEFAULT, SUCCESS, WRONG_PASSWORD, WRONG_USERNAME, WRONG_BOTH
    }
}
