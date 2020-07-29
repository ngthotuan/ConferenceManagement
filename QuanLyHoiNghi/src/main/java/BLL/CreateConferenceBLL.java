package BLL;

import DAO.ConferenceDAO;
import DAO.PlaceDAO;
import DTO.Conference;
import DTO.Place;
import Utils.MyAlert;
import Utils.MyStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class CreateConferenceBLL implements Initializable {
    private static final String RESOURCE_PATH = "src/main/resources/";
    private File imageFile;

    @FXML
    private ImageView conferenceImage;

    @FXML
    private Button btnChooseImage;

    @FXML
    private TextField txtConferenceName;

    @FXML
    private ComboBox<Place> cbBoxAddress;

    @FXML
    private Button btnCreateNewAddress;

    @FXML
    private TextField txtLimitPerson;

    @FXML
    private Label lbLimitPerson;

    @FXML
    private TextField txtConferenceShortDescription;

    @FXML
    private TextArea txtConferenceDetailDescription;

    @FXML
    private DatePicker dpConferenceDate;

    @FXML
    private ComboBox<String> cbConferenceHours;

    @FXML
    private ComboBox<String> cbConferenceMinute;

    @FXML
    private TextField txtConferenceHour;

    @FXML
    private TextField txtConferenceMinute;

    @FXML
    private Label lbErrors;

    @FXML
    private Button btnCreateConference;

    private ObservableList<Place> places;
    private Place placeSelected;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        processChooseAddress();
        processChooseImage();
        processConferenceDate();
        btnCreateConference.setOnAction(event -> {
            if(userInputValid()){
                Conference conference = new Conference();

                if(imageFile != null){
                    String imagePath = buildImageName(imageFile.getName());
                    if(saveImage(imageFile, imagePath)){
                        conference.setImage(imagePath);
                    }

                }
                conference.setName(txtConferenceName.getText());
                conference.setPlaceId(placeSelected.getId());
                conference.setLimitPerson(Integer.valueOf(txtLimitPerson.getText()));
                conference.setShortDescription(txtConferenceShortDescription.getText());
                conference.setDetailDescription(txtConferenceDetailDescription.getText());

                LocalDate picker = dpConferenceDate.getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.set(picker.getYear(), picker.getMonthValue() - 1, picker.getDayOfMonth(),
                       Integer.parseInt(cbConferenceHours.getValue()),Integer.parseInt(cbConferenceMinute.getValue()), 0);
                Date date = calendar.getTime();
                conference.setHoldTime(date);

                conference.setConferenceTime(Integer.parseInt(txtConferenceHour.getText())*60+Integer.parseInt(txtConferenceMinute.getText()));
                int created = ConferenceDAO.createConference(conference);
                if(created > 0){
                    MyAlert.show(Alert.AlertType.INFORMATION, "Tạo hội nghị",
                            "Thành công","Đã tạo hội nghị thành công");
                }
                else if(created == -1){
                    MyAlert.show(Alert.AlertType.ERROR, "Tạo hội nghị",
                            "Thất bại","Địa điểm này đã dùng cho hội nghị khác cùng thời gian\n" +
                                    "Vui lòng thay đổi thời gian của hội nghị");
                }
                else {
                    MyAlert.show(Alert.AlertType.ERROR, "Tạo hội nghị",
                            "Thất bại","Đã xảy ra lỗi không mong muốn");
                }
            }
        });
        btnCreateConference.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                lbErrors.setVisible(false);
            }
        }));
    }

    private void processConferenceDate() {
        List<String> hours = new ArrayList<>();
        List<String> minutes = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            hours.add(String.format("0%d", i));
            minutes.add(String.format("0%d", i));
        }
        for(int i = 10; i < 24; i++){
            hours.add(String.valueOf(i));
        }
        for(int i = 10; i < 60; i++){
            minutes.add(String.valueOf(i));
        }
        cbConferenceHours.setValue("07");
        cbConferenceMinute.setValue("30");
        cbConferenceHours.setItems(FXCollections.observableList(hours));
        cbConferenceMinute.setItems(FXCollections.observableList(minutes));

        dpConferenceDate.setValue(LocalDate.now());


//        LocalDate picker = dpConferenceDate.getValue();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(picker.getYear(), picker.getMonthValue() - 1, picker.getDayOfMonth(),
//                cbConferenceHours.getValue(), cbConferenceMinute.getValue(), 0);
//        Date date = calendar.getTime();
//        System.out.println(date);
    }

    private boolean userInputValid(){
        StringBuilder builder = new StringBuilder();
        if(placeSelected == null){
            builder.append("Bạn chưa chọn địa điểm\n");
        }
        else{
            try{
                int limit = Integer.parseInt(txtLimitPerson.getText());
                if(limit > placeSelected.getLimitPerson()){
                    builder.append("Hội nghị có số người lớn hơn sức chứa của địa điểm\n");
                }
            } catch (Exception e){
                builder.append("Giới hạn người của hội nghị không hợp lệ\n");
            }
            try{
                Integer.parseInt(txtConferenceHour.getText());
                Integer.parseInt(txtConferenceMinute.getText());
            } catch (Exception e){
                builder.append("Thời gian diễn ra hội nghị không hợp lệ\n");
            }
        }
        if(!builder.toString().isEmpty()){
            lbErrors.setText(builder.toString());
            lbErrors.setVisible(true);
            return false;
        }
        else{
            return true;
        }
    }

    private void processChooseImage() {
        btnChooseImage.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Chọn hình ảnh cho hội nghị");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files",
                    "*.bmp", "*.png", "*.jpg", "*.gif", "*.svg"));
            imageFile = chooser.showOpenDialog(btnChooseImage.getScene().getWindow());
            if(imageFile != null){
                Image image = new Image(imageFile.toURI().toString());
                conferenceImage.setImage(image);
            }
        });
    }

    private String buildImageName(String imageName){
        int pos = imageName.lastIndexOf(".");
        return String.format("Images/%s-%s.%s", imageName.substring(0, pos), new Date().getTime(), imageName.substring(pos+1));
    }
    private boolean saveImage(File file, String imagePath){
        boolean result = true;
        if(file != null){
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(conferenceImage.getImage(),
                        null), imagePath.substring(imagePath.lastIndexOf(".")+1), new File(RESOURCE_PATH+imagePath));
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        }
        else {
            result = false;
        }
        return result;
    }

    private void updateComboboxValue(){
        places = FXCollections.observableList(PlaceDAO.getPlaces());
        cbBoxAddress.setItems(places);
    }
    private void processChooseAddress(){
        updateComboboxValue();
        cbBoxAddress.setConverter(new StringConverter<Place>() {
            @Override
            public String toString(Place object) {
                return String.format("Tên địa điểm:%s%nĐịa chỉ: %s%nGiới hạn: %d người",object.getName(), object.getAddress(), object.getLimitPerson());
            }

            @Override
            public Place fromString(String string) {
                return null;
            }
        });

        cbBoxAddress.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() >= 0){
                placeSelected = places.get(newValue.intValue());
                String limit = placeSelected.getLimitPerson().toString();
                lbLimitPerson.setText(limit);
                txtLimitPerson.setText(limit);
            }
        });

        btnCreateNewAddress.setOnAction(event -> {
            MyStage.openNewStage("Tạo địa điểm mới", getClass().getResource("/GUI/CreatePlaceGUI.fxml"));
            updateComboboxValue();
        });
    }
}
