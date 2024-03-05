package org.scheduler.data.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.RecitalDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class RecitalRepository extends BaseRepository<RecitalDTO> {
    private final Logger _logger = LoggerFactory.getLogger(RecitalRepository.class);
    @Override
    public ObservableList<RecitalDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(RecitalDTO.class));
    }

    @Override
    public void updateItem(RecitalDTO item) throws SQLException {
        super.update(item);
    }

    @Override
    public int insertItem(RecitalDTO item) throws SQLException {
        super.insert(item);
        return 0;
    }

    @Override
    public void deleteItem(RecitalDTO item) throws SQLException {
        super.delete(item);
    }
}
