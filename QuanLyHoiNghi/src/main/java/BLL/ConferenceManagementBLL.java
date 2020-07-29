package BLL;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ConferenceManagementBLL implements Initializable {

    @FXML
    private VBox left;

    @FXML
    private Pagination center;

    @FXML
    private FlowPane top;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(HomeBLL.user == null){
            left.setVisible(false);
            left.setManaged(false);
        }
    }
}
