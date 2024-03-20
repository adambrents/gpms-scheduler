package org.scheduler.data.repository.base;

import org.scheduler.app.constants.Constants;
import org.scheduler.data.configuration.JDBC;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.TeachersRepository;
import org.scheduler.data.repository.interfaces.IRepository;
import org.scheduler.data.repository.properties.BookRepository;
import org.scheduler.data.repository.properties.InstrumentRepository;
import org.scheduler.data.repository.properties.LevelRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.scheduler.data.configuration.JDBC.getConnection;

public abstract class BaseRepository<T extends ISqlConvertible> implements IRepository<T> {
    public final String _database = Constants.CONNECTION_CONFIG.getDbConnection().getDb();

    public abstract <T extends ISqlConvertible> void setKeyOnDTO(int key, T item);

    public static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public <T extends ISqlConvertible> List<T> getAllItemsFromType(T item) {
        List<T> items = new ArrayList<>();
        try (
             PreparedStatement pstmt = item.toSqlSelectQuery(JDBC.getConnection())) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                T result = (T) item.fromResultSet(rs);
                items.add(result);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public <T extends ISqlConvertible> void update(T item, Connection connection) throws SQLException {
        try (PreparedStatement pstmt = item.toSqlUpdateQuery(item, connection)) {
            int count = pstmt.executeUpdate();

            if (count == PreparedStatement.EXECUTE_FAILED) {
                throw new SQLException("Failed to update record!");
            }
        }
    }
    public <T extends ISqlConvertible> int insertReturnGeneratedKey(T item, Connection connection){
        try (PreparedStatement pstmt = item.toSqlInsertQuery(item, connection)) {
            try {
                int count = pstmt.executeUpdate();

                if (count == PreparedStatement.EXECUTE_FAILED) {
                    throw new SQLException("Failed to insert record!");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating item failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends ISqlConvertible> void insert(T item, Connection connection) {
        try (PreparedStatement pstmt = item.toSqlInsertQuery(item, connection)) {
            int count = pstmt.executeUpdate();

            if (count == PreparedStatement.EXECUTE_FAILED) {
                throw new SQLException("Failed to insert record!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public <T extends ISqlConvertible> void delete(T item, Connection connection) throws SQLException {
        try (PreparedStatement pstmt = item.toSqlDeleteQuery(item, connection)) {
            int count = pstmt.executeUpdate();

            if (count == PreparedStatement.EXECUTE_FAILED) {
                throw new SQLException("Failed to delete record!");
            }
        }
    }

    public void performAction(T item, Constants.CRUD action) throws SQLException {
        Connection connection = JDBC.getConnection();
        switch (action){
            case CREATE:
                insertItem(item, connection);
                connection.commit();
                break;
            case READ:
                //not yet implemented
                break;
            case UPDATE:
                updateItem(item, connection);
                connection.commit();
                break;
            case DELETE:
                delete(item, connection);
                connection.commit();
                break;
        }
    }

}
