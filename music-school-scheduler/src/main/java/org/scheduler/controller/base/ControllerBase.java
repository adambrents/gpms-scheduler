package org.scheduler.controller.base;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.scheduler.constants.Constants;
import org.scheduler.viewmodels.Lesson;
import org.scheduler.viewmodels.Student;
import org.scheduler.viewmodels.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.scheduler.constants.Constants.NAVIGATION_HISTORY;

public abstract class ControllerBase implements Initializable {

    public Stage _primaryStage;
    public int _userId;
    public Student _studentToBeModified;
    public Lesson _lessonToBeModified;
    public void goBack() {
        if (_primaryStage != null && NAVIGATION_HISTORY.getHistory().size() > 0) {
            _primaryStage.setScene(NAVIGATION_HISTORY.pop().getScene());
            _primaryStage.show();
        }
    }
    public void loadNewScreen(ActionEvent actionEvent, String resourcePath, Object data) throws IOException {
        _primaryStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
        Parent parent = loader.load();
        Object controller = loader.getController();
        if (controller instanceof ControllerBase) { // BaseController approach
            ((ControllerBase) controller).setControllerProperties(data);
        }
        Scene scene = new Scene(parent);
        _primaryStage.setScene(scene);
        NAVIGATION_HISTORY.push(scene, resourcePath);
        _primaryStage.show();
    }

    public void setControllerProperties(Object data){
        if (data instanceof ControllerBase) {
            this._userId = ((ControllerBase) data)._userId;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }
    private void setUserId(int userId) {
        this._userId = userId;
    }
}
