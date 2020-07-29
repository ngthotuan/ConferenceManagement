package Main;

import DAO.UserDAO;
import DTO.User;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class MainClass {

    public static void main(String[] args) throws InterruptedException {
        for(int i = 1 ; i < 9; i++){
            String [] arg = {i+"", i+"", "user name "+i, "user email " +i};
            User user = new User(arg);
            UserDAO.createUser(user);
        }

    }
}
