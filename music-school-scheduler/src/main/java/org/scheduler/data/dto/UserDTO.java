package org.scheduler.data.dto;

import org.scheduler.data.dto.interfaces.ISqlConvertable;
import org.scheduler.data.configuration.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class UserDTO implements ISqlConvertable<UserDTO> {
    private String username;
    private int userId;
    private String password;

    /**
     * constructor for user object
     *
     * @param username
     * @param userId
     * @param password
     */
    public UserDTO(String username, int userId, String password) {
        this.username = username;
        this.userId = userId;
        this.password = password;
    }

    public UserDTO(){
        
    }

    /**
     * getter/setter for UserDTO
     *
     * @return
     */
    public String username() {
        return username;
    }

    /**
     * getter for UserDTO
     *
     * @return
     */
    public int userId() {
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    /**
     * getter/setter for UserDTO
     *
     * @return
     */
    public String password() {
        return password;
    }

    @Override
    public String toSqlSelectQuery() {
        // Assuming the table name for users follows the convention in DB_TABLES
        return String.format("SELECT * FROM %s", DB_TABLES.USERS);
    }

    @Override
    public String toSqlInsertQuery(UserDTO userDTO) {
        // Adjusted column names to match title casing with underscores
        return String.format("INSERT INTO %s (User_Id, User_Name, Password) VALUES (%d, '%s', '%s')",
                DB_TABLES.USERS, userDTO.userId(), userDTO.username(), userDTO.password());
    }

    @Override
    public String toSqlUpdateQuery(UserDTO userDTO) {
        // Adjusted column names for update
        return String.format("UPDATE %s SET User_Name = '%s', Password = '%s' WHERE User_Id = %d",
                DB_TABLES.USERS, userDTO.username(), userDTO.password(), userDTO.userId());
    }

    @Override
    public String toSqlDeleteQuery(UserDTO userDTO) {
        // Adjusted column name for delete
        return String.format("DELETE FROM %s WHERE User_Id = %d", DB_TABLES.USERS, userDTO.userId());
    }

    @Override
    public UserDTO fromResultSet(ResultSet rs) {
        try {
            return new UserDTO(rs.getString("User_Name"), rs.getInt("User_Id"), rs.getString("Password"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDTO) obj;
        return Objects.equals(this.username, that.username) &&
                this.userId == that.userId &&
                Objects.equals(this.password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userId, password);
    }

    @Override
    public String toString() {
        return "UserDTO[" +
                "username=" + username + ", " +
                "userId=" + userId + ", " +
                "password=" + password + ']';
    }

}
