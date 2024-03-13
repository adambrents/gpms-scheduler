package org.scheduler.app.controller.interfaces;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

public interface IController {
    void onGoBack(ActionEvent event);

    void onSubmit(ActionEvent actionEvent)  throws IOException, SQLException;

}
