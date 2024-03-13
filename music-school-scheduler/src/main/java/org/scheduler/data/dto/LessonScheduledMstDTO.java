package org.scheduler.data.dto;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.repository.LessonsRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class LessonScheduledMstDTO extends DTOBase<LessonScheduledMstDTO> implements ISqlConvertible<LessonScheduledMstDTO> {
    private int id;
    private int lessonScheduledId;

    public LessonScheduledMstDTO(int id, int lessonScheduledId) {
        this.id = id;
        this.lessonScheduledId = lessonScheduledId;
    }

    public LessonScheduledMstDTO(){
        
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.LESSONS_SCHEDULED_MST;
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }
    @Override
    public PreparedStatement toSqlInsertQuery(LessonScheduledMstDTO item, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.LESSONS_SCHEDULED_MST + " (LESSONS_SCHEDULED_MST_Id, Lesson_Scheduled_Id) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId());
        statement.setInt(2, item.lessonScheduledId());

        return statement;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(LessonScheduledMstDTO item, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.LESSONS_SCHEDULED_MST + " SET Lesson_Scheduled_Id = ? WHERE LESSONS_SCHEDULED_MST_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.lessonScheduledId());
        statement.setInt(2, item.getId());

        return statement;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(LessonScheduledMstDTO item, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.LESSONS_SCHEDULED_MST + " WHERE LESSONS_SCHEDULED_MST_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId());

        return statement;
    }

    @Override
    public IRepository getRepository() {
        return new LessonsRepository();
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

    public int lessonScheduledId() {
        return lessonScheduledId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LessonScheduledMstDTO) obj;
        return this.id == that.id &&
                this.lessonScheduledId == that.lessonScheduledId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lessonScheduledId);
    }

    @Override
    public String toString() {
        return "LessonScheduledMstDTO[" +
                "lessonScheduledMstId=" + id + ", " +
                "lessonScheduledId=" + lessonScheduledId + ']';
    }

}
