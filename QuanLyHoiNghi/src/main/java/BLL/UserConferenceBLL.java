package BLL;

import DAO.MeetingAccountDAO;
import DTO.Conference;
import DTO.MeetingAccount;
import Model.JoinAccount;
import Model.UserConference;
import Utils.MyAlert;
import Utils.MyStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class UserConferenceBLL implements Initializable {

    @FXML
    private TableView<UserConference> tbUserConference;

    @FXML
    private TableColumn<UserConference, String> tbcConferenceName;

    @FXML
    private TableColumn<UserConference, String> tbcPlaceName;

    @FXML
    private TableColumn<UserConference, String> tbcHoldTime;

    @FXML
    private TableColumn<UserConference, String> tbcConferenceTime;

    @FXML
    private TableColumn<UserConference, String> tbcState;

    @FXML
    private TableColumn<UserConference, String> tbcStatus;


    @FXML
    private Button btnOutConference;

    @FXML
    private Button btnConferenceDetail;

    @FXML
    private TextField lbSearch;

    ObservableList<UserConference> userConferenceObservableList;
    List<MeetingAccount> meetingAccountList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDataTableView();
        setCellFactory();
        tbUserConference.setPlaceholder(new Label("Bạn chưa tham gia hội nghị nào !!!"));

        lbSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                loadDataTableView();
            }
            else{
                tbUserConference.setItems(findUserConference(newValue));
            }
        });

        btnConferenceDetail.setOnAction(event -> {
            int posSelected = tbUserConference.getSelectionModel().getSelectedIndex();
            if(posSelected == -1){
                MyAlert.show(Alert.AlertType.ERROR, "Vui lòng chọn hội nghị để thực hiện thao tác");
            }
            else {
                Conference conferenceSelected = meetingAccountList.get(posSelected).getConferenceByConferenceId();
                MyStage.openNewStageWithValue("Chi tiết hội nghị",
                        getClass().getResource("/GUI/DetailConferenceGUI.fxml"),
                        conferenceSelected);
            }
        });

        btnOutConference.setOnAction(event -> {
            int posSelected = tbUserConference.getSelectionModel().getSelectedIndex();
            if(posSelected == -1){
                MyAlert.show(Alert.AlertType.ERROR, "Vui lòng chọn hội nghị để thực hiện thao tác");
            }
            else {
                Conference conferenceSelected = meetingAccountList.get(posSelected).getConferenceByConferenceId();
                if(Conference.isTookPlace(conferenceSelected)){
                    MyAlert.show(Alert.AlertType.ERROR, "Hủy tham gia hội nghị", "Thất bại",
                            "Không thể hủy tham gia hội nghị đã diễn ra");
                }
                else{
                    Optional<ButtonType> optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Tham gia hội nghị",
                            "Hủy tham gia", "Bạn có chắc chắn muốn hủy tham gia hội nghị này?");
                    if (!optional.get().getButtonData().isCancelButton()){
                        MeetingAccount meetingAccount = new MeetingAccount(HomeBLL.user.getUsername(), conferenceSelected.getId());
                        boolean leave = MeetingAccountDAO.deleteMeetingAccount(meetingAccount);
                        if(leave){
                            MyAlert.show(Alert.AlertType.INFORMATION, "Hủy tham gia hội nghị", "Thành công",
                                    "Hủy tham gia hội nghị thành công");
                            userConferenceObservableList.remove(posSelected);
                        } else{
                            MyAlert.show(Alert.AlertType.ERROR, "Hủy tham gia hội nghị", "Thất bại",
                                    "Hủy tham gia hội nghị thất bại");
                        }
                    }
                }

            }
        });


    }

    private void setCellFactory() {
        tbcConferenceName.setCellValueFactory(new PropertyValueFactory<>("conferenceName"));
        tbcPlaceName.setCellValueFactory(new PropertyValueFactory<>("placeName"));
        tbcHoldTime.setCellValueFactory(new PropertyValueFactory<>("holdTime"));
        tbcConferenceTime.setCellValueFactory(new PropertyValueFactory<>("conferenceTime"));
        tbcState.setCellValueFactory(new PropertyValueFactory<>("state"));
        tbcStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDataTableView() {
        meetingAccountList = MeetingAccountDAO.getMeetingAccountByUserId(HomeBLL.user.getUsername());
        userConferenceObservableList = FXCollections.observableList(UserConference.convertData(meetingAccountList));
        tbUserConference.setItems(userConferenceObservableList);
    }
    private ObservableList<UserConference> findUserConference(String key){
        List<UserConference> userConferences = new ArrayList<>();
        for(UserConference userConference : userConferenceObservableList){
            if(userConference.getConferenceName().contains(key) ||
                userConference.getPlaceName().contains(key)){
                userConferences.add(userConference);
            }
        }
        return FXCollections.observableList(userConferences);
    }
}
