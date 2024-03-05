package org.scheduler.data.repository.base;

import org.scheduler.app.constants.Constants;
import org.scheduler.data.repository.interfaces.IRepository;
import org.scheduler.data.dto.interfaces.ISqlConvertable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.scheduler.data.configuration.JDBC.getConnection;

public abstract class BaseRepository<T extends ISqlConvertable> implements IRepository<T> {
    public final String _database = Constants.CONNECTION_CONFIG.getDbConnection().getDb();

    public static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public List<T> getAllItemsFromType(Class<T> item) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> items = new ArrayList<>();
        T instance = item.newInstance();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(instance.toSqlSelectQuery())) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Method fromResultSetMethod = item.getMethod("fromResultSet", ResultSet.class);
                fromResultSetMethod.setAccessible(true);
                T result = (T) fromResultSetMethod.invoke(instance, rs);
                items.add(result);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public <T extends ISqlConvertable> void update(T item) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(item.toSqlUpdateQuery(item))) {
            pstmt.executeUpdate();
        }
    }

    public <T extends ISqlConvertable> int insert(T item) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(item.toSqlInsertQuery(item), Statement.RETURN_GENERATED_KEYS)) {
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }
        }
    }

    public <T extends ISqlConvertable> void delete(T item) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(item.toSqlDeleteQuery(item))) {
            pstmt.executeUpdate();
        }
    }

    public void performAction(T item, Constants.CRUD action) throws SQLException {
        switch (action){
            case CREATE:
                insertItem(item);
                break;
            case READ:
                //not yet implemented
                break;
            case UPDATE:
                updateItem(item);
                break;
            case DELETE:
                delete(item);
                break;
        }
    }

}
