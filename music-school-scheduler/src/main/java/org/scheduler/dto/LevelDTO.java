package org.scheduler.dto;

import org.scheduler.dto.interfaces.ISqlConvertable;
import org.scheduler.repository.configuration.model.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;

public record LevelDTO(int levelId, String name) implements ISqlConvertable<LevelDTO> {
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
}
