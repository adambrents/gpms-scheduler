package org.scheduler.app.controller.base;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.dto.LessonDTO;
import org.scheduler.data.dto.StudentDTO;
import org.scheduler.data.repository.*;
import org.scheduler.data.repository.properties.BookRepository;
import org.scheduler.data.repository.properties.InstrumentRepository;
import org.scheduler.data.repository.properties.LevelRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.scheduler.app.constants.Constants.ERROR_RED;
import static org.scheduler.app.constants.Constants.NAVIGATION_HISTORY;

public abstract class ControllerBase implements Initializable, IController {
    public int _userId;
    public StudentDTO _studentDTOToBeModified;
    public LessonDTO _lessonDTOToBeModified;

    public final StudentsRepository studentsRepository = new StudentsRepository();
    public final LessonsRepository lessonsRepository = new LessonsRepository();
    public final UsersRepository usersRepository = new UsersRepository();
    public final TeachersRepository teachersRepository = new TeachersRepository();
    public final RecitalRepository recitalRepository = new RecitalRepository();
    public final BookRepository bookRepository = new BookRepository();
    public final InstrumentRepository instrumentRepository = new InstrumentRepository();
    public final LevelRepository levelRepository = new LevelRepository();

    public abstract List<PossibleError> buildPossibleErrors();
    public abstract String getNameForItem(Object item);

    public void goBack(int userId) throws IOException {
            loadNewScreen(NAVIGATION_HISTORY.pop(), userId);
    }

    public void loadNewScreen(String destinationResourcePath, int userId) throws IOException {
        URL destinationURL = getClass().getResource(destinationResourcePath);
        loadNewScreen(destinationURL, userId);
        if(NAVIGATION_HISTORY.peek() == null){

            NAVIGATION_HISTORY.push(destinationURL);
        }
        else {

            NAVIGATION_HISTORY.push(NAVIGATION_HISTORY.peek());
        }
    }
    private void loadNewScreen(URL destinationURL, int userId) throws IOException{
        FXMLLoader loader = new FXMLLoader(destinationURL);
        Stage primaryStage = Constants.PRIMARY_STAGE;
        Parent parent = loader.load();
        Object controller = loader.getController();
        if (controller instanceof ControllerBase) {
            ((ControllerBase) controller).setControllerProperties(userId);
        }
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void setControllerProperties(int userId){
        this._userId = userId;
    }
    public abstract void setUserId(int userId);
    public void populateComboBox(ComboBox comboBox, List items) {
        comboBox.getItems().clear();
        comboBox.getItems().addAll(items);

        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : getNameForItem(item));
            }
        });

        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : getNameForItem(item));
            }
        });
    }


    public String getErrorMessage(){
        List<PossibleError> possibleErrors = buildPossibleErrors();

        List<String> errorMsgs = new ArrayList<>();

        for (PossibleError possibleError : possibleErrors) {
            if(possibleError.getPropertyValue().equals("")){
                errorMsgs.add(getErrorString(possibleError.getPropertyName()));
                setErroredLabel(possibleError.getPropertyLabel());
            }
        }

        if(!errorMsgs.isEmpty()){
            StringBuilder errorMessage = new StringBuilder();
            for (String string : errorMsgs) {
                errorMessage.append(string).append("\n");
            }
            return errorMessage.toString();
        }
        else {
            return null;
        }
    }

    private String getErrorString(String propertyName){
        return propertyName + " field MUST be filled out";
    }
    private void setErroredLabel(Label label){
        label.setTextFill(Paint.valueOf(ERROR_RED));
    }
}
