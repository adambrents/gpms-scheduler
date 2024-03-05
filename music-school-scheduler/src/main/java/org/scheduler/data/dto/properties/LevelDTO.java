package org.scheduler.data.dto.properties;

import org.scheduler.data.dto.interfaces.ISqlConvertable;
import org.scheduler.data.configuration.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class LevelDTO implements ISqlConvertable<LevelDTO> {
    private int id;
    private String name;

    public LevelDTO(int Id, String name) {
        this.id = Id;
        this.name = name;
    }

    public LevelDTO() {
        
    }

    @Override
    public String toSqlSelectQuery() {
        return String.format("SELECT * FROM %s", DB_TABLES.LEVELS);
    }

    @Override
    public String toSqlInsertQuery(LevelDTO item) {
        return String.format("INSERT INTO %s (Level_Id, Name) VALUES (%d, '%s')",
                DB_TABLES.LEVELS, item.levelId(), item.name());
    }

    @Override
    public String toSqlUpdateQuery(LevelDTO item) {
        return String.format("UPDATE %s SET Name = '%s' WHERE Level_Id = %d",
                DB_TABLES.LEVELS, item.name(), item.levelId());
    }

    @Override
    public String toSqlDeleteQuery(LevelDTO item) {
        return String.format("DELETE FROM %s WHERE Level_Id = %d", DB_TABLES.LEVELS, item.levelId());
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

    public int levelId() {
        return id;
    }

    public String name() {
        return name;
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
