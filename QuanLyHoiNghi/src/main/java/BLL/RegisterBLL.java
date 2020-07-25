package BLL;

import DAO.UserDAO;
import DTO.User;
import Utils.MyAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterBLL implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSignUp;

    @FXML
    private Label labelError;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSignUp.setOnAction(event -> {
            String [] names = {"Tài khoản", "Mật khẩu", "Họ tên", "Email"};
            String [] userInput = {txtUsername.getText(), txtPassword.getText(), txtName.getText(), txtEmail.getText()};
            StringBuilder builder = new StringBuilder();
            for(int i =0; i < userInput.length; i++){
                if(userInput[i].isEmpty()){
                    builder.append(names[i]).append(" không được trống\n");
                }
            }
            if(!builder.toString().isEmpty()){
                labelError.setText(builder.toString());
                labelError.setVisible(true);
            }
            else{
                User user = new User(userInput);
                boolean created = UserDAO.createUser(user);
                if(created){
                    MyAlert.show(Alert.AlertType.INFORMATION, "Đăng kí", "Thành công",
                            "Đăng kí tài khoản thành công bạn có thể đăng nhập!");
                    Stage stage = (Stage) btnSignUp.getScene().getWindow();
                    stage.close();
                } else{
                    MyAlert.show(Alert.AlertType.ERROR, "Đăng kí", "Lỗi",
                            "Tên đăng nhập đã tồn tại!");
                }
            }
        });
        btnSignUp.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                labelError.setVisible(false);
            }
        }));
    }
}
