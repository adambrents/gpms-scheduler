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

public class StudentTeacherDTO extends DTOMappingBase<StudentTeacherDTO> implements ISqlConvertible<StudentTeacherDTO> {
    public StudentTeacherDTO(int id, int mappingFromId, int mappingToId) {
        this.id = id;
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
    }

    public StudentTeacherDTO(int mappingFromId, int mappingToId) {
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
    }

    public StudentTeacherDTO(int mappingFromId, int mappingToId, BooleanProperty selected) {
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
        super.isSelected = selected;
        setMapping();
    }

    @Override
    public Constants.MAPPINGS getMapping(){
        return mapping;
    }

    private void setMapping() {
        super.mapping = Constants.MAPPINGS.StudentTeacher;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM student_teacher";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(StudentTeacherDTO item, Connection connection) throws SQLException {
        String sql = "INSERT INTO student_teacher (Student_Id, Teacher_Id) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getMappingFromId());
        statement.setInt(2, item.getMappingToId());

        return statement;
    }


    @Override
    public PreparedStatement toSqlUpdateQuery(StudentTeacherDTO item, Connection connection) {
        return null;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(StudentTeacherDTO item, Connection connection) throws SQLException {
        String sql = "DELETE FROM student_teacher WHERE Student_Teacher_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId());

        return statement;
    }


    @Override
    public StudentTeacherDTO fromResultSet(ResultSet rs) {
        try {
            return new StudentTeacherDTO(
                    rs.getInt("Student_Teacher_Id"),
                    rs.getInt("Student_Id"),
                    rs.getInt("Teacher_Id")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IRepository getRepository() {
        return new StudentsRepository(); // Ensure you have this repository implemented
    }
}
