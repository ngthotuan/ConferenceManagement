package BLL.Elements;

import DAO.ConferenceDAO;
import DTO.Conference;
import Utils.MyStage;
import javafx.beans.binding.ObjectBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class HoiNghiCardBLL extends AnchorPane {

    @FXML
    private Label labelConferenceName;
    @FXML
    private Label labelAddress;
    @FXML
    private Label labelCurrentPerson;
    @FXML
    private Label labelMaxPerson;
    @FXML
    private Label labelHoldTime;
    @FXML
    private Label labelShortDescription;
    @FXML
    private ImageView imageView;

    private Conference conference;

    public HoiNghiCardBLL(Conference conference) {
        super();
        loadFxml(HoiNghiListBLL.class.getResource("../../GUI/Elements/HoiNghiCard.fxml"), this);
        this.conference = conference;
        try{
            labelConferenceName.setText("Tên: " + conference.getName());
            labelShortDescription.setText("Mô tả: "+ conference.getShortDescription());
            labelCurrentPerson.setText(conference.getCurrentPerson().toString());
            labelMaxPerson.setText(conference.getLimitPerson().toString());

            if(conference.getImage() != null){
                imageView.setImage(new Image(
                        new BufferedInputStream(
                                new FileInputStream("Images/"+conference.getImage()))));
            }
            else{
                imageView.setImage(new Image(
                        String.valueOf(getClass().getResource("../../Images/default.png"))));
            }

            labelAddress.setText("Địa điểm: " + conference.getPlaceByPlaceId().getAddress());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
            labelHoldTime.setText(formatter.format(conference.getHoldTime()));

        } catch (NullPointerException e){
            System.err.println("NullPointException");
        } catch (FileNotFoundException e) {
            System.err.printf("FileNotFoundException %s%n", e.getMessage());
        } catch (IllegalArgumentException e){
            System.err.printf("Invalid URL or resource not found %s%n",conference.getImage());
        }
    }

    public void seeDetail(ActionEvent event)
    {
        MyStage.openNewStageWithValue("Chi tiết hội nghị",
                getClass().getResource("../../GUI/DetailConferenceGUI.fxml"),
                conference);
        labelCurrentPerson.textProperty().bind(new ObjectBinding<String>() {
            @Override
            protected String computeValue() {
                return String.valueOf(ConferenceDAO.getConference(conference.getId()).getCurrentPerson());
            }
        });
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