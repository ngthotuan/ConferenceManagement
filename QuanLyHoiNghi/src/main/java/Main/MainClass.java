package Main;

import BLL.HomeBLL;
import DAO.BasicDAO;
import DAO.ConferenceDAO;
import DAO.MeetingAccountDAO;
import DAO.UserDAO;
import DTO.Conference;
import DTO.MeetingAccount;
import DTO.MeetingAccountPK;
import DTO.User;
import Utils.MyStage;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

public class MainClass {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir")+"/src/main/resources/Images");
        System.out.println(file.exists());

        System.out.println(new Date().getTime());
    }
}
