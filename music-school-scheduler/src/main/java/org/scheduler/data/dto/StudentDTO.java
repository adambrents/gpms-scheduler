package org.scheduler.data.dto;

import org.scheduler.data.dto.interfaces.ISqlConvertable;
import org.scheduler.data.configuration.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class StudentDTO implements ISqlConvertable<StudentDTO> {
    private int lastUpdateBy;
    private int createBy;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private int studentId;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String phoneNumber;
    private int currentBookId;
    private int currentLevelId;
    private boolean isGoldCup;

    public StudentDTO(){
        
    }
    /**
     * constructor for customer
     *
     * @param studentId
     * @param firstName
     * @param lastName
     * @param addressLine1
     * @param addressLine2
     * @param postalCode
     * @param phoneNumber
     */
    public StudentDTO(int studentId,
                      String firstName,
                      String lastName,
                      String addressLine1,
                      String addressLine2,
                      String postalCode,
                      String phoneNumber,
                      LocalDateTime createDate,
                      int createBy,
                      LocalDateTime lastUpdate,
                      int lastUpdateBy,
                      int currentBookId,
                      int currentLevelId,
                      boolean isGoldCup) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.currentBookId = currentBookId;
        this.currentLevelId = currentLevelId;
        this.isGoldCup = isGoldCup;
    }

    public StudentDTO(String firstName, String lastName, String addressLine1, String addressLine2, String postalCode, String phoneNumber,
                      LocalDateTime createDate, int createBy, LocalDateTime lastUpdate, int lastUpdateBy, int currentBookId, int currentLevelId,
                      boolean isGoldCup) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.currentBookId = currentBookId;
        this.currentLevelId = currentLevelId;
        this.isGoldCup = isGoldCup;
    }

    /**
     * getter/setter for StudentDTO
     *
     * @return
     */
    public int getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getAddressLine1() {
        return addressLine1;
    }
    public String getAddressLine2() {
        return addressLine2;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName(){ return String.format("%s %s", getFirstName(), getLastName()); }

    public int getCurrentLevelId() {
        return currentLevelId;
    }
    private int getCurrentBookId() {
        return this.currentBookId;
    }

    public int getLastUpdateBy() {
        return lastUpdateBy;
    }

    public int getCreateBy() {
        return createBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public String toSqlSelectQuery() {
        // Assuming the table name for students is STUDENTS as per your DB_TABLES
        return String.format("SELECT * FROM %s", DB_TABLES.STUDENTS);
    }

    @Override
    public String toSqlInsertQuery(StudentDTO studentDTO) {
        return String.format("INSERT INTO %s " +
                        "(Student_First_Name, Student_Last_Name, Address_Line_1, Address_Line_2, Postal_Code, Phone, Create_Date, " +
                        "Created_By, Last_Update, Last_Updated_By, Current_Book_Id, Current_Level_Id) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', NOW(), '%s', NOW(), '%s', %d, %d)",
                DB_TABLES.STUDENTS, studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getAddressLine1(),
                studentDTO.getAddressLine2(), studentDTO.getPostalCode(), studentDTO.getPhoneNumber(),
                studentDTO.getCreateBy(), studentDTO.getLastUpdateBy(), studentDTO.getCurrentBookId(), studentDTO.getCurrentLevelId());
    }

    @Override
    public String toSqlUpdateQuery(StudentDTO studentDTO) {
        return String.format(
                        "UPDATE students SET " +
                        "Student_First_Name = '%s', " +
                        "Student_Last_Name = '%s', " +
                        "Address_Line_1 = '%s', " +
                        "Address_Line_2 = '%s', " +
                        "Postal_Code = '%s', " +
                        "Phone = '%s', " +
                        "Created_By = '%s', " +
                        "Last_Update = NOW(), " +
                        "Last_Updated_By = '%s', " +
                        "Current_Book_Id = %d, " +
                        "Current_Level_Id = %d " +
                        "WHERE Student_ID = %d",
                studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getAddressLine1(),
                studentDTO.getAddressLine2(), studentDTO.getPostalCode(), studentDTO.getPhoneNumber(),
                studentDTO.getCreateDate(), studentDTO.getCreateBy(), studentDTO.getLastUpdateBy(),
                studentDTO.getCurrentBookId(), studentDTO.getCurrentLevelId(), studentDTO.getStudentId());
    }


    @Override
    public String toSqlDeleteQuery(StudentDTO studentDTO) {
        return String.format("DELETE FROM %s WHERE Id = %d", DB_TABLES.STUDENTS, studentDTO.getStudentId());
    }

    @Override
    public StudentDTO fromResultSet(ResultSet rs) {
        try {
            return new StudentDTO(
                    rs.getInt("Student_ID"),
                    rs.getString("Student_First_Name"),
                    rs.getString("Student_Last_Name"),
                    rs.getString("Address_Line_1"),
                    rs.getString("Address_Line_2"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    rs.getTimestamp("Create_Date").toLocalDateTime(),
                    rs.getInt("Created_By"),
                    rs.getTimestamp("Last_Update").toLocalDateTime(),
                    rs.getInt("Last_Updated_By"),
                    rs.getInt("Current_Book_Id"),
                    rs.getInt("Current_Level_Id"),
                    rs.getBoolean("Gold_Cup"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
