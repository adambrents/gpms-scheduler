package org.scheduler.data.repository.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.repository.base.BaseRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class BookRepository extends BaseRepository<BookDTO> {
    @Override
    public ObservableList<BookDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(BookDTO.class));
    }

    @Override
    public void updateItem(BookDTO item, Connection connection) throws SQLException {
        super.update(item, connection);
    }

    @Override
    public void insertItem(BookDTO item, Connection connection) throws SQLException {
        super.insert(item, connection);
    }

    @Override
    public void deleteItem(BookDTO item, Connection connection) throws SQLException {
        super.delete(item, connection);
    }

    public void performAction(BookDTO bookDTO, String action) {
    }

    @Override
    public <T extends ISqlConvertible> void setKeyOnDTO(int key, T item) {

    }
}
