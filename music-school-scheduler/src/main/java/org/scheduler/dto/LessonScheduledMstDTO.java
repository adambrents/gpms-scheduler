package org.scheduler.dto;

import org.scheduler.dto.interfaces.ISqlConvertable;
import org.scheduler.repository.configuration.model.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;

public record LessonScheduledMstDTO(int lessonScheduledMstId, int lessonScheduledId) implements ISqlConvertable<LessonScheduledMstDTO> {
    @Override
    public String toSqlSelectQuery() {
        return String.format("SELECT * FROM %s", DB_TABLES.LESSONS_SCHEDULED_MST);
    }

    @Override
    public String toSqlInsertQuery(LessonScheduledMstDTO item) {
        return String.format("INSERT INTO %s (LESSONS_SCHEDULED_MST_Id, Lesson_Scheduled_Id) VALUES (%d, %d)",
                DB_TABLES.LESSONS_SCHEDULED_MST, item.lessonScheduledMstId(), item.lessonScheduledId());
    }

    @Override
    public String toSqlUpdateQuery(LessonScheduledMstDTO item) {
        return String.format("UPDATE %s SET Lesson_Scheduled_Id = %d WHERE LESSONS_SCHEDULED_MST_Id = %d",
                DB_TABLES.LESSONS_SCHEDULED_MST, item.lessonScheduledId(), item.lessonScheduledMstId());
    }

    @Override
    public String toSqlDeleteQuery(LessonScheduledMstDTO item) {
        return String.format("DELETE FROM %s WHERE LESSONS_SCHEDULED_MST_Id = %d", DB_TABLES.LESSONS_SCHEDULED_MST, item.lessonScheduledMstId());
    }

    @Override
    public LessonScheduledMstDTO fromResultSet(ResultSet rs) {
        try {
            return new LessonScheduledMstDTO(rs.getInt("LESSONS_SCHEDULED_MST_Id"), rs.getInt("Lesson_Scheduled_Id"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
