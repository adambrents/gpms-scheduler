package org.scheduler.viewmodels;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Lesson {
    private final int appointmentID;
    private String title;
    private final String description;
    private final String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private final int customerID;
    private final int userID;
    private final int contactID;
    private final String contactName;

    /**
     * constructor for appointments
     *
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contactID
     */
    public Lesson(int appointmentID, String title, String description, String location, String type, LocalDateTime start,
                  LocalDateTime end, int customerID, int userID, int contactID, String contactName) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.contactName = contactName;
    }

    /**
     * gets appt id
     *
     * @return
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * gets title
     *
     * @return
     */
    public String getTitle() {
        return title;
    }


    /**
     * sets title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;

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
    public int getCustomerID() {
        return customerID;

    }

    /**
     * gets user ID
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * get Contact Id
     * @return
     */
    public int getContactID() {

        return contactID;
    }

    /**
     * gets contact name
     * @return
     */
    public String getContactName() {
        return contactName;
    }
}
