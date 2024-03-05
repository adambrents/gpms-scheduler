package org.scheduler.data.repository.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.properties.InstrumentDTO;
import org.scheduler.data.repository.base.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class InstrumentRepository extends BaseRepository<InstrumentDTO> {
    private final Logger _logger = LoggerFactory.getLogger(InstrumentRepository.class);
    @Override
    public ObservableList<InstrumentDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(InstrumentDTO.class));
    }

    @Override
    public void updateItem(InstrumentDTO item) throws SQLException {
        super.update(item);
    }

    @Override
    public int insertItem(InstrumentDTO item) throws SQLException {
        super.insert(item);
        return 0;
    }

    @Override
    public void deleteItem(InstrumentDTO item) throws SQLException {
        super.delete(item);
    }

    public void performAction(InstrumentDTO instrumentDTO, String action) {
    }
}
