package org.scheduler.data.dto.interfaces;

import javafx.beans.property.BooleanProperty;
import org.scheduler.data.repository.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ISqlConvertible<T> {
    PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException;
    PreparedStatement toSqlInsertQuery(T item, Connection connection) throws SQLException;
    PreparedStatement toSqlUpdateQuery(T item, Connection connection) throws SQLException;
    PreparedStatement toSqlDeleteQuery(T item, Connection connection) throws SQLException;
    <T extends ISqlConvertible> T fromResultSet(ResultSet rs);
    void setSelected(boolean b);
    BooleanProperty getSelected();
    int getId();
    String getName();
    void setName(String text);
    IRepository getRepository();
}
