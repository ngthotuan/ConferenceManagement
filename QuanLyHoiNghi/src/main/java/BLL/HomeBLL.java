package BLL;

import BLL.Elements.HoiNghiCardBLL;
import BLL.Elements.HoiNghiListBLL;
import DAO.ConferenceDAO;
import DTO.Conference;
import DTO.User;
import Utils.MyStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
    @FXML
    private Label lbUser;
    @FXML
    private Label lbLogout;

    @FXML
    private Hyperlink hpUserProfile;

    @FXML
    private Hyperlink hpUserConference;

    @FXML
    private Hyperlink hpUserManagement;

    @FXML
    private Hyperlink hpConferenceManagement;

    @FXML
    private Hyperlink hpPlaceManagement;

    private List<Conference> conferences;

    public static User user = null;// new User("admin", "admin");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateUserField();
        hpUserProfile.setOnMouseClicked(event -> {
            if(user == null){
                MyStage.openNewStage("Đăng nhập", getClass().getResource("../GUI/LoginGUI.fxml"));
                updateUserField();
            }
            if(user!= null){
                MyStage.openNewStageWithValue("Thông tin tài khoản",
                        getClass().getResource("../GUI/UserProfileGUI.fxml"),
                        user);
            }
        });
        hpConferenceManagement.setOnMouseClicked(event -> {
            MyStage.openNewStage("Tạo hội nghị", getClass().getResource("../GUI/CreateConferenceGUI.fxml"));
        });
        hpPlaceManagement.setOnMouseClicked(event -> {
            MyStage.openNewStage("Địa điểm", getClass().getResource("../GUI/CreatePlaceGUI.fxml"));
        });
        //user logout
        lbLogout.setOnMouseClicked(event -> {
            lbUser.setText("Khách");
            user = null;
            lbLogout.setVisible(false);
        });

        //show list conference
        conferences = ConferenceDAO.getConferences();
        conferences.forEach(conference -> {
            vbox.getChildren().add(new HoiNghiListBLL(conference));
        });
        // listView by default
        scrollPane.setContent(vbox);
        initGridPane(GRID_COL);
        cardViews.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                scrollPane.setContent(gridPane);
            }
            else{
                scrollPane.setContent(vbox);
            }
        });
    }



    private void openUserProfile() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../GUI/UserProfileGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            UserProfileBLL userProfileBLL = fxmlLoader.getController();
            userProfileBLL.setValue(user);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Thông tin tài khoản");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGridPane(int col){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(1.0*100/col);
        for(int i =0; i < col; i++){
            gridPane.getColumnConstraints().add(column);
        }
        for(int i =0; i<conferences.size(); i++){
            gridPane.add(new HoiNghiCardBLL(conferences.get(i)), i%GRID_COL, i/GRID_COL);
        }
    }

    private void updateUserField(){
        if(user!=null){
            lbUser.setText(user.getUsername());
            lbLogout.setVisible(true);
            if(user.getIsAdmin()){
                hpConferenceManagement.setDisable(true);
                hpUserManagement.setDisable(true);
            }
        }
    }
}
