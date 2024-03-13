package org.scheduler.data.dto;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISelectableDTO;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.repository.StudentsRepository;
import org.scheduler.data.repository.TeachersRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.*;
import java.time.LocalDateTime;

public class StudentDTO extends DTOBase<StudentDTO> implements ISelectableDTO<StudentDTO> {
    private int lastUpdateBy;
    private int createBy;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String phoneNumber;
    private boolean isGoldCup;
    private String city;
    private String state;
    private String email;

    public StudentDTO(){
        
    }
    /**
     * constructor for customer
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param addressLine1
     * @param addressLine2
     * @param postalCode
     * @param phoneNumber
     */
    public StudentDTO(int id,
                      String firstName,
                      String lastName,
                      String addressLine1,
                      String addressLine2,
                      String postalCode,
                      String city,
                      String state,
                      String phoneNumber,
                      String email,
                      LocalDateTime createDate,
                      int createBy,
                      LocalDateTime lastUpdate,
                      int lastUpdateBy,
                      boolean isGoldCup) {
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
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.isGoldCup = isGoldCup;
    }

    public StudentDTO(String firstName, String lastName, String addressLine1, String addressLine2, String postalCode, String phoneNumber,
                      LocalDateTime createDate, int createBy, LocalDateTime lastUpdate, int lastUpdateBy,
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
        this.isGoldCup = isGoldCup;
    }

    public StudentDTO(int id,
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
                      int lastUpdateBy,
                      boolean isGoldCup) {
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
        this.lastUpdateBy = lastUpdateBy;
        this.isGoldCup = isGoldCup;
    }

    /**
     * getter/setter for StudentDTO
     *
     * @return
     */

    @Override
    public String getName() {
        return getFullName();
    }

    @Override
    public IRepository getRepository() {
        return new StudentsRepository();
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

    public String getIsGoldCup() {
        if(isGoldCup()){
            return "Yes";
        }
        else{
            return "No";
        }
    }

    public boolean isGoldCup() {
        return isGoldCup;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getEmail() {
        return email;
    }
    public String getStudentInstruments(){
        return (new StudentsRepository()).getStudentInstrumentsAsString(getId());
    }
    public String getStudentLevels(){
        return (new StudentsRepository()).getStudentLevelsAsString(getId());
    }
    public String getStudentBooks(){
        return (new StudentsRepository()).getStudentBooksAsString(getId());
    }
    public String getStudentTeachers(){
        return (new StudentsRepository()).getStudentTeachersAsString(getId());
    }


    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.STUDENTS; // Direct concatenation as table name is a constant
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }


    @Override
    public PreparedStatement toSqlInsertQuery(StudentDTO studentDTO, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.STUDENTS +
                " (Student_First_Name, Student_Last_Name, Address_Line_1, Address_Line_2, Postal_Code, City, State, Phone, Email, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Gold_Cup) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, studentDTO.getFirstName());
        statement.setString(2, studentDTO.getLastName());
        statement.setString(3, studentDTO.getAddressLine1());
        statement.setString(4, studentDTO.getAddressLine2());
        statement.setString(5, studentDTO.getPostalCode());
        statement.setString(6, studentDTO.getCity());
        statement.setString(7, studentDTO.getState());
        statement.setString(8, studentDTO.getPhoneNumber());
        statement.setString(9, studentDTO.getEmail());
        statement.setInt(10, studentDTO.getCreateBy());
        statement.setInt(11, studentDTO.getLastUpdateBy());
        statement.setBoolean(12, studentDTO.isGoldCup());

        return statement;
    }



    @Override
    public PreparedStatement toSqlUpdateQuery(StudentDTO studentDTO, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.STUDENTS + " SET " +
                "Student_First_Name = ?, " +
                "Student_Last_Name = ?, " +
                "Address_Line_1 = ?, " +
                "Address_Line_2 = ?, " +
                "Postal_Code = ?, " +
                "City = ?, " +
                "State = ?, " +
                "Phone = ?, " +
                "Email = ?, " +
                "Created_By = ?, " + // Assuming you might want to update this
                "Last_Update = NOW(), " +
                "Last_Updated_By = ?, " +
                "Gold_Cup = ? " +
                "WHERE Student_ID = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, studentDTO.getFirstName());
        statement.setString(2, studentDTO.getLastName());
        statement.setString(3, studentDTO.getAddressLine1());
        statement.setString(4, studentDTO.getAddressLine2());
        statement.setString(5, studentDTO.getPostalCode());
        statement.setString(6, studentDTO.getCity());
        statement.setString(7, studentDTO.getState());
        statement.setString(8, studentDTO.getPhoneNumber());
        statement.setString(9, studentDTO.getEmail());
        statement.setInt(10, studentDTO.getCreateBy());
        statement.setInt(11, studentDTO.getLastUpdateBy());
        statement.setBoolean(12, studentDTO.isGoldCup());
        statement.setInt(13, studentDTO.getId());

        return statement;
    }


    @Override
    public PreparedStatement toSqlDeleteQuery(StudentDTO studentDTO, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.STUDENTS + " WHERE Student_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, studentDTO.getId());

        return statement;
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
                    rs.getString("City"),
                    rs.getString("State"),
                    rs.getString("Phone"),
                    rs.getString("Email"),
                    rs.getTimestamp("Create_Date").toLocalDateTime(),
                    rs.getInt("Created_By"),
                    rs.getTimestamp("Last_Update").toLocalDateTime(),
                    rs.getInt("Last_Updated_By"),
                    rs.getBoolean("Gold_Cup"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
