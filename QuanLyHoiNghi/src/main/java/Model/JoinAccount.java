package Model;

import DAO.UserDAO;
import DTO.MeetingAccount;

import java.util.ArrayList;
import java.util.List;

public class JoinAccount{
    private static final String[] stateName = {"Đã chấp nhận","Chưa chấp nhận"};
    private String name, state;

    public JoinAccount(String name, String state) {
        this.name = name;
        this.state = state;
    }

    public JoinAccount(MeetingAccount meetingAccount) {
        this.name = UserDAO.getUser(meetingAccount.getUserId()).getName();
        this.state = meetingAccount.getIsAccepted() ? stateName[0] : stateName[1];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static List<JoinAccount> convertData(List<MeetingAccount> meetingAccounts){
        List<JoinAccount> result  = new ArrayList<>();
        meetingAccounts.forEach(meetingAccount -> {
            result.add(new JoinAccount(meetingAccount));
        });
        return result;
    }

    @Override
    public String toString() {
        return "JoinAccount{" +
                "name='" + name + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
