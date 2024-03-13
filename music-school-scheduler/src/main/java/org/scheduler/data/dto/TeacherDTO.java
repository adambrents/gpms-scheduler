package org.scheduler.data.dto;

import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.TeachersRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.*;
import java.time.LocalDateTime;

public class TeacherDTO extends DTOBase<TeacherDTO> implements ISqlConvertible<TeacherDTO> {
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String city;
    private String state;
    private String phoneNumber;
    private String email;
    private LocalDateTime createDate;
    private int createdBy;
    private LocalDateTime lastUpdate;
    private int lastUpdatedBy;
    private int userId;

    public TeacherDTO(){
        
    }

    public TeacherDTO(int id,
                      String firstName,
                      String lastName,
                      String addressLine1,
                      String addressLine2,
                      String postalCode,
                      String city,
                      String state,
                      String phoneNumber,
                      String email,
                      LocalDateTime lastUpdate,
                      int lastUpdatedBy) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public TeacherDTO(int id,
                      String firstName,
                      String lastName,
                      String addressLine1,
                      String addressLine2,
                      String postalCode,
                      String city,
                      String state,
                      String phoneNumber,
                      String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public TeacherDTO(String firstName,
                      String lastName,
                      String addressLine1,
                      String addressLine2,
                      String postalCode,
                      String city,
                      String state,
                      String phoneNumber,
                      String email,
                      LocalDateTime createDate,
                      int createdBy,
                      LocalDateTime lastUpdate,
                      int lastUpdatedBy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getFullName(){ return String.format("%s %s", getFirstName(), getLastName()); }

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

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public int getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public String getTeacherInstruments(){
        return (new TeachersRepository()).getTeacherInstrumentsAsString(getId());
    }

    public int getNumberOfStudents(){
        return (new TeachersRepository()).getTeacherStudentsCount(getId());
    }

    @Override
    public String getName(){
        return getFullName();
    }

    @Override
    public IRepository getRepository() {
        return new TeachersRepository();
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.TEACHERS;
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(TeacherDTO teacherDTO, Connection connection) throws SQLException {
        // Added City, State, Last_Update, and Last_Updated_By to the INSERT statement
        String sql = "INSERT INTO " + DB_TABLES.TEACHERS +
                " (Teacher_First_Name, Teacher_Last_Name, Address_Line_1, Address_Line_2, Postal_Code, City, State, Phone, Email, Last_Update, Last_Updated_By) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, teacherDTO.getFirstName());
        statement.setString(2, teacherDTO.getLastName());
        statement.setString(3, teacherDTO.getAddressLine1());
        statement.setString(4, teacherDTO.getAddressLine2());
        statement.setString(5, teacherDTO.getPostalCode());
        statement.setString(6, teacherDTO.getCity());
        statement.setString(7, teacherDTO.getState());
        statement.setString(8, teacherDTO.getPhoneNumber());
        statement.setString(9, teacherDTO.getEmail());
        statement.setTimestamp(10, Timestamp.valueOf(teacherDTO.getLastUpdate()));
        statement.setInt(11, teacherDTO.getLastUpdatedBy());

        return statement;
    }


    @Override
    public PreparedStatement toSqlUpdateQuery(TeacherDTO teacherDTO, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.TEACHERS +
                " SET Teacher_First_Name = ?, " +
                "Teacher_Last_Name = ?, " +
                "Address_Line_1 = ?, " +
                "Address_Line_2 = ?, " +
                "Postal_Code = ?, " +
                "City = ?, " +
                "State = ?, " +
                "Phone = ?, " +
                "Email = ?, " +
                "Last_Update = ?, " +
                "Last_Updated_By = ? " +
                "WHERE Teacher_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, teacherDTO.getFirstName());
        statement.setString(2, teacherDTO.getLastName());
        statement.setString(3, teacherDTO.getAddressLine1());
        statement.setString(4, teacherDTO.getAddressLine2());
        statement.setString(5, teacherDTO.getPostalCode());
        statement.setString(6, teacherDTO.getCity());
        statement.setString(7, teacherDTO.getState());
        statement.setString(8, teacherDTO.getPhoneNumber());
        statement.setString(9, teacherDTO.getEmail());
        statement.setTimestamp(10, Timestamp.valueOf(teacherDTO.getLastUpdate()));
        statement.setInt(11, teacherDTO.getLastUpdatedBy());
        statement.setInt(12, teacherDTO.getId());

        return statement;
    }



    @Override
    public PreparedStatement toSqlDeleteQuery(TeacherDTO teacherDTO, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.TEACHERS + " WHERE Teacher_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, teacherDTO.getId());

        return statement;
    }


    @Override
    public TeacherDTO fromResultSet(ResultSet rs) {
        try {
            return new TeacherDTO(
                    rs.getInt("Teacher_Id"),
                    rs.getString("Teacher_First_Name"),
                    rs.getString("Teacher_Last_Name"),
                    rs.getString("Address_Line_1"),
                    rs.getString("Address_Line_2"),
                    rs.getString("Postal_Code"),
                    rs.getString("City"),
                    rs.getString("State"),
                    rs.getString("Phone"),
                    rs.getString("Email")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
