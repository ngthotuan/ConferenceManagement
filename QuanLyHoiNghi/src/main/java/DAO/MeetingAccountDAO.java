package DAO;

import DTO.Conference;
import DTO.MeetingAccount;
import DTO.MeetingAccountPK;
import Utils.HibernateUtils;
import org.hibernate.Session;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MeetingAccountDAO extends BasicDAO {
    public static MeetingAccount getMeetingAccount(MeetingAccountPK id){
        MeetingAccount meetingAccount = null;
        Session session = HibernateUtils.getSessionFactory().openSession();
        try{
            meetingAccount = session.get(MeetingAccount.class, id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return meetingAccount;
    }
    public static List<MeetingAccount> getMeetingAccounts(){
        return (List<MeetingAccount>) getAll("MeetingAccount");
    }

    public static boolean createMeetingAccount(MeetingAccount meetingAccount) {
        if(!isExist(new MeetingAccountPK(meetingAccount.getUserId(), meetingAccount.getConferenceId()))){
            Conference conference = ConferenceDAO.getConference(meetingAccount.getConferenceId());
            if(conference.getAcceptedRegister()){
                if(conference.getCurrentPerson() < conference.getLimitPerson()){
                    HashSet<MeetingAccount> cd = new HashSet<>();
                    cd.add(meetingAccount);
                    conference.setMeetingAccountsById(cd);
                    conference.setCurrentPerson(conference.getCurrentPerson()+1);
                    return ConferenceDAO.updateConference(conference);
                }
            }
        }
        return false;
    }
    public static boolean updateMeetingAccount(MeetingAccount meetingAccount) {
        if(getMeetingAccount(new MeetingAccountPK(meetingAccount.getUserId(), meetingAccount.getConferenceId())) == null){
            return false;
        }
        else {
            return update(meetingAccount);
        }
    }
    public static boolean deleteMeetingAccount(MeetingAccount meetingAccount) {
        if (isExist(new MeetingAccountPK(meetingAccount.getUserId(), meetingAccount.getConferenceId()))) {
            Conference conference = ConferenceDAO.getConference(meetingAccount.getConferenceId());
            if (delete(meetingAccount)) {
                conference.getMeetingAccountsById().remove(meetingAccount);
                conference.setCurrentPerson(conference.getCurrentPerson() - 1);
                return ConferenceDAO.updateConference(conference);
            }
        }
        return false;
    }

    public static boolean isExist(MeetingAccountPK meetingAccountPK){
        return getMeetingAccount(meetingAccountPK) != null;
    }

}
