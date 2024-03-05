package org.scheduler.data.repository.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.data.dto.properties.BookDTO;
import org.scheduler.data.repository.base.BaseRepository;

import java.sql.SQLException;

public class BookRepository extends BaseRepository<BookDTO> {
    @Override
    public ObservableList<BookDTO> getAllItems() throws SQLException, InstantiationException, IllegalAccessException {
        return FXCollections.observableArrayList(super.getAllItemsFromType(BookDTO.class));
    }

    @Override
    public void updateItem(BookDTO item) throws SQLException {
        super.update(item);
    }

    @Override
    public int insertItem(BookDTO item) throws SQLException {
        super.insert(item);
        return 0;
    }

    @Override
    public void deleteItem(BookDTO item) throws SQLException {
        super.delete(item);
    }

    public void performAction(BookDTO bookDTO, String action) {
    }
}
