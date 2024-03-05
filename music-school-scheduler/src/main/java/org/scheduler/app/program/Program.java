package org.scheduler.app.program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.scheduler.app.configuration.model.AppConfig;
import org.scheduler.app.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.scheduler.app.utilities.GsonHelper.loadConfig;

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_ROUTES.LOGIN_SCREEN));
        Parent root = loader.load();

//        primaryStage.getIcons().add(appIcon);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.setTitle(loadConfig(AppConfig.class).getAppName());

        Constants.PRIMARY_STAGE = primaryStage;

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
