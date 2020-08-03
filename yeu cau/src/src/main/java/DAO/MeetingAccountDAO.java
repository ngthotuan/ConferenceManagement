package DAO;

import DTO.Conference;
import DTO.MeetingAccount;
import DTO.MeetingAccountPK;
import Utils.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
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
//                if(conference.getCurrentPerson() < conference.getLimitPerson()){
//                    HashSet<MeetingAccount> cd = new HashSet<>();
//                    cd.add(meetingAccount);
//                    conference.setMeetingAccountsById(cd);
//                    conference.setCurrentPerson(conference.getCurrentPerson()+1);
//                    return ConferenceDAO.updateConference(conference);
//                }
                HashSet<MeetingAccount> cd = new HashSet<>();
                cd.add(meetingAccount);
                conference.setMeetingAccountsById(cd);
                conference.setCurrentPerson(conference.getCurrentPerson()+1);
                return ConferenceDAO.updateConference(conference);
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

    public static List<MeetingAccount> getMeetingAccountByUserId(String userId){
        List<MeetingAccount> ds = new ArrayList<>();
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            String hql = "from MeetingAccount where userId = :userId";
            Query query = session.createQuery(hql);
            query.setParameter("userId", userId);
            ds = query.list();
        } catch (HibernateException ex) {
            //Log the exception
            System.err.println(ex);
        } finally {
            session.close();
        }
        return ds;
    }

    public static List<MeetingAccount> getMeetingAccountByConferenceId(int conferenceId){
        List<MeetingAccount> ds = new ArrayList<>();
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            String hql = "from MeetingAccount where conferenceId = :conferenceId";
            Query query = session.createQuery(hql);
            query.setParameter("conferenceId", conferenceId);
            ds = query.list();
        } catch (HibernateException ex) {
            //Log the exception
            System.err.println(ex);
        } finally {
            session.close();
        }
        return ds;
    }
    public static List<MeetingAccount> getMeetingAccountAcceptedByConferenceId(int conferenceId){
        List<MeetingAccount> ds = new ArrayList<>();
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
            String hql = "from MeetingAccount where conferenceId = :conferenceId and isAccepted=true";
            Query query = session.createQuery(hql);
            query.setParameter("conferenceId", conferenceId);
            ds = query.list();
        } catch (HibernateException ex) {
            //Log the exception
            System.err.println(ex);
        } finally {
            session.close();
        }
        return ds;
    }
}
