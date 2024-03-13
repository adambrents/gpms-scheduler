package org.scheduler.data.dto;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.RecitalRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class RecitalDTO extends DTOBase<RecitalDTO> implements ISqlConvertible<RecitalDTO> {

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
    public PreparedStatement toSqlSelectQuery(Connection connection) {
        return null;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(RecitalDTO item, Connection connection) {
        return null;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(RecitalDTO item, Connection connection) {
        return null;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(RecitalDTO item, Connection connection) {
        return null;
    }


    @Override
    public <T extends ISqlConvertible> T fromResultSet(ResultSet rs) {
        return null;
    }

    @Override
    public IRepository getRepository() {
        return new RecitalRepository();
    }
}
