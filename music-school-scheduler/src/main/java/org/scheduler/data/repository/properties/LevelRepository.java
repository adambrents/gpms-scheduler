package org.scheduler.data.repository.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.properties.LevelDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class LevelRepository extends BaseRepository<LevelDTO> {
    private final Logger _logger = LoggerFactory.getLogger(LevelRepository.class);
    @Override
    public ObservableList<LevelDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(LevelDTO.class));
    }


    @Override
    public void updateItem(LevelDTO item, Connection connection) throws SQLException {
        super.update(item, connection);
    }

    @Override
    public void insertItem(LevelDTO item, Connection connection) throws SQLException {
        super.insert(item, connection);
    }

    @Override
    public void deleteItem(LevelDTO item, Connection connection) throws SQLException {
        super.delete(item, connection);
    }


    @Override
    public <T extends ISqlConvertible> void setKeyOnDTO(int key, T item) {

    }
}
