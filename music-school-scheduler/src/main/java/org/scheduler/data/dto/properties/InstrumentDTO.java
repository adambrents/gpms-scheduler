package org.scheduler.data.dto.properties;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISelectableDTO;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.repository.interfaces.IRepository;
import org.scheduler.data.repository.properties.InstrumentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class InstrumentDTO extends DTOBase<InstrumentDTO> implements ISelectableDTO<InstrumentDTO> {

    public InstrumentDTO(int Id, String name) {
        super.id = Id;
        super.name = name;
    }

    public InstrumentDTO() {
        
    }

    public InstrumentDTO(String name) {
        super.name = name;
    }
    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.INSTRUMENTS; // Assuming DB_TABLES.INSTRUMENTS is a constant
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }
    @Override
    public PreparedStatement toSqlInsertQuery(InstrumentDTO instrumentDTO, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.INSTRUMENTS + " (Instrument_Id, Name) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, instrumentDTO.getId());
        statement.setString(2, instrumentDTO.getName());

        return statement;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(InstrumentDTO instrumentDTO, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.INSTRUMENTS + " SET Name = ? WHERE Instrument_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, instrumentDTO.getName());
        statement.setInt(2, instrumentDTO.getId());

        return statement;
    }
    @Override
    public PreparedStatement toSqlDeleteQuery(InstrumentDTO instrumentDTO, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.INSTRUMENTS + " WHERE Instrument_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, instrumentDTO.getId());

        return statement;
    }


    @Override
    public IRepository getRepository() {
        return new InstrumentRepository();
    }

    @Override
    public InstrumentDTO fromResultSet(ResultSet rs) {
        try {
            int instrumentId = rs.getInt("Instrument_Id");
            String instrumentName = rs.getString("Name");
            return new InstrumentDTO(instrumentId, instrumentName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (InstrumentDTO) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "InstrumentDTO[" +
                "instrumentId=" + id + ", " +
                "instrumentName=" + name + ']';
    }

}
