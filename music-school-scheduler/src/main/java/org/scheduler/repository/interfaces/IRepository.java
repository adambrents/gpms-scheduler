package org.scheduler.repository.interfaces;

import javafx.collections.ObservableList;

public interface IRepository<T> {
    ObservableList<T> getAllItems();
    void updateItem(T item);
    void insertItem(T item);
    void deleteItem(T item);
}
