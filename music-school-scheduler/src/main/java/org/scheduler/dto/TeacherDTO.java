package org.scheduler.dto;

import org.scheduler.repository.configuration.model.DB_TABLES;
import org.scheduler.dto.interfaces.ISqlConvertable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherDTO implements ISqlConvertable<TeacherDTO> {
    private int teacherId;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String phoneNumber;
    private String email;

    public TeacherDTO(){

    }
    public TeacherDTO(int teacherId, String firstName, String lastName,
                      String addressLine1, String addressLine2, String postalCode,
                      String phoneNumber, String email) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    @Override
    public String toSqlSelectQuery() {
        return String.format("SELECT * FROM %s", DB_TABLES.TEACHERS);
    }

    @Override
    public String toSqlInsertQuery(TeacherDTO teacherDTO) {
        return String.format("INSERT INTO %s (Teacher_Id, First_Name, Last_Name, Address_Line_1, Address_Line_2, Postal_Code, Phone_Number, Email) " +
                        "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                DB_TABLES.TEACHERS, teacherId, firstName, lastName, addressLine1, addressLine2, postalCode, phoneNumber, email);
    }

    @Override
    public String toSqlUpdateQuery(TeacherDTO teacherDTO) {
        return String.format("UPDATE %s SET Email = '%s', Phone_Number = '%s' WHERE Teacher_Id = %d",
                DB_TABLES.TEACHERS, email, phoneNumber, teacherId);
    }

    @Override
    public String toSqlDeleteQuery(TeacherDTO teacherDTO) {
        return String.format("DELETE FROM %s WHERE Teacher_Id = %d", DB_TABLES.TEACHERS, teacherId);
    }

    @Override
    public TeacherDTO fromResultSet(ResultSet rs) {
        try {
            return new TeacherDTO(
                rs.getInt("Teacher_Id"),
                rs.getString("First_Name"),
                rs.getString("Last_Name"),
                rs.getString("Address_Line_1"),
                rs.getString("Address_Line_2"),
                rs.getString("Postal_Code"),
                rs.getString("Phone_Number"),
                rs.getString("Email")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
