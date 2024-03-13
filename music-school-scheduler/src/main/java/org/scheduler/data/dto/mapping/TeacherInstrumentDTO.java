package org.scheduler.data.dto.mapping;

import javafx.beans.property.BooleanProperty;
import org.scheduler.app.constants.Constants;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.dto.base.DTOMappingBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.TeachersRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherInstrumentDTO extends DTOMappingBase<TeacherInstrumentDTO> implements ISqlConvertible<TeacherInstrumentDTO> {
    public TeacherInstrumentDTO(int id, int mappingFromId, int mappingToId) {
        this.id = id;
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
    }

    public TeacherInstrumentDTO(int mappingToId) {
        this.mappingToId = mappingToId;
    }

    public TeacherInstrumentDTO(int mappingFromId, int mappingToId, BooleanProperty selected) {
        this.mappingFromId = mappingFromId;
        this.mappingToId = mappingToId;
        this.isSelected = selected;
        setMapping();
    }
    @Override
    public Constants.MAPPINGS getMapping() {
        return mapping;
    }
    private void setMapping() {
        super.mapping = Constants.MAPPINGS.TeacherInstrument;
    }

    public TeacherInstrumentDTO() {
        
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.TEACHER_INSTRUMENTS; // Assuming DB_TABLES.TEACHER_INSTRUMENTS is a constant
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }


    @Override
    public PreparedStatement toSqlInsertQuery(TeacherInstrumentDTO item, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.TEACHER_INSTRUMENTS + " (Teacher_Id, Instrument_Id) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getMappingFromId()); // Assuming getter methods exist; replace with actual field names if not
        statement.setInt(2, item.getMappingToId());

        return statement;
    }


    @Override
    public PreparedStatement toSqlUpdateQuery(TeacherInstrumentDTO item, Connection connection) {
        return null;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(TeacherInstrumentDTO item, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.TEACHER_INSTRUMENTS + " WHERE Teacher_Instrument_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, item.getId());

        return statement;
    }


    @Override
    public IRepository getRepository() {
        return new TeachersRepository();
    }

    @Override
    public TeacherInstrumentDTO fromResultSet(ResultSet rs) {
        try {
            return new TeacherInstrumentDTO(
                    rs.getInt("Teacher_Instrument_Id"),
                    rs.getInt("Teacher_Id"),
                    rs.getInt("Instrument_Id")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
