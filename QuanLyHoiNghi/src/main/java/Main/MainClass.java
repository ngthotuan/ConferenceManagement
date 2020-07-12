package Main;

import DAO.ConferenceController;
import DTO.Conference;

public class MainClass {

    public static void main(String[] args) {
        Conference conference = new Conference();
        conference.setName("conference name test");
        conference.setPlaceId(1);
        System.out.println("ConferenceController.createConference(conference) = " + ConferenceController.createConference(conference));
    }
}
