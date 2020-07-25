package BLL;

import DAO.UserDAO;
import Utils.MyAlert;
import Utils.Password;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserChangePasswordBLL implements Initializable {

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Label lbError;

    @FXML
    private Button btnChange;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnChange.setOnAction(event -> {
            String password = txtPassword.getText();
            String newPassword = txtNewPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();

            if(newPassword.trim().isEmpty() || !newPassword.equals(confirmPassword)){
                lbError.setText("Mật khẩu mới trống hoăc không trùng nhau");
                lbError.setVisible(true);
            } else{
                if(UserDAO.login(HomeBLL.user.getUsername(), password) == null){
                    lbError.setText("Mật khẩu cũ không đúng");
                    lbError.setVisible(true);
                }
                else{
                    String oldPassword = HomeBLL.user.getPassword();
                    HomeBLL.user.setPassword(Password.genPassword(newPassword));
                    boolean updated = UserDAO.updateUser(HomeBLL.user);
                    if(updated){
                        MyAlert.show(Alert.AlertType.INFORMATION, "Cập nhật mật khẩu", "Thành công",
                                "Cập nhật mật khẩu thành công!");
                        Stage stage = (Stage) btnChange.getScene().getWindow();
                        stage.close();
                    } else{
                        HomeBLL.user.setPassword(oldPassword);
                        MyAlert.show(Alert.AlertType.ERROR, "Cập nhật mật khẩu", "Lỗi",
                                "Cập nhật mật khẩu thất bại!");
                    }
                }
            }

        });
        btnChange.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                lbError.setVisible(false);
            }
        }));
    }
}