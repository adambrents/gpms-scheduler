package org.scheduler.data.repository.interfaces;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;

public interface IRepository<T> {
    ObservableList<T> getAllItems();
    void updateItem(T item, Connection connection) throws SQLException;
    void insertItem(T item, Connection connection) throws SQLException;
    void deleteItem(T item, Connection connection) throws SQLException;
}
