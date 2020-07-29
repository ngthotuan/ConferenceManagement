package BLL;

import DAO.UserDAO;
import Utils.MyAlert;
import Utils.MyStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginBLL implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnSignIn;

    @FXML
    private Button btnSignUp;

    @FXML
    private Label labelError;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSignIn.setOnAction(event -> {
            HomeBLL.user = UserDAO.login(txtUsername.getText(), txtPassword.getText());
            if( HomeBLL.user == null){
                labelError.setVisible(true);
            }
            else if(HomeBLL.user.getIsBlocked()){
                MyAlert.show(Alert.AlertType.ERROR, "Lỗi", "Tài khoản bị khóa",
                        "Vui lòng liên hệ admin để được hỗ trợ");
            }
            else{
                ((Stage)((Node) event.getSource()).getScene().getWindow()).close();
            }
        });
        btnSignIn.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                labelError.setVisible(false);
            }
        }));
        btnSignUp.setOnAction(event -> {
            MyStage.openNewStage("Đăng kí", getClass().getResource("../GUI/RegisterGUI.fxml"));
        });
    }
}

