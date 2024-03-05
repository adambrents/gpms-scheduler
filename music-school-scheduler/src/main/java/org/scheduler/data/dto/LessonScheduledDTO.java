package org.scheduler.data.dto;

import org.scheduler.data.dto.interfaces.ISqlConvertable;
import org.scheduler.data.configuration.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LessonScheduledDTO implements ISqlConvertable<LessonScheduledDTO> {
    private int lessonsScheduledId;
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

    public LessonScheduledDTO(int lessonsScheduledId, String description, String type, String location, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, boolean goldCup, boolean newStudent, int lessonBookId, int lessonLevelId, int lessonInstrumentId) {
        this.lessonsScheduledId = lessonsScheduledId;
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
    public String toSqlSelectQuery() {
        return String.format("SELECT * FROM %s", DB_TABLES.LESSONS_SCHEDULED);
    }

    @Override
    public String toSqlInsertQuery(LessonScheduledDTO item) {
        return String.format(
                "INSERT INTO %s (Lessons_Scheduled_Id, Description, Type, Location, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Gold_Cup, New_Student, Lesson_Book_Id, Lesson_Level_Id, Lesson_Instrument_Id) VALUES (%d, '%s', '%s', '%s', '%tF %<tT', '%tF %<tT', '%tF %<tT', '%s', '%tF %<tT', '%s', %b, %b, %d, %d, %d)",
                DB_TABLES.LESSONS_SCHEDULED, item.lessonsScheduledId, item.description, item.type, item.location, item.start, item.end, item.createDate, item.createdBy, item.lastUpdate, item.lastUpdatedBy, item.goldCup, item.newStudent, item.lessonBookId, item.lessonLevelId, item.lessonInstrumentId
        );
    }

    @Override
    public String toSqlUpdateQuery(LessonScheduledDTO item) {
        return String.format(
                "UPDATE %s SET Description = '%s', Type = '%s', Location = '%s', Start = '%tF %<tT', End = '%tF %<tT', Create_Date = '%tF %<tT', Created_By = '%s', Last_Update = '%tF %<tT', Last_Updated_By = '%s', Gold_Cup = %b, New_Student = %b, Lesson_Book_Id = %d, Lesson_Level_Id = %d, Lesson_Instrument_Id = %d WHERE Lessons_Scheduled_Id = %d",
                DB_TABLES.LESSONS_SCHEDULED, item.description, item.type, item.location, item.start, item.end, item.createDate, item.createdBy, item.lastUpdate, item.lastUpdatedBy, item.goldCup, item.newStudent, item.lessonBookId, item.lessonLevelId, item.lessonInstrumentId, item.lessonsScheduledId
        );
    }

    @Override
    public String toSqlDeleteQuery(LessonScheduledDTO item) {
        return String.format("DELETE FROM %s WHERE Lessons_Scheduled_Id = %d", DB_TABLES.LESSONS_SCHEDULED, item.lessonsScheduledId);
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
