package Main;

import DAO.ConferenceDAO;
import DAO.UserDAO;
import DTO.Conference;
import DTO.MeetingAccount;
import DTO.User;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class MainClass {

    public static void main(String[] args) throws InterruptedException {
//        for(int i = 1 ; i < 9; i++){
//            String [] arg = {i+"", i+"", "user name "+i, "user email " +i};
//            User user = new User(arg);
//            UserDAO.createUser(user);
//        }


        Conference conference = ConferenceDAO.getConference(28);
        Collection<MeetingAccount> meetingAccountsById = conference.getMeetingAccountsById();
        for(MeetingAccount meetingAccount : meetingAccountsById){
            meetingAccount.setIsAccepted(true);
        }
        conference.setMeetingAccountsById(meetingAccountsById);
        ConferenceDAO.updateConference(conference);
        System.out.println(meetingAccountsById);

    }
}
