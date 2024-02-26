package org.scheduler.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.constants.Constants;
import org.scheduler.repository.base.BaseDTO;
import org.scheduler.viewmodels.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersDTO extends BaseDTO {

    private static final ObservableList<User> allUsers = FXCollections.observableArrayList();

    /**
     * Validates user credentials with the db and returns a numeric value for what credential element is incorrect/correct
     *
     * @param user
     * @return
     */

    public int isLoginMatchUser(User user){
        int valid = 0;
        String sql = String.format(
                "SELECT * FROM %s.%s;",
                _database,
                Constants.DbTables.USERS
        );

        try(Statement statement = getStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                allUsers.add(buildUserFromResultSet(resultSet));
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        int i = 0;
        while (i < allUsers.size()){
            boolean bothAreWrong = !user.username().equals(allUsers.get(i).username()) && !user.password().equals(allUsers.get(i).password());
            if(!user.password().equals(allUsers.get(i).password())){
                valid = 1;
                if (bothAreWrong){
                    valid = 3;

                }
            }
            else if(!user.username().equals(allUsers.get(i).username())){
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
    public ObservableList<User> GetAllUsers(){
        allUsers.clear();
        String sql = String.format(
                "SELECT * FROM %s.%s;",
                _database,
                Constants.DbTables.USERS
        );

        try(Statement statement = getStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                allUsers.add(buildUserFromResultSet(resultSet));
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        return allUsers;
    }

    /**
     * returns a user object in response to a username
     *
     * @param userName
     * @return
     */
    public User getUserByName(String userName){
        allUsers.clear();
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
    public User getUserById(int userId){
        allUsers.clear();
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
    private User buildUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getString("User_Name"),
                resultSet.getInt("User_ID"),
                resultSet.getString("Password")
        );
    }
}
