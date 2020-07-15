package Main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class Test  extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane hBox = new StackPane();
        hBox.setStyle("-fx-background-color: aqua;");
        hBox.prefHeight(Region.USE_PREF_SIZE);
        Button btn1 = new Button("Button1");
        btn1.setPrefWidth(100);
        hBox.getChildren().add(btn1);

        hBox.getChildren().add(new Button("Button2"));
//        StackPane stack = new StackPane();
//        Rectangle helpIcon = new Rectangle(30.0, 25.0);
//        helpIcon.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
//                new Stop[]{
//                        new Stop(0, Color.web("#4977A3")),
//                        new Stop(0.5, Color.web("#B0C6DA")),
//                        new Stop(1,Color.web("#9CB6CF")),}));
//        helpIcon.setStroke(Color.web("#D0E6FA"));
//        helpIcon.setArcHeight(3.5);
//        helpIcon.setArcWidth(3.5);
//
//        Text helpText = new Text("?");
//        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
//        helpText.setFill(Color.WHITE);
//        helpText.setStroke(Color.web("#7080A0"));
//
//        stack.getChildren().addAll(helpIcon, helpText);
//        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
//        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"
//        hBox.getChildren().add(stack);
//        HBox.setHgrow(stack, Priority.ALWAYS);

        primaryStage.setScene(new Scene(hBox, 300, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
