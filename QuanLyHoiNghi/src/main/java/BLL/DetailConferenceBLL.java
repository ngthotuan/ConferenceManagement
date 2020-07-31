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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


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
    private Button btnJoin;
    @FXML
    private Button btnLeave;

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
    }

    private void updateField(){
        labelCurrentPerson.setText(ConferenceDAO.getConference(conference.getId()).getCurrentPerson().toString());
        if(btnJoin.isVisible()){
            btnJoin.setVisible(false);
            btnJoin.setManaged(false);
            btnLeave.setManaged(true);
            btnLeave.setVisible(true);
        }
        else{
            btnJoin.setVisible(true);
            btnJoin.setManaged(true);
            btnLeave.setManaged(false);
            btnLeave.setVisible(false);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnJoin.setVisible(true);
        btnJoin.setManaged(true);
        btnLeave.setManaged(false);
        btnLeave.setVisible(false);
        tbListJoin.setPlaceholder(new Label("Chưa có user nào đăng kí tham gia"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcState.setCellValueFactory(new PropertyValueFactory<>("state"));

        btnJoin.setOnAction(event -> {
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
            }
            else{
                MeetingAccount meetingAccount = new MeetingAccount(HomeBLL.user.getUsername(), conference.getId());
                joinMeeting(meetingAccount);
            }
        });
        btnLeave.setOnAction(event -> {
            if(Conference.isTookPlace(conference)){
                MyAlert.show(Alert.AlertType.ERROR, "Hủy tham gia hội nghị", "Thất bại",
                        "Không thể hủy tham gia hội nghị đã diễn ra");
            }
            else{
                Optional<ButtonType>  optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Tham gia hội nghị",
                        "Hủy tham gia", "Bạn có chắc chắn muốn hủy tham gia hội nghị này?");
                if (!optional.get().getButtonData().isCancelButton()){
                    MeetingAccount meetingAccount = new MeetingAccount(HomeBLL.user.getUsername(), conference.getId());
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
        });
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
                        new BufferedInputStream(
                                new FileInputStream("Images/"+conference.getImage()))));

            } else{
                imageView.setImage(new Image(
                        String.valueOf(getClass().getResource("../Images/default.png"))));
            }

        } catch (NullPointerException e){
            System.err.println("NullPointException");
        } catch (FileNotFoundException e) {
            System.err.printf("FileNotFoundException %s%n", e.getMessage());
        } catch (IllegalArgumentException e){
            System.err.printf("Invalid URL or resource not found %s%n",conference.getImage());
        } finally {
            if(HomeBLL.user != null){
                if(MeetingAccountDAO.isExist(new MeetingAccountPK(HomeBLL.user.getUsername(), conference.getId()))){
                    btnJoin.setVisible(false);
                    btnJoin.setManaged(false);
                    btnLeave.setManaged(true);
                    btnLeave.setVisible(true);
                }
            }
        }
    }
}
