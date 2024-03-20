package org.scheduler.data.dto.reports;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.IReportable;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstrumentLessonsRatioDTO extends DTOBase<InstrumentLessonsRatioDTO> {
    private int numberOfLessons;
    private String instrumentName;
    private String teacherFirstName;
    private String teacherLastName;

    public InstrumentLessonsRatioDTO(int numberOfLessons, String instrumentName, String teacherFirstName, String teacherLastName) {
        this.numberOfLessons = numberOfLessons;
        this.instrumentName = instrumentName;
        this.teacherFirstName = teacherFirstName;
        this.teacherLastName = teacherLastName;
    }

    public int getNumberOfLessons() {
        return numberOfLessons;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    private String getTeacherFirstName() {
        return teacherFirstName;
    }

    private String getTeacherLastName() {
        return teacherLastName;
    }

    public String getTeacherFullName(){
        return getTeacherFirstName() + " " + getTeacherLastName();
    }

    public InstrumentLessonsRatioDTO(){

    }
    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(InstrumentLessonsRatioDTO item, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(InstrumentLessonsRatioDTO item, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(InstrumentLessonsRatioDTO item, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public InstrumentLessonsRatioDTO fromResultSet(ResultSet rs) {
        try {
            return new InstrumentLessonsRatioDTO(
                    rs.getInt("NumberOfLessons"),
                    rs.getString("Name"),
                    rs.getString("Teacher_First_Name"),
                    rs.getString("Teacher_Last_Name")
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
