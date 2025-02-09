package BLL.Elements;

import DAO.ConferenceDAO;
import DTO.Conference;
import Utils.MyStage;
import javafx.beans.binding.ObjectBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

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
            labelMaxPerson.setText(conference.getLimitPerson().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
            labelHoldTime.setText(formatter.format(conference.getHoldTime()));

        } catch (NullPointerException e){
            System.err.println("NullPointException");
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