package BLL;

import DAO.UserDAO;
import DTO.User;
import Utils.MyAlert;
import Utils.MyStage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileBLL extends MyController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private Button btnChange;

    @FXML
    private Button btnUpdate;

    @FXML
    private Hyperlink hpChangePassword;

    private User user;

    public void setValue(User user) {
        this.user = user;
        try{
            txtUsername.setText(user.getUsername());
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnChange.setOnAction(event -> {
            btnChange.setVisible(false);
            btnUpdate.setVisible(true);
            txtName.setDisable(false);
            txtEmail.setDisable(false);
        });

        btnUpdate.setOnAction(event -> {
            user.setName(txtName.getText());
            user.setEmail(txtEmail.getText());
            boolean updated = UserDAO.updateUser(user);
            if(updated){
              MyAlert.show(Alert.AlertType.INFORMATION,"Cập nhật thông tin", "Thành công", "Thông tin đã được cập nhật");
                btnChange.setVisible(true);
                btnUpdate.setVisible(false);
                txtName.setDisable(true);
                txtEmail.setDisable(true);
            } else{
                MyAlert.show(Alert.AlertType.ERROR,"Cập nhật thông tin", "Thất bại", "Đã xảy ra lỗi không mong muốn");
            }
        });

        hpChangePassword.setOnMouseClicked(event -> {
            MyStage.openNewStage("Đổi mật khẩu", getClass().getResource("../GUI/UserChangePasswordGUI.fxml"));
        });
    }

    @Override
    public void setValue(Object value) {
        try{
            this.user = (User) value;
            txtUsername.setText(user.getUsername());
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
