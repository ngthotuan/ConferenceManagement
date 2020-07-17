package BLL;

import BLL.Elements.HoiNghiCardBLL;
import BLL.Elements.HoiNghiListBLL;
import DAO.ConferenceDAO;
import DTO.Conference;
import DTO.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class HomeBLL implements Initializable{
    private VBox vbox = new VBox();
    private GridPane gridPane =new GridPane();
    private final int GRID_COL = 3;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private CheckBox cardViews;

    private List<Conference> conferences;

    public static User user = new User("admin", "admin");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conferences = ConferenceDAO.getConferences();

        conferences.forEach(conference -> {
            vbox.getChildren().add(new HoiNghiListBLL(conference));
        });
        // listView by default
        scrollPane.setContent(vbox);
        initGridPane(GRID_COL);

        cardViews.selectedProperty().addListener((observable, oldValue, newValue) -> {
            conferences = ConferenceDAO.getConferences();
            System.out.println(conferences.size());
            if(newValue){
                gridPane.getChildren().removeAll();
                for(int i =0; i<conferences.size(); i++){
                    gridPane.add(new HoiNghiCardBLL(conferences.get(i)), i%GRID_COL, i/GRID_COL);
                }
                scrollPane.setContent(gridPane);
            }
            else{
                vbox.getChildren().removeAll(vbox.getChildren());
                conferences.forEach(conference -> {
                    vbox.getChildren().add(new HoiNghiListBLL(conference));
                });
                scrollPane.setContent(vbox);
            }
        });
    }

    private void initGridPane(int col){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(1.0*100/col);
        for(int i =0; i < col; i++){
            gridPane.getColumnConstraints().add(column);
        }

    }

}
