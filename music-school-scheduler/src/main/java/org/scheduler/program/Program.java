package org.scheduler.program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.scheduler.configuration.model.AppConfig;
import org.scheduler.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.scheduler.constants.Constants.NAVIGATION_HISTORY;
import static org.scheduler.utilities.GsonHelper.loadConfig;

public class Program extends Application{
    private static final Logger _logger = LoggerFactory.getLogger(Program.class);
    /**
     * Loads login screen on app Startup and sets the application icon.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
//        Image appIcon = new Image(getClass().getResourceAsStream("img/app-icon.png"));

        Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_ROUTES.LOGIN_SCREEN));

//        primaryStage.getIcons().add(appIcon);
        Scene scene = new Scene(root);

        NAVIGATION_HISTORY.push(scene, Constants.FXML_ROUTES.LOGIN_SCREEN);

        primaryStage.setScene(scene);

        primaryStage.setTitle(loadConfig(AppConfig.class).getAppName());

        primaryStage.show();
    }

    /**
     * launches javafx application
     * @param args
     */
    public static void main(String[] args) {
        try {
            Path logDirectory = Paths.get("./logs");
            if (!Files.exists(logDirectory)) {
                Files.createDirectories(logDirectory);
            }

            Path logFile = logDirectory.resolve("musicscheduler.txt");
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }

            launch(args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
