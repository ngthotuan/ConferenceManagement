package BLL;

import DAO.ConferenceDAO;
import DAO.MeetingAccountDAO;
import DTO.Conference;
import DTO.MeetingAccount;
import DTO.MeetingAccountPK;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class DetailConferenceBLL implements Initializable {
    @FXML
    private Label labelConferenceName;
    @FXML
    private Label labelAddress;
    @FXML
    private Label labelHoldTime;
    @FXML
    private Label labelConferenceTime;
    @FXML
    private Label labelCurrentPerson;
    @FXML
    private Label labelMaxPerson;
    @FXML
    private Label labelDetailDescription;
    @FXML
    private ImageView imageView;

    @FXML
    private Button btnJoinAndLeave;
    private final String BTN_JOIN_ID = "btnJoin";
    private final String BTN_LEAVE_ID = "btnLeave";
    private final String JOIN = "Đăng ký tham dự";
    private final String LEAVE = "Hủy đăng ký tham dự";

    private Conference conference;

    public void setValue(Conference conference) {
        this.conference = conference;
        try{
            labelConferenceName.setText(conference.getName());
            labelDetailDescription.setText(conference.getDetailDescription());
            labelCurrentPerson.setText(conference.getCurrentPerson().toString());
            labelMaxPerson.setText(conference.getPlaceByPlaceId().getLimitPerson().toString());
            imageView.setImage(new Image(
                    String.valueOf(getClass().getResource("../Images/"+ conference.getImage()))));
            labelAddress.setText(conference.getPlaceByPlaceId().getAddress());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
            labelHoldTime.setText(formatter.format(conference.getHoldTime()));

        } catch (NullPointerException e){
            System.err.println("NullPointException");
        } catch (IllegalArgumentException e){
            System.err.printf("Invalid URL: Invalid URL or resource not found %s%n", conference.getImage());
            imageView.setImage(new Image(
                    String.valueOf(getClass().getResource("../Images/team.png"))));

        } finally {
            if(HomeBLL.user != null){
                if(MeetingAccountDAO.isExist(new MeetingAccountPK(HomeBLL.user.getUsername(), conference.getId()))){
                    //updateButton();
                    btnJoinAndLeave.setText(LEAVE);
                    btnJoinAndLeave.setId(BTN_LEAVE_ID);
                }
            }
        }
    }
    public void backToMain(ActionEvent event){
        Stage stage = (Stage) ((Node)(event.getSource())).getScene().getWindow();
        stage.close();
    }

    private Optional<ButtonType> alert(Alert.AlertType type, String title, String header, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
    private void updateField(){
        labelCurrentPerson.setText(ConferenceDAO.getConference(conference.getId()).getCurrentPerson().toString());
        if(btnJoinAndLeave.getId().equals(BTN_JOIN_ID)){
            btnJoinAndLeave.setText(LEAVE);
            btnJoinAndLeave.setId(BTN_LEAVE_ID);
        } else {
            btnJoinAndLeave.setText(JOIN);
            btnJoinAndLeave.setId(BTN_JOIN_ID);
        }
    }
    private void joinMeeting(MeetingAccount meetingAccount){
        btnJoinAndLeave.setId(BTN_JOIN_ID);
        boolean joined = MeetingAccountDAO.createMeetingAccount(meetingAccount);
        if(joined){
            alert(Alert.AlertType.INFORMATION, "Tham gia hội nghị", "Thành công",
                 "Tham gia hội nghị thành công");
            updateField();
        } else{
            alert(Alert.AlertType.ERROR, "Tham gia hội nghị", "Thất bại",
                    "Tham gia hội nghị thất bại");
        }
    }
    private void leaveMeeting(MeetingAccount meetingAccount){
        boolean leave = MeetingAccountDAO.deleteMeetingAccount(meetingAccount);
        if(leave){
            updateField();
            alert(Alert.AlertType.INFORMATION, "Hủy tham gia hội nghị", "Thành công",
                    "Hủy tham gia hội nghị thành công");
        } else{
            alert(Alert.AlertType.ERROR, "Hủy tham gia hội nghị", "Thất bại",
                    "Hủy tham gia hội nghị thất bại");
        }
    }
    public void joinConference(ActionEvent event) {
        if(HomeBLL.user == null){
            // user is not login
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../GUI/Login.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if(MeetingAccountDAO.isExist(new MeetingAccountPK(HomeBLL.user.getUsername(), conference.getId()))){
                alert(Alert.AlertType.ERROR, "Tham gia hội nghị", "Lỗi",
                        "Bạn đã đăng kí tham gia hội nghị này từ trước");
                updateField();

            } else {
                joinMeeting(new MeetingAccount(HomeBLL.user.getUsername(), conference.getId()));
            }
        }else{
            // user already login
                // User already join -> Huy dang ki
                MeetingAccount meetingAccount = new MeetingAccount(HomeBLL.user.getUsername(), conference.getId());
                if(btnJoinAndLeave.getId().equals(BTN_LEAVE_ID)){
                    Optional<ButtonType>  optional = alert(Alert.AlertType.CONFIRMATION, "Tham gia hội nghị",
                            "Hủy tham gia", "Bạn có chắc chắn muốn hủy tham gia hội nghị này?");
                    if (!optional.get().getButtonData().isCancelButton()){
                        leaveMeeting(meetingAccount);
                    }
                }
                // User not join -> Dang ki tham du
                else{
                    joinMeeting(meetingAccount);
                }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnJoinAndLeave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                joinConference(event);
            }
        });
    }
}
