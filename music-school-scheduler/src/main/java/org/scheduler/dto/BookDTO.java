package org.scheduler.dto;

import org.scheduler.dto.interfaces.ISqlConvertable;
import org.scheduler.repository.configuration.model.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;

public record BookDTO(int bookId, String name) implements ISqlConvertable<BookDTO> {
    @Override
    public String toSqlSelectQuery() {
        return String.format("SELECT * FROM %s", DB_TABLES.BOOKS);
    }

    @Override
    public String toSqlInsertQuery(BookDTO bookDTO) {
        return String.format("INSERT INTO %s (Book_Id, Name) VALUES (%d, '%s')",
                DB_TABLES.BOOKS, bookDTO.bookId(), bookDTO.name());
    }

    @Override
    public String toSqlUpdateQuery(BookDTO bookDTO) {
        return String.format("UPDATE %s SET Name = '%s' WHERE Book_Id = %d",
                DB_TABLES.BOOKS, bookDTO.name(), bookDTO.bookId());
    }

    @Override
    public String toSqlDeleteQuery(BookDTO bookDTO) {
        return String.format("DELETE FROM %s WHERE Book_Id = %d", DB_TABLES.BOOKS, bookDTO.bookId());
    }

    @Override
    public BookDTO fromResultSet(ResultSet rs) {
        try {
            return new BookDTO(rs.getInt("Book_Id"), rs.getString("Name"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
