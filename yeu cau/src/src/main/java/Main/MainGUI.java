package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GUI/HomeGUI.fxml"));
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.sizeToScene();
            primaryStage.setMinHeight(2 * primaryStage.getHeight() - primaryStage.getX());
            primaryStage.setMinWidth(primaryStage.getWidth());


        }catch (Exception exception){
            exception.printStackTrace();
            System.err.printf("Cause: %s%nMessage: %s%n", exception.getCause(), exception.getMessage());
        }
    }
}
