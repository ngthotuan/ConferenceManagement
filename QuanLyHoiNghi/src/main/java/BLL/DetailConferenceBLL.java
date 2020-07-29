package BLL;

import DAO.ConferenceDAO;
import DAO.MeetingAccountDAO;
import DTO.Conference;
import DTO.MeetingAccount;
import DTO.MeetingAccountPK;
import Model.JoinAccount;
import Utils.MyAlert;
import Utils.MyStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


public class DetailConferenceBLL extends MyController implements Initializable {

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

    @FXML
    private TableView<JoinAccount> tbListJoin;
    @FXML
    private TableColumn<JoinAccount, String> tcName;
    @FXML
    private TableColumn<JoinAccount, String> tcState;

    private ObservableList<JoinAccount> list;

    private Conference conference;

    public void backToMain(ActionEvent event){
        Stage stage = (Stage) ((Node)(event.getSource())).getScene().getWindow();
        stage.close();
//        try{
//            Parent root = FXMLLoader.load(getClass().getResource("../GUI/HomeGUI.fxml"));
//            Scene scene = new Scene(root);
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
////            stage.setTitle("Hội nghị");
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void updateField(){
        labelCurrentPerson.setText(ConferenceDAO.getConference(conference.getId()).getCurrentPerson().toString());
        if(btnJoinAndLeave.getId().equals(BTN_LEAVE_ID)){
            btnJoinAndLeave.setText(JOIN);
            btnJoinAndLeave.setId(BTN_JOIN_ID);
        } else {
            btnJoinAndLeave.setText(LEAVE);
            btnJoinAndLeave.setId(BTN_LEAVE_ID);
        }
    }
    private void joinMeeting(MeetingAccount meetingAccount){
        boolean joined = MeetingAccountDAO.createMeetingAccount(meetingAccount);
        if(joined){
            MyAlert.show(Alert.AlertType.INFORMATION, "Tham gia hội nghị", "Thành công",
                 "Tham gia hội nghị thành công");
            list.add(new JoinAccount(meetingAccount));
            updateField();
        } else{
            MyAlert.show(Alert.AlertType.ERROR, "Tham gia hội nghị", "Thất bại",
                    "Tham gia hội nghị thất bại");
        }
    }
    private void leaveMeeting(MeetingAccount meetingAccount){
        if(conference.getHoldTime().getTime() < Calendar.getInstance().getTime().getTime()){
            MyAlert.show(Alert.AlertType.ERROR, "Hủy tham gia hội nghị", "Thất bại",
                    "Không thể hủy tham gia hội nghị đã diễn ra");
        }
        else{
            boolean leave = MeetingAccountDAO.deleteMeetingAccount(meetingAccount);
            if(leave){
                MyAlert.show(Alert.AlertType.INFORMATION, "Hủy tham gia hội nghị", "Thành công",
                        "Hủy tham gia hội nghị thành công");
                updateField();
                list.remove(new JoinAccount(meetingAccount));
            } else{
                MyAlert.show(Alert.AlertType.ERROR, "Hủy tham gia hội nghị", "Thất bại",
                        "Hủy tham gia hội nghị thất bại");
            }
        }
    }
    public void joinAndLeaveMeeting(ActionEvent event) {
        if(HomeBLL.user == null){
            // user is not login
            MyStage.openNewStage("Login", getClass().getResource("../GUI/LoginGUI.fxml"));

            if(HomeBLL.user != null){
                if(MeetingAccountDAO.isExist(new MeetingAccountPK(HomeBLL.user.getUsername(), conference.getId()))){
                    MyAlert.show(Alert.AlertType.ERROR, "Tham gia hội nghị", "Lỗi",
                            "Bạn đã đăng kí tham gia hội nghị này từ trước");
                    updateField();
                } else {
                    joinMeeting(new MeetingAccount(HomeBLL.user.getUsername(), conference.getId()));
                }
            }
        }else{
            // user already login
                // User already join -> Cancel
            MeetingAccount meetingAccount = new MeetingAccount(HomeBLL.user.getUsername(), conference.getId());
                if(btnJoinAndLeave.getId().equals(BTN_LEAVE_ID)){
                    Optional<ButtonType>  optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Tham gia hội nghị",
                            "Hủy tham gia", "Bạn có chắc chắn muốn hủy tham gia hội nghị này?");
                    if (!optional.get().getButtonData().isCancelButton()){
                        leaveMeeting(meetingAccount);
                    }
                }
                // User not join -> Join
                else{
                    joinMeeting(meetingAccount);
                }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnJoinAndLeave.setOnAction(this::joinAndLeaveMeeting);
        tbListJoin.setPlaceholder(new Label("Chưa có user nào đăng kí tham gia"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcState.setCellValueFactory(new PropertyValueFactory<>("state"));
    }

    @Override
    public void setValue(Object value) {
        try{
            this.conference = (Conference)value;
            List<MeetingAccount> meetingAccountsById = (List<MeetingAccount>) conference.getMeetingAccountsById();
            list = FXCollections.observableList(JoinAccount.convertData(meetingAccountsById));
            tbListJoin.setItems(list);

            labelConferenceName.setText(conference.getName());
            labelDetailDescription.setText(conference.getDetailDescription());
            labelCurrentPerson.setText(conference.getCurrentPerson().toString());
            labelMaxPerson.setText(conference.getLimitPerson().toString());
            labelAddress.setText(conference.getPlaceByPlaceId().getAddress());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
            labelHoldTime.setText(formatter.format(conference.getHoldTime()));
            labelConferenceTime.setText(String.format("%d giờ %d phút",
                    conference.getConferenceTime()/60, conference.getConferenceTime()%60));
            if(conference.getImage() != null){
                imageView.setImage(new Image(
                        String.valueOf(getClass().getResource("../" + conference.getImage()))));
            } else{
                imageView.setImage(new Image(
                        String.valueOf(getClass().getResource("../Images/default.png"))));
            }

        } catch (NullPointerException e){
            System.err.println("NullPointException");
        } catch (IllegalArgumentException e){
            System.err.printf("Invalid URL or resource not found %s%n",conference.getImage());
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
}
