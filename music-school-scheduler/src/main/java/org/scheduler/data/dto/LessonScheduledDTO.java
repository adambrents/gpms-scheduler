package org.scheduler.data.dto;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.repository.LessonsRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.*;
import java.time.LocalDateTime;

public class LessonScheduledDTO extends DTOBase<LessonScheduledDTO> implements ISqlConvertible<LessonScheduledDTO> {
    private String description;
    private String type;
    private String location;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private boolean goldCup;
    private boolean newStudent;
    private int lessonBookId;
    private int lessonLevelId;
    private int lessonInstrumentId;

    public LessonScheduledDTO() {
        
    }

    public LessonScheduledDTO(int id, String description, String type, String location, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, boolean goldCup, boolean newStudent, int lessonBookId, int lessonLevelId, int lessonInstrumentId) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.location = location;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.goldCup = goldCup;
        this.newStudent = newStudent;
        this.lessonBookId = lessonBookId;
        this.lessonLevelId = lessonLevelId;
        this.lessonInstrumentId = lessonInstrumentId;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.LESSONS_SCHEDULED;
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }


    @Override
    public PreparedStatement toSqlInsertQuery(LessonScheduledDTO item, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.LESSONS_SCHEDULED +
                " (Lessons_Scheduled_Id, Description, Type, Location, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Gold_Cup, New_Student, Lesson_Book_Id, Lesson_Level_Id, Lesson_Instrument_Id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.id);
        statement.setString(2, item.description);
        statement.setString(3, item.type);
        statement.setString(4, item.location);
        statement.setTimestamp(5, Timestamp.valueOf(item.start));
        statement.setTimestamp(6, Timestamp.valueOf(item.end));
        statement.setTimestamp(7, Timestamp.valueOf(item.createDate));
        statement.setString(8, item.createdBy);
        statement.setTimestamp(9, Timestamp.valueOf(item.lastUpdate));
        statement.setString(10, item.lastUpdatedBy);
        statement.setBoolean(11, item.goldCup);
        statement.setBoolean(12, item.newStudent);
        statement.setInt(13, item.lessonBookId);
        statement.setInt(14, item.lessonLevelId);
        statement.setInt(15, item.lessonInstrumentId);

        return statement;
    }


    @Override
    public PreparedStatement toSqlUpdateQuery(LessonScheduledDTO item, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.LESSONS_SCHEDULED +
                " SET Description = ?, Type = ?, Location = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Gold_Cup = ?, New_Student = ?, Lesson_Book_Id = ?, Lesson_Level_Id = ?, Lesson_Instrument_Id = ? WHERE Lessons_Scheduled_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, item.description);
        statement.setString(2, item.type);
        statement.setString(3, item.location);
        statement.setTimestamp(4, Timestamp.valueOf(item.start));
        statement.setTimestamp(5, Timestamp.valueOf(item.end));
        statement.setTimestamp(6, Timestamp.valueOf(item.createDate));
        statement.setString(7, item.createdBy);
        statement.setTimestamp(8, Timestamp.valueOf(item.lastUpdate));
        statement.setString(9, item.lastUpdatedBy);
        statement.setBoolean(10, item.goldCup);
        statement.setBoolean(11, item.newStudent);
        statement.setInt(12, item.lessonBookId);
        statement.setInt(13, item.lessonLevelId);
        statement.setInt(14, item.lessonInstrumentId);
        statement.setInt(15, item.id);

        return statement;
    }
    @Override
    public PreparedStatement toSqlDeleteQuery(LessonScheduledDTO item, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.LESSONS_SCHEDULED + " WHERE Lessons_Scheduled_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.id);

        return statement;
    }

    @Override
    public IRepository getRepository() {
        return new LessonsRepository();
    }

    @Override
    public LessonScheduledDTO fromResultSet(ResultSet rs) {
        try {
            return new LessonScheduledDTO(
                    rs.getInt("lessons_scheduled_Id"),
                    rs.getString("Description"),
                    rs.getString("Type"),
                    rs.getString("Location"),
                    rs.getTimestamp("Start").toLocalDateTime(),
                    rs.getTimestamp("End").toLocalDateTime(),
                    rs.getTimestamp("Create_Date").toLocalDateTime(),
                    rs.getString("Created_By"),
                    rs.getTimestamp("Last_Update").toLocalDateTime(),
                    rs.getString("Last_Updated_By"),
                    rs.getBoolean("Gold_Cup"),
                    rs.getBoolean("New_Student"),
                    rs.getInt("Lesson_Book_Id"),
                    rs.getInt("Lesson_Level_Id"),
                    rs.getInt("Lesson_Instrument_Id")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
