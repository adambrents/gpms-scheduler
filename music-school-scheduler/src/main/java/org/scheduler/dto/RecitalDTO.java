package org.scheduler.dto;

import org.scheduler.dto.interfaces.ISqlConvertable;

import java.sql.ResultSet;
import java.time.LocalDateTime;

public class RecitalDTO implements ISqlConvertable<RecitalDTO> {

    private int recitalId;
    private LocalDateTime eventDate;
    private String eventDescription;

    public RecitalDTO(int recitalId, LocalDateTime eventDate, String eventDescription) {
        this.recitalId = recitalId;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
    }

    public RecitalDTO() {

    }

    @Override
    public String toSqlSelectQuery() {
        return null;
    }

    @Override
    public String toSqlInsertQuery(RecitalDTO item) {
        return null;
    }

    @Override
    public String toSqlUpdateQuery(RecitalDTO item) {
        return null;
    }

    @Override
    public String toSqlDeleteQuery(RecitalDTO item) {
        return null;
    }

    @Override
    public <T extends ISqlConvertable> T fromResultSet(ResultSet rs) {
        return null;
    }
}
