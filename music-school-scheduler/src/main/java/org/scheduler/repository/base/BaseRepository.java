package org.scheduler.repository.base;

import org.scheduler.constants.Constants;
import org.scheduler.repository.interfaces.IRepository;
import org.scheduler.dto.interfaces.ISqlConvertable;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.scheduler.repository.configuration.model.JDBC.getConnection;

public abstract class BaseRepository<T> implements IRepository<T> {
    public final String _database = Constants.CONNECTION_CONFIG.getDbConnection().getDb();

    public static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public <T extends ISqlConvertable> List<T> getAllItemsFromType(Class<T> item) throws SQLException {
        List<T> items = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(item.newInstance().toSqlSelectQuery())) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add((T) item.getMethod("fromResultSet", ResultSet.class).invoke(null, rs));
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public <T extends ISqlConvertable> void updateItems(T item) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(item.toSqlUpdateQuery(item))) {
            pstmt.executeUpdate();
        }
    }

    public <T extends ISqlConvertable> void insertItemsFromTable(T item) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(item.toSqlInsertQuery(item))) {

            pstmt.executeUpdate();
        }
    }

    public <T extends ISqlConvertable> void deleteItemsFromTable(T item) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(item.toSqlDeleteQuery(item))) {
            pstmt.executeUpdate();
        }
    }

}
