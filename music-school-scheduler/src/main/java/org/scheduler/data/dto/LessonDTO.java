package org.scheduler.data.dto;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.repository.LessonsRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LessonDTO extends DTOBase<LessonDTO> implements ISqlConvertible<LessonDTO> {
    private int lessonID;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int studentID;
    private int userID;

    public LessonDTO(){
        
    }

    /**
     * constructor for LessonDTO
     *
     * @param lessonID
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param userID
     */
    public LessonDTO(int lessonID, String description, String location, String type, LocalDateTime start,
                     LocalDateTime end, int userID) {
        this.lessonID = lessonID;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.userID = userID;
    }

    /**
     * gets description
     * @return
     */
    public String getDescription() {
        return description;
    }
//
//    /**
//     * sets description
// 
//// 
//     * @param description
// 
////     */
////    public void setDescription(String description) {
////        this.description = description;
//// 
// 
//    }

    /**
     * gets location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * gets type
     * @return
     */
    public String getType() {

        return type;
    }

    /**
     * sets type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * gets start
     * @return
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * sets start
     * @param start
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }


    /**
     * gets end

     * @return
     */
    public LocalDateTime getEnd() {

        return end;
    }

    public LocalTime getEndTime() {return end.toLocalTime();}

    public LocalTime getStartTime() {return start.toLocalTime();}
     public LocalDate getDate() {return start.toLocalDate();}
    /**
     * sets end
     * @param end
     */

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * gets customerID
     * @return
     */
    public int getStudentID() {
        return studentID;

    }

    /**
     * gets user ID
     * @return
     */
    public int getUserID() {
        return userID;
    }

    public void setStudentId(int customerID) {
        this.studentID = customerID;
    }
    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.LESSONS;
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(LessonDTO lessonDTO, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.LESSONS + " (lessonID, description, location, type, start, end, studentID, userID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, lessonDTO.lessonID);
        statement.setString(2, lessonDTO.description);
        statement.setString(3, lessonDTO.location);
        statement.setString(4, lessonDTO.type);
        statement.setTimestamp(5, Timestamp.valueOf(lessonDTO.start));
        statement.setTimestamp(6, Timestamp.valueOf(lessonDTO.end));
        statement.setInt(7, lessonDTO.studentID);
        statement.setInt(8, lessonDTO.userID);

        return statement;
    }
    @Override
    public PreparedStatement toSqlUpdateQuery(LessonDTO lessonDTO, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.LESSONS + " SET description = ?, start = ?, end = ? WHERE lessonID = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, lessonDTO.description);
        statement.setTimestamp(2, Timestamp.valueOf(lessonDTO.start));
        statement.setTimestamp(3, Timestamp.valueOf(lessonDTO.end));
        statement.setInt(4, lessonDTO.lessonID);

        return statement;
    }
    @Override
    public PreparedStatement toSqlDeleteQuery(LessonDTO lessonDTO, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.LESSONS + " WHERE lessonID = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, lessonDTO.lessonID);

        return statement;
    }

    @Override
    public IRepository getRepository() {
        return new LessonsRepository();
    }

    @Override
    public LessonDTO fromResultSet(ResultSet rs) {
        try {
            return new LessonDTO(
                    rs.getInt("Lesson_ID"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    rs.getTimestamp("Start").toLocalDateTime(),
                    rs.getTimestamp("End").toLocalDateTime(),
                    rs.getInt("User_ID"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
