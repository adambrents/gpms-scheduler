package org.scheduler.data.dto.reports;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NumberStudentsInstrumentsDTO extends DTOBase<NumberStudentsInstrumentsDTO> {
    private int studentCount;
    private String instrumentName;

    public NumberStudentsInstrumentsDTO(int studentCount, String instrumentName) {
        this.studentCount = studentCount;
        this.instrumentName = instrumentName;
    }

    public NumberStudentsInstrumentsDTO() {
    }

    public int getStudentCount() {
        return studentCount;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(NumberStudentsInstrumentsDTO item, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(NumberStudentsInstrumentsDTO item, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(NumberStudentsInstrumentsDTO item, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public NumberStudentsInstrumentsDTO fromResultSet(ResultSet rs) {
        try {
            return new NumberStudentsInstrumentsDTO(
                    rs.getInt("Student_Count"),
                    rs.getString("Instrument_Name")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IRepository getRepository() {
        return null;
    }
}
