package BLL.Elements;

import DTO.Conference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class HoiNghiCardBLL extends VBox {

    @FXML
    private Label labelConferenceName;
    @FXML
    private Label labelAddress;
    @FXML
    private Label labelCurrentPerson;
    @FXML
    private Label labelMaxPerson;
    @FXML
    private ImageView imageView;

    private Conference conference;

    public HoiNghiCardBLL(Conference conference) {
        super();
        loadFxml(HoiNghiListBLL.class.getResource("../../GUI/Elements/HoiNghiCard.fxml"), this);
        this.conference = conference;
        try{
            labelConferenceName.setText(conference.getName());
            labelAddress.setText(conference.getPlaceByPlaceId().getAddress());
            labelCurrentPerson.setText(conference.getCurrentPerson().toString());
            labelMaxPerson.setText(conference.getPlaceByPlaceId().getLimitPerson().toString());
            imageView.setImage(new Image(
                    String.valueOf(getClass().getResource("../../Images/"+ conference.getImage()))));
        } catch (NullPointerException e){
            System.err.println("NullPointException");
            labelMaxPerson.setText("undefined");
            labelAddress.setText("undefined");
        } catch (IllegalArgumentException e){
            System.err.printf("Invalid URL: Invalid URL or resource not found %s%n", conference.getImage());
            imageView.setImage(new Image(
                    String.valueOf(getClass().getResource("../../Images/team.png"))));

        }
    }

    public void changeLabel()
    {
        labelConferenceName.setText(conference.getPlaceByPlaceId().getAddress());
    }


    protected static void loadFxml(URL fxmlFile, Object rootController) {
        FXMLLoader loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

}