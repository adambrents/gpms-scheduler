package org.scheduler.data.dto.mapping;

import javafx.beans.property.BooleanProperty;
import org.scheduler.app.constants.Constants;
import org.scheduler.data.dto.base.DTOMappingBase;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.StudentsRepository;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentRecitalDTO extends DTOMappingBase<StudentRecitalDTO> implements ISqlConvertible<StudentRecitalDTO> {

    public StudentRecitalDTO(int id, int mappingFromId, int mappingToId, BooleanProperty selected) {
        super.id = id;
        super.mappingFromId = mappingFromId;
        super.mappingToId = mappingToId;
        super.isSelected = selected;
        setMapping();
    }

    public StudentRecitalDTO() {
        
    }
    @Override
    public Constants.MAPPINGS getMapping(){
        return mapping;
    }
    private void setMapping() {
        super.mapping = Constants.MAPPINGS.StudentRecital;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) {
        return null;
    }

    @Override
    public PreparedStatement toSqlInsertQuery(StudentRecitalDTO item, Connection connection) {
        return null;
    }

    @Override
    public PreparedStatement toSqlUpdateQuery(StudentRecitalDTO item, Connection connection) {
        return null;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(StudentRecitalDTO item, Connection connection) {
        return null;
    }

    @Override
    public <T extends ISqlConvertible> T fromResultSet(ResultSet rs) {
        return null;
    }

    @Override
    public IRepository getRepository() {
        return new StudentsRepository();
    }
}
