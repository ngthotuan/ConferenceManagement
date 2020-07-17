package BLL;

import DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
            } else{
                ((Stage)((Node) event.getSource()).getScene().getWindow()).close();
            }
        });
        txtUsername.setOnMouseClicked(event -> {
            labelError.setVisible(false);
        });
        txtPassword.setOnMouseClicked(event -> {
            labelError.setVisible(false);
        });
    }
}

