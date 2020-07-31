package BLL;

import DAO.ConferenceDAO;
import DAO.MeetingAccountDAO;
import DAO.UserDAO;
import DTO.Conference;
import DTO.MeetingAccount;
import DTO.User;
import Model.UserConference;
import Utils.MyAlert;
import Utils.MyStage;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConferenceManagementBLL implements Initializable {

    @FXML
    private TableView<Conference> tbConference;

    @FXML
    private TableColumn<Conference, String> tbcConferenceName;

    @FXML
    private TableColumn<Conference, String> tbcPlaceName;

    @FXML
    private TableColumn<Conference, String> tbcHoldTime;

    @FXML
    private TableColumn<Conference, String> tbcConferenceTime;

    @FXML
    private TableColumn<Conference, String> tbcSignUp;

    @FXML
    private TableColumn<Conference, String> tbcAccept;

    @FXML
    private TableColumn<Conference, String> tbcStatus;

    @FXML
    private TableColumn<Conference, String> tbcAcceptRegister;

    @FXML
    private Button btnCreateConference;

    @FXML
    private Button btnConferenceDetail;

    @FXML
    private Button btnConferenceChange;

    @FXML
    private Button btnAcceptUser;

    @FXML
    private Button btnLockRegister;

    private ObservableList<Conference> conferenceObservableList;
    private Conference conferenceSelected = null;
    private int posSelected = -1;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDataTableView();
        setCellFactory();
        tbConference.setPlaceholder(new Label("Chưa có hội nghị nào"));

        tbConference.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            conferenceSelected = newValue;
            posSelected = conferenceObservableList.indexOf(conferenceSelected);
            if(newValue != null){
                if (newValue.getAcceptedRegister()) {
                    btnLockRegister.setId("block");
                    btnLockRegister.setText("Tạm khóa đăng ký");
                }
                else {
                    btnLockRegister.setId("unblock");
                    btnLockRegister.setText("Mở đăng ký");
                }
            }
        });

        btnLockRegister.setOnAction(event -> {
            if (conferenceSelected == null) {
                MyAlert.show(Alert.AlertType.ERROR, "Lỗi",
                        "Vui lòng chọn hội nghị để thực hiện thao tác" );
            }
            else{
                Optional<ButtonType> optional;
                if(btnLockRegister.getId().equals("block")){
                    optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Khóa đăng ký",
                            "Bạn có thực sự muốn tạm khóa đăng kí hội nghị", conferenceSelected.getName());
                    conferenceSelected.setAcceptedRegister(false);
                }
                else{
                    optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Mở đăng ký",
                            "Bạn có thực sự muốn mở đăng kí hội nghị", conferenceSelected.getName());
                    conferenceSelected.setAcceptedRegister(true);
                }

                if(!optional.get().getButtonData().isCancelButton()){
                    boolean updateUser = ConferenceDAO.updateConference(conferenceSelected);
                    if(updateUser){
                        MyAlert.show(Alert.AlertType.INFORMATION, "Thành công");
                        conferenceObservableList.set(posSelected, conferenceSelected);
                    }
                }
            }
        });

        btnCreateConference.setOnAction(event -> {
            MyStage.openNewStage("Tạo hội nghị mới",
                    getClass().getResource("/GUI/CreateConferenceGUI.fxml"));
            loadDataTableView();
        });

        btnConferenceDetail.setOnAction(event -> {
            if(conferenceSelected == null){
                MyAlert.show(Alert.AlertType.ERROR, "Lỗi", 
                        "Vui lòng chọn hội nghị để thực hiện thao tác" );
            }
            else{
                MyStage.openNewStageWithValue("Chi tiết hội nghị",
                        getClass().getResource("/GUI/DetailConferenceGUI.fxml"),
                        conferenceSelected);
            }

        });

        btnConferenceChange.setOnAction(event -> {
            if(conferenceSelected == null){
                MyAlert.show(Alert.AlertType.ERROR, "Lỗi",
                        "Vui lòng chọn hội nghị để thực hiện thao tác" );
            }
            else{
                MyStage.openNewStageWithValue("Sửa đổi hội nghị",
                        getClass().getResource("/GUI/EditConferenceGUI.fxml"),
                        conferenceSelected);
            }
        });

        btnAcceptUser.setOnAction(event -> {
            if(conferenceSelected == null){
                MyAlert.show(Alert.AlertType.ERROR, "Lỗi",
                        "Vui lòng chọn hội nghị để thực hiện thao tác" );
            }
            else{
                MyStage.openNewStageWithValue(String.format("Danh sách đăng kí của hội nghị %s",conferenceSelected.getName()),
                        getClass().getResource("/GUI/ConferenceAcceptUserGUI.fxml"),
                        conferenceSelected);
                loadDataTableView();
                tbConference.refresh();
            }
        });
    }

    private void setCellFactory() {
        tbcConferenceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcPlaceName.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return param.getValue().getPlaceByPlaceId().getName();
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        tbcHoldTime.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                Conference conference = param.getValue();
                return new SimpleDateFormat("yyyy-MM-dd hh:mm aaa").format(conference.getHoldTime());
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        tbcConferenceTime.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                Conference conference = param.getValue();
                return String.format("%d giờ %d phút",
                        conference.getConferenceTime()/60, conference.getConferenceTime()%60);
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        tbcSignUp.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                Conference conference = param.getValue();
                return String.format("%d/%d", conference.getCurrentPerson(), conference.getLimitPerson());
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        tbcAccept.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                Conference conference = param.getValue();
                int conferenceId = conference.getId();
                List<MeetingAccount> meetingAccountList = MeetingAccountDAO.getMeetingAccountAcceptedByConferenceId(conferenceId);
                return meetingAccountList.size() + "";
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        tbcStatus.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return Conference.isTookPlace(param.getValue()) ? "Đã diễn ra" : "Chưa diễn ra";
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        tbcAcceptRegister.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                Conference conference = param.getValue();
                return conference.getAcceptedRegister() ? "Cho phép ĐK" : "Tạm khóa ĐK";
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
    }

    private void loadDataTableView() {
        conferenceObservableList = FXCollections.observableList(ConferenceDAO.getConferences());
        tbConference.setItems(conferenceObservableList);
    }
}
