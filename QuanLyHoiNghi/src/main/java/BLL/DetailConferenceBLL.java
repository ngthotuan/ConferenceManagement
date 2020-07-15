package BLL;

import BLL.Elements.HoiNghiListBLL;
import DTO.Conference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class DetailConferenceBLL {
    @FXML
    private Label labelConferenceName;
    @FXML
    private Label labelAddress;
    @FXML
    private Label labelHoldTime;
    @FXML
    private Label labelConferenceTime;
    @FXML
    private Label labelCurrentPerson;
    @FXML
    private Label labelMaxPerson;
    @FXML
    private Label labelDetailDescription;
    @FXML
    private ImageView imageView;

    private Conference conference;

    public void setValue(Conference conference) {
        this.conference = conference;
        try{
            labelConferenceName.setText(conference.getName());
            labelDetailDescription.setText(conference.getDetailDescription());
            labelCurrentPerson.setText(conference.getCurrentPerson().toString());
            labelMaxPerson.setText(conference.getPlaceByPlaceId().getLimitPerson().toString());
            imageView.setImage(new Image(
                    String.valueOf(getClass().getResource("../Images/"+ conference.getImage()))));
            labelAddress.setText(conference.getPlaceByPlaceId().getAddress());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
            labelHoldTime.setText(formatter.format(conference.getHoldTime()));

        } catch (NullPointerException e){
            System.err.println("NullPointException");
        } catch (IllegalArgumentException e){
            System.err.printf("Invalid URL: Invalid URL or resource not found %s%n", conference.getImage());
            imageView.setImage(new Image(
                    String.valueOf(getClass().getResource("../Images/team.png"))));

        }
    }

}
