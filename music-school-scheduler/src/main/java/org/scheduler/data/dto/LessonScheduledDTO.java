package org.scheduler.data.dto;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.dto.properties.LevelDTO;
import org.scheduler.data.repository.LessonsRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
//import java.time.format.TextStyle;


public class LessonScheduledDTO extends DTOBase<LessonScheduledDTO> implements ISqlConvertible<LessonScheduledDTO> {

    private String description;
    private String type;
    private String location;
    private LocalTime start;
    private LocalTime end;
    private DayOfWeek dayOfWeek;
    private LocalDate date;
    private boolean goldCup;
    private boolean newStudent;
    private LevelDTO level;
    private BookDTO book;
    private InstrumentDTO instrument;
    private int lessonId;

    public LessonScheduledDTO() {
        
    }

    public LessonScheduledDTO(int id,
                              String description,
                              String type,
                              String location,
                              LocalTime start,
                              LocalTime end,
                              DayOfWeek dayOfWeek,
                              LocalDate date,
                              LocalDateTime createDate,
                              int createdBy,
                              LocalDateTime lastUpdate,
                              int lastUpdatedBy,
                              boolean goldCup,
                              boolean newStudent,
                              int bookId,
                              int levelId,
                              int instrumentId,
                              int lessonId) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.location = location;
        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.createDate = createDate;
        this.createdBy = createdBy;
        super.updateDate = lastUpdate;
        super.updatedBy = lastUpdatedBy;
        this.goldCup = goldCup;
        this.newStudent = newStudent;
        this.book = new BookDTO(bookId);
        this.level = new LevelDTO(levelId);
        this.instrument = new InstrumentDTO(instrumentId);
        this.lessonId = lessonId;
    }

    public LessonScheduledDTO(String description,
                              LocalTime start,
                              LocalTime end,
                              DayOfWeek dayOfWeek,
                              LocalDateTime createDate,
                              int createdBy,
                              LocalDateTime lastUpdate,
                              int lastUpdatedBy,
                              boolean newStudent,
                              BookDTO book,
                              LevelDTO level,
                              InstrumentDTO instrument,
                              int lessonId) {
        this.description = description;
        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
        this.createDate = createDate;
        this.createdBy = createdBy;
        super.updateDate = lastUpdate;
        super.updatedBy = lastUpdatedBy;
        this.newStudent = newStudent;
        this.book = book;
        this.level = level;
        this.instrument = instrument;
        this.lessonId = lessonId;
    }

    public LessonScheduledDTO(int id,
                              String description,
                              LocalTime start,
                              LocalTime end,
                              DayOfWeek dayOfWeek,
                              LocalDateTime lastUpdate,
                              int lastUpdatedBy,
                              boolean isNewStudent,
                              BookDTO book,
                              LevelDTO level,
                              InstrumentDTO instrument,
                              int lessonId) {
        super.id = id;
        this.description = description;
        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
        super.updateDate = lastUpdate;
        super.updatedBy = lastUpdatedBy;
        this.newStudent = isNewStudent;
        this.book = book;
        this.level = level;
        this.instrument = instrument;
        this.lessonId = lessonId;
    }
    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public DayOfWeek getDayOfWeek(){
        return this.dayOfWeek;
    }

    public LevelDTO getLevel() {
        return level;
    }

    public BookDTO getBook() {
        return book;
    }

    public InstrumentDTO getInstrument() {
        return instrument;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isGoldCup() {
        return goldCup;
    }

    public boolean isNewStudent() {
        return newStudent;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLevel(LevelDTO level) {
        this.level = level;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public void setInstrument(InstrumentDTO instrument) {
        this.instrument = instrument;
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
                " (Description, Type, Location, Start, End, DayOfWeek, Date, Create_Date, Created_By, Last_Update, Last_Updated_By, Gold_Cup, New_Student, Lesson_Book_Id, Lesson_Level_Id, Lesson_Instrument_Id, Lesson_Id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";



        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, item.getDescription());
        statement.setString(2, item.getType());
        statement.setString(3, item.getLocation());
        statement.setTime(4, Time.valueOf(item.getStart()));
        statement.setTime(5, Time.valueOf(item.getEnd()));
        statement.setString(6,item.getDayOfWeek().toString());
        if (item.getDate() != null) statement.setDate(7, Date.valueOf(item.getDate()));
        else statement.setNull(7, java.sql.Types.DATE);
        statement.setTimestamp(8, Timestamp.valueOf(item.getCreateDate()));
        statement.setInt(9, item.getCreatedBy());
        statement.setTimestamp(10, Timestamp.valueOf(item.getUpdateDate()));
        statement.setInt(11, item.getUpdatedBy());
        statement.setBoolean(12, item.isGoldCup());
        statement.setBoolean(13, item.isNewStudent());
        statement.setInt(14, item.getBook().getId());
        statement.setInt(15, item.getLevel().getId());
        statement.setInt(16, item.getInstrument().getId());
        statement.setInt(17, item.lessonId);

        return statement;
    }



    @Override
    public PreparedStatement toSqlUpdateQuery(LessonScheduledDTO item, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.LESSONS_SCHEDULED +
                " SET Description = ?, Type = ?, Location = ?, Start = ?, End = ?, DayOfWeek = ?, Date = ?, Last_Update = ?, Last_Updated_By = ?, Gold_Cup = ?, New_Student = ?, Lesson_Book_Id = ?, Lesson_Level_Id = ?, Lesson_Instrument_Id = ? WHERE Lessons_Scheduled_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, item.description);
        statement.setString(2, item.type);
        statement.setString(3, item.location);
        statement.setTime(4, Time.valueOf(item.start));
        statement.setTime(5, Time.valueOf(item.end));
        statement.setString(6, item.dayOfWeek.toString());
        if (item.getDate() != null) statement.setDate(7, Date.valueOf(item.getDate()));
        else statement.setNull(7, java.sql.Types.DATE);
        statement.setTimestamp(8, Timestamp.valueOf(item.updateDate));
        statement.setInt(9, item.updatedBy);
        statement.setBoolean(10, item.goldCup);
        statement.setBoolean(11, item.newStudent);
        statement.setInt(12, item.book.getId());
        statement.setInt(13, item.level.getId());
        statement.setInt(14, item.instrument.getId());
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
            Date date = rs.getDate("Date");
            LocalDate localDate = (date != null) ? date.toLocalDate() : null;

            Timestamp createDateTimestamp = rs.getTimestamp("Create_Date");
            LocalDateTime createDate = (createDateTimestamp != null) ? createDateTimestamp.toLocalDateTime() : null;
            Timestamp lastUpdateTimestamp = rs.getTimestamp("Last_Update");
            LocalDateTime lastUpdate = (lastUpdateTimestamp != null) ? lastUpdateTimestamp.toLocalDateTime() : null;

            Time startTime = rs.getTime("Start");
            LocalTime startLocalTime = (startTime != null) ? startTime.toLocalTime() : null;

            Time endTime = rs.getTime("Start");
            LocalTime endLocalTime = (endTime != null) ? endTime.toLocalTime() : null;

            String dayofWeekString = rs.getString("DayOfWeek");
            DayOfWeek dayofWeek = (dayofWeekString != null) ? DayOfWeek.valueOf(dayofWeekString.toUpperCase()) : null;

            return new LessonScheduledDTO(
                    rs.getInt("lessons_scheduled_Id"),
                    rs.getString("Description"),
                    rs.getString("Type"),
                    rs.getString("Location"),
                    startLocalTime,
                    endLocalTime,
                    dayofWeek,
                    localDate,
                    createDate,
                    rs.getInt("Created_By"),
                    lastUpdate,
                    rs.getInt("Last_Updated_By"),
                    rs.getBoolean("Gold_Cup"),
                    rs.getBoolean("New_Student"),
                    rs.getInt("Lesson_Book_Id"),
                    rs.getInt("Lesson_Level_Id"),
                    rs.getInt("Lesson_Instrument_Id"),
                    rs.getInt("Lesson_Id")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
}
