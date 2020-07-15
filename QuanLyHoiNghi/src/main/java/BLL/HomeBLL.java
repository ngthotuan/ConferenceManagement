package BLL;

import BLL.Elements.HoiNghiCardBLL;
import BLL.Elements.HoiNghiListBLL;
import DAO.ConferenceDAO;
import DTO.Conference;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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


    @FXML
    private ScrollPane scrollPane;
    @FXML
    private CheckBox cardViews;

    private List<Conference> conferences;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conferences = ConferenceDAO.getConferences();

        conferences.forEach(conference -> {
            vbox.getChildren().add(new HoiNghiListBLL(conference));
        });
        // listView by default
        scrollPane.setContent(vbox);
        // load data to Grid
        initGridPane(conferences, 3);


        cardViews.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    scrollPane.setContent(gridPane);
                }
                else{
                    scrollPane.setContent(vbox);
                }
            }
        });
    }

    private void initGridPane(List<Conference> conferences, int col){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(1.0*100/col);
        for(int i =0; i < col; i++){
            gridPane.getColumnConstraints().add(column);
        }
        for(int i =0; i<conferences.size(); i++){
            gridPane.add(new HoiNghiCardBLL(conferences.get(i)), i%col, i/col);
        }
    }

}
