package org.scheduler.app.controller.interfaces;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

public interface IUpdate {
    void onUpdate(ActionEvent actionEvent) throws IOException, SQLException;
}
