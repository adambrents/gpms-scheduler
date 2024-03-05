package org.scheduler.data.dto.properties;

import org.scheduler.data.dto.interfaces.ISqlConvertable;
import org.scheduler.data.configuration.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class InstrumentDTO implements ISqlConvertable<InstrumentDTO> {
    private int id;
    private String name;

    public InstrumentDTO(int Id, String name) {
        this.id = Id;
        this.name = name;
    }

    public InstrumentDTO() {
        
    }

    @Override
    public String toSqlSelectQuery() {
        return String.format("SELECT * FROM %s", DB_TABLES.INSTRUMENTS);
    }

    @Override
    public String toSqlInsertQuery(InstrumentDTO instrumentDTO) {
        return String.format("INSERT INTO %s (Instrument_Id, Name) VALUES (%d, '%s')",
                DB_TABLES.INSTRUMENTS, instrumentDTO.instrumentId(), instrumentDTO.instrumentName());
    }

    @Override
    public String toSqlUpdateQuery(InstrumentDTO instrumentDTO) {
        return String.format("UPDATE %s SET Name = '%s' WHERE Instrument_Id = %d",
                DB_TABLES.INSTRUMENTS, instrumentDTO.instrumentName(), instrumentDTO.instrumentId());
    }

    @Override
    public String toSqlDeleteQuery(InstrumentDTO instrumentDTO) {
        return String.format("DELETE FROM %s WHERE Instrument_Id = %d", DB_TABLES.INSTRUMENTS, instrumentDTO.instrumentId());
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

    public int instrumentId() {
        return id;
    }

    public String instrumentName() {
        return name;
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
