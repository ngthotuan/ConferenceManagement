package BLL.Elements;

import BLL.DetailConferenceBLL;
import DTO.Conference;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HoiNghiListBLL extends AnchorPane {

    @FXML
    private Label labelConferenceName;
    @FXML
    private Label labelCurrentPerson;
    @FXML
    private Label labelMaxPerson;
    @FXML
    private Label labelHoldTime;


    private Conference conference;


    public HoiNghiListBLL(Conference conference) {
        super();
        loadFxml(HoiNghiListBLL.class.getResource("../../GUI/Elements/HoiNghiList.fxml"), this);
        this.conference = conference;
        try{
            labelConferenceName.setText(conference.getName());
            labelCurrentPerson.setText(conference.getCurrentPerson().toString());
            labelMaxPerson.setText(conference.getPlaceByPlaceId().getLimitPerson().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
            labelHoldTime.setText(formatter.format(conference.getHoldTime()));

        } catch (NullPointerException e){
            System.err.println("NullPointException");
        }
    }

    public void changeLabel(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../../GUI/DetailConferenceGUI.fxml"));
        Parent parent = null;
        try {
            parent = loader.load();
            stage.setScene(new Scene(parent));
            DetailConferenceBLL detailController = loader.getController();
            detailController.setValue(conference);
        } catch (IOException e) {
            System.out.println(getClass().getName());
            System.err.println(e.getCause().getMessage());
        }
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