package org.scheduler.data.dto.properties;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.repository.interfaces.IRepository;
import org.scheduler.data.repository.properties.LevelRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class LevelDTO extends DTOBase<LevelDTO> implements ISqlConvertible<LevelDTO> {

    public LevelDTO(int Id, String name) {
        this.id = Id;
        this.name = name;
    }

    public LevelDTO() {
        
    }

    public LevelDTO(String name) {
        this.name = name;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.LEVELS;
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(LevelDTO item, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.LEVELS + " (Level_Id, Name) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId());
        statement.setString(2, item.getName());
        return statement;
    }
    @Override
    public PreparedStatement toSqlUpdateQuery(LevelDTO item, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.LEVELS + " SET Name = ? WHERE Level_Id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, item.getName());
        statement.setInt(2, item.getId());
        return statement;
    }
    @Override
    public PreparedStatement toSqlDeleteQuery(LevelDTO item, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.LEVELS + " WHERE Level_Id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId());
        return statement;
    }
    @Override
    public IRepository getRepository() {
        return new LevelRepository();
    }

    @Override
    public LevelDTO fromResultSet(ResultSet rs) {
        try {
            return new LevelDTO(rs.getInt("Level_Id"), rs.getString("Name"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LevelDTO) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "LevelDTO[" +
                "levelId=" + id + ", " +
                "name=" + name + ']';
    }

}
