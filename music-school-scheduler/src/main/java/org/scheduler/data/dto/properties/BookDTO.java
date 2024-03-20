package org.scheduler.data.dto.properties;

import org.scheduler.data.dto.base.DTOBase;
import org.scheduler.data.dto.interfaces.IComboBox;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.configuration.DB_TABLES;
import org.scheduler.data.repository.interfaces.IRepository;
import org.scheduler.data.repository.properties.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class BookDTO extends DTOBase<BookDTO> implements ISqlConvertible<BookDTO>, IComboBox {
    public BookDTO() {
        
    }
    public BookDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public BookDTO(String name) {
        this.name = name;
    }

    public BookDTO(int bookId) {
        super.id = bookId;
    }

    @Override
    public PreparedStatement toSqlSelectQuery(Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + DB_TABLES.BOOKS;
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }


    @Override
    public PreparedStatement toSqlInsertQuery(BookDTO bookDTO, Connection connection) throws SQLException {
        String sql = "INSERT INTO " + DB_TABLES.BOOKS + " (Book_Id, Name) VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, bookDTO.getId());
        statement.setString(2, bookDTO.getName());

        return statement;
    }


    @Override
    public PreparedStatement toSqlUpdateQuery(BookDTO bookDTO, Connection connection) throws SQLException {
        String sql = "UPDATE " + DB_TABLES.BOOKS + " SET Name = ? WHERE Book_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, bookDTO.getName());
        statement.setInt(2, bookDTO.getId());

        return statement;
    }

    @Override
    public PreparedStatement toSqlDeleteQuery(BookDTO bookDTO, Connection connection) throws SQLException {
        String sql = "DELETE FROM " + DB_TABLES.BOOKS + " WHERE Book_Id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, bookDTO.getId());

        return statement;
    }

    @Override
    public IRepository getRepository() {
        return new BookRepository();
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
