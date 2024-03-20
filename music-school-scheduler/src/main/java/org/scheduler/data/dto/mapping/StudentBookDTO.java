package org.scheduler.data.dto.mapping;

import javafx.beans.property.BooleanProperty;
import org.scheduler.app.constants.Constants;
import org.scheduler.data.dto.base.DTOMappingBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.StudentsRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentBookDTO extends DTOMappingBase<StudentBookDTO> implements ISqlConvertible<StudentBookDTO> {

    public StudentBookDTO(int id, int mappingFromId, int mappingToId) {
        this.id = id;
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
        setMapping();
    }
    public StudentBookDTO(int mappingFromId, int mappingToId) {
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
        setMapping();
    }
    public StudentBookDTO() {
        setMapping();
    }

    public StudentBookDTO(int mappingFromId, int mappingToId, BooleanProperty selected) {
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
        super.isSelected = selected;
        setMapping();
    }

    @Override
    public Constants.MAPPINGS getMapping(){
        return super.mapping;
    }

    private void setMapping() {
        super.mapping = Constants.MAPPINGS.StudentBook;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM student_book";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(StudentBookDTO item, Connection connection) throws SQLException {
        String sql = "INSERT INTO student_book (Student_Id, Book_Id) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getMappingFromId()); // Assume these are the correct getter methods.
        statement.setInt(2, item.getMappingToId());

        return statement;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(StudentBookDTO item, Connection connection) throws SQLException {
        String sql = "UPDATE student_book SET Student_Id = ?, Book_Id = ? WHERE Student_Book_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getMappingFromId()); // Adjust if your DTO has getter methods.
        statement.setInt(2, item.getMappingToId());
        statement.setInt(3, item.getId()); // Adjust accordingly.

        return statement;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(StudentBookDTO item, Connection connection) throws SQLException {
        String sql = "DELETE FROM student_book WHERE Student_Book_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId()); // Ensure this matches your DTO's structure.

        return statement;
    }


    @Override
    public StudentBookDTO fromResultSet(ResultSet rs) {
        try {
            return new StudentBookDTO(
                    rs.getInt("Student_Book_Id"),
                    rs.getInt("Student_Id"),
                    rs.getInt("Book_Id")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IRepository getRepository() {
        return new StudentsRepository(); // Adjust to your actual repository class
    }
}
