package org.scheduler.viewmodels;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Lesson {
    private final int lessonID;
    private final String description;
    private final String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int studentID;
    private final int userID;

    /**
     * constructor for appointments
     *
     * @param lessonID
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param userID
     */
    public Lesson(int lessonID, String description, String location, String type, LocalDateTime start,
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
}
