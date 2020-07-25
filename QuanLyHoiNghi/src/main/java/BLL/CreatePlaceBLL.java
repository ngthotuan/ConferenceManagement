package BLL;

import DAO.PlaceDAO;
import DAO.UserDAO;
import DTO.Place;
import DTO.User;
import Utils.MyAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class CreatePlaceBLL implements Initializable {

    @FXML
    private TextField txtPlaceName;

    @FXML
    private TextField txtPlaceAddress;

    @FXML
    private TextField txtPlaceLimit;

    @FXML
    private Button btnCreate;

    @FXML
    private Label labelError;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         btnCreate.setOnAction(event -> {
             String [] names = {"Tên địa điểm", "Địa chỉ", "Giới hạn người"};
             String [] placeInput = {txtPlaceName.getText(), txtPlaceAddress.getText(), txtPlaceLimit.getText()};
             StringBuilder builder = new StringBuilder();
             for(int i =0; i < placeInput.length; i++){
                 if(placeInput[i].isEmpty()){
                     builder.append(names[i]).append(" không được trống\n");
                 }
             }
             int limitPerson = 0;
             try{
                 limitPerson = Integer.parseInt(placeInput[2]);
             } catch (NumberFormatException e){
                 builder.append(names[2]).append(" không phải là số nguyên");
             }
             if(!builder.toString().isEmpty()){
                 labelError.setText(builder.toString());
                 labelError.setVisible(true);
             }
             else{
                 Place place = new Place();
                 place.setName(placeInput[0]);
                 place.setAddress(placeInput[1]);
                 place.setLimitPerson(limitPerson);
                 boolean created = PlaceDAO.createPlace(place);
                 if(created){
                     MyAlert.show(Alert.AlertType.INFORMATION, "Địa điểm", "Thành công",
                             "Tạo địa điểm thành công");
                     Stage stage = (Stage) btnCreate.getScene().getWindow();
                     stage.close();
                 } else{
                     MyAlert.show(Alert.AlertType.ERROR, "Địa điểm", "Lỗi",
                             "Tạo địa điểm thất bại");
                 }
             }
         });

        btnCreate.focusedProperty().addListener(((observable, oldValue, newValue) -> {
        if(!newValue){
            labelError.setVisible(false);
        }
    }));
    }
}
