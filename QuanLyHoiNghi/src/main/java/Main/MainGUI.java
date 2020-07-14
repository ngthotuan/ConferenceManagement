package Main;

import BLL.Elements.HoiNghiListBLL;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.event.ChangeEvent;
import java.util.Arrays;

public class MainGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../GUI/HomeGUI.fxml"));
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
