package BLL.Elements;

import BLL.DetailConferenceBLL;
import DAO.ConferenceDAO;
import DTO.Conference;
import javafx.beans.binding.ObjectBinding;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            labelConferenceName.setText(conference.getId()+conference.getName());
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../../GUI/DetailConferenceGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Chi tiết hội nghị");
            DetailConferenceBLL detailController = fxmlLoader.getController();
            detailController.setValue(conference);
            stage.setScene(scene);
//            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            labelCurrentPerson.textProperty().bind(new ObjectBinding<String>() {
                @Override
                protected String computeValue() {
                    return String.valueOf(ConferenceDAO.getConference(conference.getId()).getCurrentPerson());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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