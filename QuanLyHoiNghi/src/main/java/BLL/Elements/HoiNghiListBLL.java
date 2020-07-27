package BLL.Elements;

import DTO.Conference;
import Utils.MyStage;
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
            labelConferenceName.setText(conference.getId()+conference.getName());
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
        MyStage.openStageWithValue(event,
                "Chi tiết hội nghị",
                getClass().getResource("../../GUI/DetailConferenceGUI.fxml"),
                conference);
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