package org.scheduler.data.repository.interfaces;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IRepository<T> {
    ObservableList<T> getAllItems() throws SQLException, InstantiationException, IllegalAccessException;
    void updateItem(T item) throws SQLException;
    int insertItem(T item) throws SQLException;
    void deleteItem(T item) throws SQLException;
}
