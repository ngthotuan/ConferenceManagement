package Main;

import Controllers.PojoController.BasicDAO;
import Controllers.PojoController.ConferenceController;
import Controllers.PojoController.PlaceController;
import Controllers.PojoController.UserController;
import Model.Pojo.Conference;
import Model.Pojo.User;

import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        Conference conference = new Conference();
        conference.setName("conference name test");
        conference.setPlaceId(1);
        System.out.println("ConferenceController.createConference(conference) = " + ConferenceController.createConference(conference));
    }
}
