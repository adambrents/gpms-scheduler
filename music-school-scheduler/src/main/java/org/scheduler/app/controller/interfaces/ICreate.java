package org.scheduler.app.controller.interfaces;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

public interface ICreate {
    void onAdd(ActionEvent event) throws IOException, SQLException;
}
