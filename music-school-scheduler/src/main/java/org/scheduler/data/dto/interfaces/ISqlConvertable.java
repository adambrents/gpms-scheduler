package org.scheduler.data.dto.interfaces;

import java.sql.ResultSet;

public interface ISqlConvertable<T> {
    String toSqlSelectQuery();
    String toSqlInsertQuery(T item);
    String toSqlUpdateQuery(T item);
    String toSqlDeleteQuery(T item);
    <T extends ISqlConvertable> T fromResultSet(ResultSet rs);
}
