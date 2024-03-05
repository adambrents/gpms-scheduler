package org.scheduler.data.dto;

import org.scheduler.data.dto.interfaces.ISqlConvertable;

import java.sql.ResultSet;

public class StudentRecitalDTO implements ISqlConvertable<StudentRecitalDTO> {

    private int studentRecitalId;
    private int studentId;
    private int recitalId;

    public StudentRecitalDTO(int studentRecitalId, int studentId, int recitalId) {
        this.studentRecitalId = studentRecitalId;
        this.studentId = studentId;
        this.recitalId = recitalId;
    }

    public StudentRecitalDTO() {
        
    }

    @Override
    public String toSqlSelectQuery() {
        return null;
    }

    @Override
    public String toSqlInsertQuery(StudentRecitalDTO item) {
        return null;
    }

    @Override
    public String toSqlUpdateQuery(StudentRecitalDTO item) {
        return null;
    }

    @Override
    public String toSqlDeleteQuery(StudentRecitalDTO item) {
        return null;
    }

    @Override
    public <T extends ISqlConvertable> T fromResultSet(ResultSet rs) {
        return null;
    }
}
