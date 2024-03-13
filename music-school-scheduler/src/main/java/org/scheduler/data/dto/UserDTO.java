package org.scheduler.data.dto;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.repository.UsersRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

public final class UserDTO extends DTOBase<UserDTO> implements ISqlConvertible<UserDTO> {
    private int userId;
    private String password;
    private LocalDateTime createDate;
    private int createdBy;
    private LocalDateTime lastUpdate;
    private int lastUpdatedBy;
    private boolean isActive;

    /**
     * constructor for user object
     *
     * @param name
     * @param userId
     * @param password
     */
    public UserDTO(String name, int userId, String password, boolean isActive) {
        super.name = name;
        this.userId = userId;
        this.password = password;
        this.isActive = isActive;
    }
    public UserDTO(String name, String password, LocalDateTime createDate, int createdBy, LocalDateTime lastUpdate, int lastUpdatedBy, boolean isActive) {
        super.name = name;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.isActive = isActive;
    }
    public UserDTO(int userId, String name, String password, LocalDateTime lastUpdate, int lastUpdatedBy, boolean isActive) {
        this.userId = userId;
        super.name = name;
        this.password = password;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.isActive = isActive;
    }
    public UserDTO(){
        
    }

    /**
     * getter for UserDTO
     *
     * @return
     */
    public void setUserId(int userId){
        this.userId = userId;
    }

    /**
     * getter/setter for UserDTO
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.USERS;
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(UserDTO userDTO, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.USERS + " (User_Id, User_Name, Password) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, userDTO.getId());
        statement.setString(2, userDTO.getName());
        statement.setString(3, userDTO.getPassword());
        return statement;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(UserDTO userDTO, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.USERS + " SET User_Name = ?, Password = ?, Active = ? WHERE User_Id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, userDTO.getName());
        statement.setString(2, userDTO.getPassword());
        statement.setInt(3, userDTO.getIsActive() ? 1 : 0); // Convert boolean to int
        statement.setInt(4, userDTO.getId());
        return statement;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(UserDTO userDTO, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.USERS + " WHERE User_Id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, userDTO.getId());
        return statement;
    }

    @Override
    public IRepository getRepository() {
        return new UsersRepository();
    }

    @Override
    public UserDTO fromResultSet(ResultSet rs) {
        try {
            return new UserDTO(
                    rs.getString("User_Name"),
                    rs.getInt("User_Id"),
                    rs.getString("Password"),
                    rs.getBoolean("Active")

            );
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
        return Objects.equals(this.name, that.name) &&
                this.userId == that.userId &&
                Objects.equals(this.password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userId, password);
    }

    @Override
    public String toString() {
        return "UserDTO[" +
                "username=" + name + ", " +
                "userId=" + userId + ", " +
                "password=" + password + ']';
    }

}
