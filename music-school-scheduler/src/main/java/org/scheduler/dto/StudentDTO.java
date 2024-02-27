package org.scheduler.dto;

import org.scheduler.dto.interfaces.ISqlConvertable;
import org.scheduler.repository.configuration.model.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDTO implements ISqlConvertable<StudentDTO> {
    private int Id;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String phoneNumber;

    public StudentDTO(){

    }
    /**
     * constructor for customer
     *
     * @param Id
     * @param firstName
     * @param lastName
     * @param addressLine1
     * @param addressLine2
     * @param postalCode
     * @param phoneNumber
     */
    public StudentDTO(int Id, String firstName, String lastName, String addressLine1, String addressLine2, String postalCode, String phoneNumber) {
        this.Id = Id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    /**
     * getter/setter for StudentDTO
     *
     * @return
     */
    public int getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * getter/setter for StudentDTO
     *
     * @return
     */
    public String getAddressLine1() {
        return addressLine1;
    }


    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * getter/setter for StudentDTO
     *
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * getter/setter for StudentDTO
     *
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName(){ return String.format("%s %s", getFirstName(), getLastName()); }

    @Override
    public String toSqlSelectQuery() {
        // Assuming the table name for students is STUDENTS as per your DB_TABLES
        return String.format("SELECT * FROM %s", DB_TABLES.STUDENTS);
    }

    @Override
    public String toSqlInsertQuery(StudentDTO studentDTO) {
        // Assuming the column names in the database match the DTO field names but with SQL naming conventions
        return String.format("INSERT INTO %s (Id, firstName, lastName, addressLine1, addressLine2, postalCode, phoneNumber) " +
                        "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s')",
                DB_TABLES.STUDENTS, studentDTO.getId(), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getAddressLine1(),
                studentDTO.getAddressLine2(), studentDTO.getPostalCode(), studentDTO.getPhoneNumber());
    }

    @Override
    public String toSqlUpdateQuery(StudentDTO studentDTO) {
        // Example updates phoneNumber for a given student Id. Adjust as necessary.
        return String.format("UPDATE %s SET phoneNumber = '%s' WHERE Id = %d",
                DB_TABLES.STUDENTS, studentDTO.getPhoneNumber(), studentDTO.getId());
    }

    @Override
    public String toSqlDeleteQuery(StudentDTO studentDTO) {
        return String.format("DELETE FROM %s WHERE Id = %d", DB_TABLES.STUDENTS, studentDTO.getId());
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
                    rs.getString("Phone"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
