package program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application{
    /**
     * Loads login screen on app startup
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));

        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }

    /**
     * launches javafx application
     * @param args
     */
    public static void main(String[] args) {
        try{
            launch(args);
        }catch (Throwable t){
            t.printStackTrace();
        }
    }
}