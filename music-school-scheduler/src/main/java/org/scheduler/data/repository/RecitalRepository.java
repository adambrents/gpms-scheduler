package org.scheduler.data.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.RecitalDTO;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class RecitalRepository extends BaseRepository<RecitalDTO> {
    private final Logger _logger = LoggerFactory.getLogger(RecitalRepository.class);
    @Override
    public ObservableList<RecitalDTO> getAllItems() {
        return FXCollections.observableArrayList(super.getAllItemsFromType(new RecitalDTO()));
    }

    @Override
    public void updateItem(RecitalDTO item, Connection connection) throws SQLException {
        super.update(item, connection);
    }

    @Override
    public void insertItem(RecitalDTO item, Connection connection) throws SQLException {
        super.insert(item, connection);
    }

    @Override
    public void deleteItem(RecitalDTO item, Connection connection) throws SQLException {
        super.delete(item, connection);
    }

    @Override
    public <T extends ISqlConvertible> void setKeyOnDTO(int key, T item) {

    }
}
