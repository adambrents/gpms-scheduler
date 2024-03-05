package org.scheduler.data.dto;

import org.scheduler.data.dto.interfaces.ISqlConvertable;
import org.scheduler.data.configuration.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class LessonScheduledMstDTO implements ISqlConvertable<LessonScheduledMstDTO> {
    private int lessonScheduledMstId;
    private int lessonScheduledId;

    public LessonScheduledMstDTO(int lessonScheduledMstId, int lessonScheduledId) {
        this.lessonScheduledMstId = lessonScheduledMstId;
        this.lessonScheduledId = lessonScheduledId;
    }

    public LessonScheduledMstDTO(){
        
    }
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

    public int lessonScheduledMstId() {
        return lessonScheduledMstId;
    }

    public int lessonScheduledId() {
        return lessonScheduledId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LessonScheduledMstDTO) obj;
        return this.lessonScheduledMstId == that.lessonScheduledMstId &&
                this.lessonScheduledId == that.lessonScheduledId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonScheduledMstId, lessonScheduledId);
    }

    @Override
    public String toString() {
        return "LessonScheduledMstDTO[" +
                "lessonScheduledMstId=" + lessonScheduledMstId + ", " +
                "lessonScheduledId=" + lessonScheduledId + ']';
    }

}
