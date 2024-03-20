package org.scheduler.app.controller.interfaces;

import org.scheduler.data.dto.interfaces.ISqlConvertible;

import java.sql.SQLException;

public interface ITableView <T extends ISqlConvertible> {
    public void handleDoubleClickOnRow (T item) throws SQLException, InstantiationException, IllegalAccessException;
}
