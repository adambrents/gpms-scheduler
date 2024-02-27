package org.scheduler.dto;

import org.scheduler.dto.interfaces.ISqlConvertable;
import org.scheduler.repository.configuration.model.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LessonDTO implements ISqlConvertable<LessonDTO> {
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
     * gets appt id
     *
     * @return
     */
    public int getLessonID() {
        return lessonID;
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
    public String toSqlSelectQuery() {
        return String.format("SELECT * FROM %s", DB_TABLES.LESSONS);
    }

    @Override
    public String toSqlInsertQuery(LessonDTO lessonDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("INSERT INTO %s (lessonID, description, location, type, start, end, studentID, userID) " +
                        "VALUES (%d, '%s', '%s', '%s', '%s', '%s', %d, %d)",
                DB_TABLES.LESSONS, lessonDTO.lessonID, lessonDTO.description, lessonDTO.location, lessonDTO.type,
                lessonDTO.start.format(formatter), lessonDTO.end.format(formatter), lessonDTO.studentID, lessonDTO.userID);
    }

    @Override
    public String toSqlUpdateQuery(LessonDTO lessonDTO) {
        // This example updates the description, start, and end for a given lessonID. Adjust as necessary.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("UPDATE %s SET description = '%s', start = '%s', end = '%s' WHERE lessonID = %d",
                DB_TABLES.LESSONS, lessonDTO.description, lessonDTO.start.format(formatter), lessonDTO.end.format(formatter), lessonDTO.lessonID);
    }

    @Override
    public String toSqlDeleteQuery(LessonDTO lessonDTO) {
        return String.format("DELETE FROM %s WHERE lessonID = %d", DB_TABLES.LESSONS, lessonDTO.lessonID);
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
