package org.scheduler.data.dto.properties;

import org.scheduler.data.dto.interfaces.ISqlConvertable;
import org.scheduler.data.configuration.DB_TABLES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class BookDTO implements ISqlConvertable<BookDTO> {
    private int id;
    private String name;

    public BookDTO() {
        
    }
    public BookDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public int bookId() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BookDTO) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "BookDTO[" +
                "bookId=" + id + ", " +
                "name=" + name + ']';
    }

}
