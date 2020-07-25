package Utils;

import BLL.MyController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MyStage {
    public static void openNewStage(String title, URL resource){
        try {
            Parent parent = FXMLLoader.load(resource);
            Scene scene = new Scene(parent);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openNewStageWithValue(String title, URL resource, Object value){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(resource);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            MyController controller = loader.getController();
            controller.setValue(value);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openStage(Event event, String title, URL resource){
        try {
            Parent parent = FXMLLoader.load(resource);
            Scene scene = new Scene(parent);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openStageWithValue(Event event, String title, URL resource, Object value){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(resource);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            MyController controller = loader.getController();
            controller.setValue(value);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
