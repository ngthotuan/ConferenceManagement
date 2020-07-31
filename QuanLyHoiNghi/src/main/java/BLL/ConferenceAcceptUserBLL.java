package BLL;

import DAO.ConferenceDAO;
import DTO.Conference;
import DTO.MeetingAccount;
import Model.JoinAccount;
import Utils.MyAlert;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConferenceAcceptUserBLL extends MyController implements Initializable {

    @FXML
    private TableView<JoinAccount> tbConferenceAcceptUser;

    @FXML
    private TableColumn<JoinAccount, String> tbcUsername;

    @FXML
    private TableColumn<JoinAccount, String> tbcState;

    @FXML
    private TableColumn<JoinAccount, Button> tbcAction;

    @FXML
    private Button btnAcceptAll;


    private ObservableList<JoinAccount> joinAccountObservableList;

    private Conference conference;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellFactory();
        tbConferenceAcceptUser.setPlaceholder(new Label("Chưa có user nào đăng kí tham gia hội nghị này"));
    }

    private void setCellFactory() {
        tbcUsername.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcState.setCellValueFactory(new PropertyValueFactory<>("state"));
        tbcAction.setCellValueFactory(new PropertyValueFactory<>("action"));
 }

    @Override
    public void setValue(Object value) {
        this.conference = (Conference)value;
        List<MeetingAccount> meetingAccountsById = (List<MeetingAccount>) conference.getMeetingAccountsById();
        joinAccountObservableList = FXCollections.observableList(JoinAccount.convertData(meetingAccountsById));
        tbConferenceAcceptUser.setItems(joinAccountObservableList);

        if(meetingAccountsById.isEmpty()){
            btnAcceptAll.setDisable(true);
        }
        btnAcceptAll.setOnAction(event -> {
            Optional<ButtonType> optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Chấp nhập tham gia hội nghị",
                    "Chấp nhận tất cả", "Bạn có chắc chắn muốn chấp nhận hết danh sách user");
            if(!optional.get().getButtonData().isCancelButton()){
                for(MeetingAccount meetingAccount : meetingAccountsById){
                    meetingAccount.setIsAccepted(true);
                }
                conference.setMeetingAccountsById(meetingAccountsById);
                boolean updateConference = ConferenceDAO.updateConference(conference);
                if(updateConference){
                    MyAlert.show(Alert.AlertType.INFORMATION, "Chấp nhập tham gia hội nghị", "Thành công");
                    joinAccountObservableList = FXCollections.observableList(JoinAccount.convertData(meetingAccountsById));
                    tbConferenceAcceptUser.setItems(joinAccountObservableList);
                }
            }
        });
    }
}
