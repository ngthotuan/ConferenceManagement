package Model;

import DTO.Conference;
import DTO.MeetingAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserConference {
    private String conferenceName, placeName;
    private String holdTime;
    private String conferenceTime;
    private String state;
    private String status;

    public UserConference() {
    }

    public UserConference(String conferenceName, String placeName, String holdTime, String conferenceTime, String state, String status) {
        this.conferenceName = conferenceName;
        this.placeName = placeName;
        this.holdTime = holdTime;
        this.conferenceTime = conferenceTime;
        this.state = state;
        this.status = status;
    }

    public UserConference(MeetingAccount meetingAccount){
        Conference conference = meetingAccount.getConferenceByConferenceId();
        this.conferenceName = conference.getName();
        this.placeName = conference.getPlaceByPlaceId().getName();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa");
        this.holdTime = formatter.format(conference.getHoldTime());
        this.conferenceTime = String.format("%d giờ %d phút",
                conference.getConferenceTime()/60, conference.getConferenceTime()%60);
        this.state = meetingAccount.getIsAccepted() ? "Đã chấp nhận" : "Chưa chấp nhận";
        this.status = Conference.isTookPlace(conference) ? "Đã diễn ra" : "Chưa diễn ra";
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(String holdTime) {
        this.holdTime = holdTime;
    }

    public String getConferenceTime() {
        return conferenceTime;
    }

    public void setConferenceTime(String conferenceTime) {
        this.conferenceTime = conferenceTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static List<UserConference> convertData(List<MeetingAccount> meetingAccounts){
        List<UserConference> result  = new ArrayList<>();
        meetingAccounts.forEach(meetingAccount -> {
            result.add(new UserConference(meetingAccount));
        });
        return result;
    }
}
