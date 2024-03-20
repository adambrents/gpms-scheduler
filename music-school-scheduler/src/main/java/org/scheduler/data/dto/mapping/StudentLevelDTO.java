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

public class StudentLevelDTO extends DTOMappingBase<StudentLevelDTO> implements ISqlConvertible<StudentLevelDTO> {
    public StudentLevelDTO(int id, int mappingFromId, int mappingToId) {
        this.id = id;
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
        setMapping();
    }
    public StudentLevelDTO(int mappingFromId, int mappingToId) {
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
        setMapping();
    }
    public StudentLevelDTO() {
        setMapping();
    }

    public StudentLevelDTO(int mappingFromId, int mappingToId, BooleanProperty selected) {
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
        super.mapping = Constants.MAPPINGS.StudentLevel;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM student_level";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }


    @Override
    public PreparedStatement toSqlInsertQuery(StudentLevelDTO item, Connection connection) throws SQLException {
        String sql = "INSERT INTO student_level (Student_Id, Level_Id) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getMappingFromId());
        statement.setInt(2, item.getMappingToId());

        return statement;
    }


    @Override
    public PreparedStatement toSqlUpdateQuery(StudentLevelDTO item, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(StudentLevelDTO item, Connection connection) throws SQLException {
        String sql = "DELETE FROM student_level WHERE Student_Level_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId());

        return statement;
    }

    @Override
    public StudentLevelDTO fromResultSet(ResultSet rs) {
        try {
            return new StudentLevelDTO(
                    rs.getInt("Student_Level_Id"),
                    rs.getInt("Student_Id"),
                    rs.getInt("Level_Id")
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
