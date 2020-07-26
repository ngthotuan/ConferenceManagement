package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

public class Test  extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GUI/CreateConferenceGUI.fxml"));
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.show();


        }catch (Exception exception){
            exception.printStackTrace();
            System.err.printf("Cause: %s%nMessage: %s%n", exception.getCause(), exception.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
