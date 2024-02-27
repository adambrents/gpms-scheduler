package org.scheduler.dto;

import org.scheduler.dto.interfaces.ISqlConvertable;
import org.scheduler.repository.configuration.model.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;

public record InstrumentDTO(int instrumentId, String instrumentName) implements ISqlConvertable<InstrumentDTO> {
    @Override
    public String toSqlSelectQuery() {
        return String.format("SELECT * FROM %s", DB_TABLES.INSTRUMENTS);
    }

    @Override
    public String toSqlInsertQuery(InstrumentDTO instrumentDTO) {
        return String.format("INSERT INTO %s (Instrument_Id, Instrument_Name) VALUES (%d, '%s')",
                DB_TABLES.INSTRUMENTS, instrumentDTO.instrumentId(), instrumentDTO.instrumentName());
    }

    @Override
    public String toSqlUpdateQuery(InstrumentDTO instrumentDTO) {
        return String.format("UPDATE %s SET Instrument_Name = '%s' WHERE Instrument_Id = %d",
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
            String instrumentName = rs.getString("Instrument_Name");
            return new InstrumentDTO(instrumentId, instrumentName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
