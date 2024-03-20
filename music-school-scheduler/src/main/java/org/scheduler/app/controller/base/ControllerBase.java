package org.scheduler.app.controller.base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.scheduler.app.constants.Constants;
import org.scheduler.app.controller.handlers.BookActionHandler;
import org.scheduler.app.controller.handlers.InstrumentActionHandler;
import org.scheduler.app.controller.handlers.LevelActionHandler;
import org.scheduler.app.controller.handlers.TeacherActionHandler;
import org.scheduler.app.controller.handlers.base.IActionHandler;
import org.scheduler.app.controller.interfaces.IController;
import org.scheduler.app.models.errors.PossibleError;
import org.scheduler.data.dto.interfaces.IComboBox;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.*;
import org.scheduler.data.repository.interfaces.IRepository;
import org.scheduler.data.repository.properties.BookRepository;
import org.scheduler.data.repository.properties.InstrumentRepository;
import org.scheduler.data.repository.properties.LevelRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.scheduler.app.constants.Constants.*;

public abstract  class ControllerBase implements Initializable, IController {
    public int userId;
    public final StudentsRepository studentsRepository = new StudentsRepository();
    public final LessonsRepository lessonsRepository = new LessonsRepository();
    public final UsersRepository usersRepository = new UsersRepository();
    public final TeachersRepository teachersRepository = new TeachersRepository();
    public final RecitalRepository recitalRepository = new RecitalRepository();
    public final BookRepository bookRepository = new BookRepository();
    public final InstrumentRepository instrumentRepository = new InstrumentRepository();
    public final LevelRepository levelRepository = new LevelRepository();
    public InstrumentActionHandler instrumentActionHandler = new InstrumentActionHandler();
    public LevelActionHandler levelActionHandler = new LevelActionHandler();
    public TeacherActionHandler teacherActionHandler = new TeacherActionHandler();
    public BookActionHandler bookActionHandler = new BookActionHandler();
    public final List<Label> labelsToReset = new ArrayList<>();

    public TextArea errorText;
    @Override
    public void reset() {
        resetAllFieldsOnScreen(PRIMARY_STAGE.getScene().getRoot());
    }

    private void resetAllFieldsOnScreen(Node node) {
        if (node instanceof TextField) {
            ((TextField) node).clear();
        } else if (node instanceof TextArea) {
            ((TextArea) node).clear();
        }else if (node instanceof ComboBox) {
            ((ComboBox<?>) node).setValue(null);
        }
        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                resetAllFieldsOnScreen(child);
            }
        }
        if (node instanceof CheckBox) {
            ((CheckBox) node).setSelected(false);
        }

        if (node instanceof Control) {
            node.setDisable(false);
        }
        for (Label label : labelsToReset) {
            label.setTextFill(Color.BLACK);
        }
        errorText.setVisible(false);
        errorText.setText("");
    }

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
        setUserId(userId);
    }
    public abstract void setUserId(int userId);
    public <T extends IComboBox> void populateComboBox(ComboBox<T> comboBox, List<T> items) {
        comboBox.getItems().clear();
        comboBox.getItems().addAll(items);

        comboBox.setCellFactory(lv -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });

        comboBox.setButtonCell(new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });

        comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T item) {
                return item == null ? "" : item.getName();
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        });
    }

    public void populateComboBoxTimes(ComboBox<LocalTime> comboBox, List<LocalTime> items) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        comboBox.getItems().clear();
        comboBox.getItems().addAll(items);

        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(LocalTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.format(timeFormatter));
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LocalTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.format(timeFormatter));
            }
        });
        comboBox.setConverter(new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime item) {
                return item == null ? "" : item.format(timeFormatter);
            }

            @Override
            public LocalTime fromString(String string) {
                return null;
            }
        });
    }


    public String getErrorMessage(){
        List<PossibleError> possibleErrors = buildPossibleErrors();

        List<String> errorMsgs = new ArrayList<>();

        for (PossibleError possibleError : possibleErrors) {
            String propertyValue = possibleError.getPropertyValue();
            if (propertyValue != null && ("null".equals(propertyValue) || propertyValue.isEmpty())) {
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
            return "";
        }
    }

    public abstract List<PossibleError> buildPossibleErrors();

    private String getErrorString(String propertyName){
        return propertyName + " field MUST be filled out";
    }
    private void setErroredLabel(Label label){
        label.setTextFill(Paint.valueOf(ERROR_RED));
    }


//    public <repo extends BaseRepository, listDto extends ISqlConvertible, dto extends ISqlConvertible, mappings extends DTOMappingBase> void setCheckboxValues(ListView<listDto> selectedItems, dto dtoItem, repo repository, mappings map){
//        List<mappings> mappings = null;
//
//        try {
//            mappings = map.getMappingsById(dtoItem, repository);
//        } catch (SQLException | InstantiationException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//
//        List<mappings> finalMappings = new ArrayList<>();
//
//        for (mappings mapping : mappings) {
//            if(mapping.mappingFromId == dtoItem.getId()){
//                finalMappings.add(mapping);
//            }
//        }
//        selectedItems.getItems().forEach(item -> {
//            for (mappings mappingObject : finalMappings) {
//                if (item.getId() == mappingObject.getMappingToId()) {
//                    item.setSelected(true);
//                }
//            }
//        });
//    }
    public <repo extends IRepository, dto extends ISqlConvertible, handler extends IActionHandler> void populateListView(ListView<dto> listView, repo item) {
        listView.getItems().clear();

        listView.setItems(item.getAllItems());

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        listView.setCellFactory(param -> new ListCell<dto>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(dto item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());

                    checkBox.setSelected(item.getSelected().get());

                    checkBox.setOnAction(e -> {
                        item.setSelected(checkBox.isSelected());
                    });

                    setGraphic(checkBox);
                }
            }
        });
    }
    public <repo extends IRepository, dto extends ISqlConvertible, handler extends IActionHandler>  void setUpListener(ListView<dto> listView, handler actionHandler){
        listView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty property = new SimpleBooleanProperty();
            property.addListener((obs, wasSelected, isNowSelected) -> {
                if(isNowSelected){
                    item.setSelected(true);
                    actionHandler.performConcreteListAction(true, item);
                }
                if(wasSelected){
                    item.setSelected(false);
                    actionHandler.performConcreteListAction(false, item);
                }

            });

            return property;
        }, new StringConverter<dto>() {
            @Override
            public String toString(dto object) {
                return object.getName();
            }

            @Override
            public dto fromString(String string) {
                return null;
            }
        }));
    }

//    protected abstract <mapping extends ISqlConvertible> void performConcreteListAction(boolean isAddAction, mapping item);

}
