package Utils;

import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MyAlert {
    public static  Optional<ButtonType> show(javafx.scene.control.Alert.AlertType type, String title, String header, String content){
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

}
