package org.scheduler.dto;

import org.scheduler.dto.interfaces.ISqlConvertable;
import org.scheduler.repository.configuration.model.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;

public record UserDTO(String username, int userId, String password) implements ISqlConvertable<UserDTO> {
    /**
     * constructor for user object
     *
     * @param username
     * @param userId
     * @param password
     */
    public UserDTO {
    }

    /**
     * getter/setter for UserDTO
     *
     * @return
     */
    @Override
    public String username() {
        return username;
    }

    /**
     * getter/setter for UserDTO
     *
     * @return
     */
    @Override
    public int userId() {
        return userId;
    }

    /**
     * getter/setter for UserDTO
     *
     * @return
     */
    @Override
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
}
