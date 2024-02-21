package program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Program extends Application{
    /**
     * Loads login screen on app startup and sets the application icon.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));

        primaryStage.setScene(new Scene(root));

        primaryStage.getIcons().add(new Image("/img/music.png"));

        primaryStage.show();
    }

    /**
     * launches javafx application
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Ensure the logs directory exists
            Path logDirectory = Paths.get("./logs");
            if (!Files.exists(logDirectory)) {
                Files.createDirectories(logDirectory); // Create the logs directory if it does not exist
            }

            // Ensure the musicscheduler.txt file exists
            Path logFile = logDirectory.resolve("musicscheduler.txt");
            if (!Files.exists(logFile)) {
                Files.createFile(logFile); // Create the file if it does not exist
            }

            launch(args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
