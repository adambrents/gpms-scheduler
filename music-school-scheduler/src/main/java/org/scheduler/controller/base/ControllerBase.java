package org.scheduler.controller.base;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.scheduler.constants.Constants.NAVIGATION_HISTORY;

public abstract class ControllerBase implements Initializable {

    public Stage _primaryStage;
    public int _userId;
    public void goBack() {
        if (_primaryStage != null && NAVIGATION_HISTORY.getHistory().size() > 1) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(NAVIGATION_HISTORY.peek().getFxmlPath()));
            ControllerBase controller = loader.getController();
            controller.setUser(_userId);
            _primaryStage.setScene(NAVIGATION_HISTORY.pop().getScene());
        }
    }
    public void setUser(int userId) {
        _userId = userId;
    }

    public void setScene(String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);
        NAVIGATION_HISTORY.push(scene, fxmlPath);
        _primaryStage.setScene(scene);
    }
}
