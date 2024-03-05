package org.scheduler.data.dto;

import org.scheduler.data.dto.interfaces.ISqlConvertable;

import java.sql.ResultSet;

public class TeacherStudentInstrumentDTO implements ISqlConvertable<StudentRecitalDTO> {
    private int teacherInstrumentId;
    private int teacherId;
    private int studentId;
    private int instrumentId;

    public TeacherStudentInstrumentDTO(int teacherInstrumentId, int teacherId, int studentId, int instrumentId) {
        this.teacherInstrumentId = teacherInstrumentId;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.instrumentId = instrumentId;
    }
    public TeacherStudentInstrumentDTO(int teacherId, int studentId, int instrumentId) {
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.instrumentId = instrumentId;
    }

    public TeacherStudentInstrumentDTO() {
        
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
