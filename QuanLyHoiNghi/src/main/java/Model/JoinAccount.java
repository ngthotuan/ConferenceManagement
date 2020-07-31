package Model;

import DAO.MeetingAccountDAO;
import DAO.UserDAO;
import DTO.Conference;
import DTO.MeetingAccount;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JoinAccount{
    private static final String[] stateName = {"Đã chấp nhận","Chưa chấp nhận"};
    private static final String[] actionName = {"Hủy chấp nhận tham dự","Chấp nhận tham dự"};
    private boolean status;
    private String name;
    private Button action;
    private SimpleStringProperty state ;

    private void updateField(){
        if(status){
            action.setText(actionName[0]);
            state.set(stateName[0]);
        }
        else{
            action.setText(actionName[1]);
            state.set(stateName[1]);
        }
    }

    public JoinAccount(MeetingAccount meetingAccount) {
        this.status = meetingAccount.getIsAccepted();
        this.name = UserDAO.getUser(meetingAccount.getUserId()).getName();
        this.state = new SimpleStringProperty();
        this.action = new Button();
        updateField();
        this.action.setOnAction(event -> {
            meetingAccount.setIsAccepted(!status);
            boolean updateMeetingAccount = MeetingAccountDAO.updateMeetingAccount(meetingAccount);
            if(updateMeetingAccount){
                status = !status;
                updateField();
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }

    public static List<JoinAccount> convertData(List<MeetingAccount> meetingAccounts){
        List<JoinAccount> result  = new ArrayList<>();
        meetingAccounts.forEach(meetingAccount -> {
            result.add(new JoinAccount(meetingAccount));
        });
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinAccount that = (JoinAccount) o;
        return Objects.equals(name, that.name) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status);
    }

    @Override
    public String toString() {
        return "JoinAccount{" +
                "name='" + name + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}