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

public class StudentInstrumentDTO extends DTOMappingBase<StudentInstrumentDTO> implements ISqlConvertible<StudentInstrumentDTO> {

    // Constructors
    public StudentInstrumentDTO(int id, int mappingFromId, int mappingToId) {
        this.id = id;
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
    }
    public StudentInstrumentDTO(int mappingFromId, int mappingToId) {
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
    }
    public StudentInstrumentDTO(int mappingToId) {
        this.mappingToId = mappingToId;
    }
    public StudentInstrumentDTO() {
    }

    public StudentInstrumentDTO(int mappingFromId, int mappingToId, BooleanProperty selected) {
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
        super.mapping = Constants.MAPPINGS.StudentInstrument;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM student_instrument";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(StudentInstrumentDTO item, Connection connection) throws SQLException {
        String sql = "INSERT INTO student_instrument (Student_Id, Instrument_Id) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getMappingFromId());
        statement.setInt(2, item.getMappingToId());

        return statement;
    }


    @Override
    public PreparedStatement toSqlUpdateQuery(StudentInstrumentDTO item, Connection connection) {
        return null;
    }
    @Override
    public PreparedStatement toSqlDeleteQuery(StudentInstrumentDTO item, Connection connection) throws SQLException {
        String sql = "DELETE FROM student_instrument WHERE Student_Instrument_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId());

        return statement;
    }

    @Override
    public StudentInstrumentDTO fromResultSet(ResultSet rs) {
        try {
            return new StudentInstrumentDTO(
                    rs.getInt("Student_Instrument_Id"),
                    rs.getInt("Student_Id"),
                    rs.getInt("Instrument_Id")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IRepository getRepository() {
        return new StudentsRepository();
    }
}
