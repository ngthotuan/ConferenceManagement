package BLL;

import BLL.Elements.HoiNghiCardBLL;
import BLL.Elements.HoiNghiListBLL;
import DAO.ConferenceDAO;
import DTO.Conference;
import DTO.User;
import Utils.MyStage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private Label lbUser;
    @FXML
    private Hyperlink hpLogout;
    @FXML
    private Button btnLogin;

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

    @FXML
    private VBox layoutManage;

    @FXML
    private VBox layoutAdmin;

    private List<Conference> conferences;

    public static User user = null;// new User("admin", "admin");
    public static SimpleStringProperty username = new SimpleStringProperty("Khách");
    public static SimpleIntegerProperty userType = new SimpleIntegerProperty(-1);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindComponent();
        hpUserProfile.setOnMouseClicked(event -> {
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
        hpUserManagement.setOnAction(event -> {
            MyStage.openNewStage("Quản lý User", getClass().getResource("../GUI/UserManagementGUI.fxml"));
        });
        //user login

         btnLogin.setOnAction(event -> {
            if(user == null){
                MyStage.openNewStage("Đăng nhập", getClass().getResource("../GUI/LoginGUI.fxml"));
            }
        });
        //user logout
        hpLogout.setOnMouseClicked(event -> {
            user = null;
            username.set("Khách");
            userType.set(-1);
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

    private void bindComponent(){
        hpLogout.visibleProperty().bind(userType.greaterThan(-1));
        lbUser.textProperty().bind(username);
        layoutManage.managedProperty().bind(userType.greaterThan(-1));
        layoutManage.visibleProperty().bind(userType.greaterThan(-1));
        layoutAdmin.managedProperty().bind(userType.greaterThan(0));
        layoutAdmin.visibleProperty().bind(userType.greaterThan(0));
        hpLogout.visibleProperty().bind(userType.greaterThan(-1));
        btnLogin.visibleProperty().bind(userType.isEqualTo(-1));
    }
}
