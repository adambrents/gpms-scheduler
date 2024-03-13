package org.scheduler.data.repository.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class InstrumentRepository extends BaseRepository<InstrumentDTO> {
    private final Logger _logger = LoggerFactory.getLogger(InstrumentRepository.class);
    @Override
    public ObservableList<InstrumentDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(InstrumentDTO.class));
    }

    @Override
    public void updateItem(InstrumentDTO item, Connection connection) throws SQLException {
        super.update(item, connection);
    }

    @Override
    public void insertItem(InstrumentDTO item, Connection connection) throws SQLException {
        super.insert(item, connection);
    }

    @Override
    public void deleteItem(InstrumentDTO item, Connection connection) throws SQLException {
        super.delete(item, connection);
    }

    public void performAction(InstrumentDTO instrumentDTO, String action) {
    }

    @Override
    public <T extends ISqlConvertible> void setKeyOnDTO(int key, T item) {

    }
}
