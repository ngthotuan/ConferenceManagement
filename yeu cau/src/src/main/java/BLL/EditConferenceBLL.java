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

public class EditConferenceBLL extends MyController implements Initializable {
    public static final String IMAGE_FOLDER = "Images";

    private File imageFile;

    private Conference conference;

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
    private Button btnUpdateConference;

    private ObservableList<Place> places;
    private Place placeSelected;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        processChooseAddress();
        processChooseImage();
        processConferenceDate();
        btnUpdateConference.setOnAction(event -> {
            if(userInputValid()){
                if(imageFile != null){
                    String imageName = buildImageName(imageFile.getName());
                    if(saveImage(imageFile, imageName)){
                        conference.setImage(imageName);
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
                        cbConferenceHours.getSelectionModel().getSelectedIndex(),cbConferenceMinute.getSelectionModel().getSelectedIndex(), 0);
                Date date = calendar.getTime();
                conference.setHoldTime(date);

                conference.setConferenceTime(Integer.parseInt(txtConferenceHour.getText())*60+Integer.parseInt(txtConferenceMinute.getText()));
                boolean updateConference = ConferenceDAO.updateConference(conference);
                if(updateConference){
                    MyAlert.show(Alert.AlertType.INFORMATION, "Cập nhật hội nghị",
                            "Thành công","Đã cập nhật thông tin hội nghị thành công");
                    MyStage.close(event);
                }
                else {
                    MyAlert.show(Alert.AlertType.ERROR, "Cập nhật hội nghị",
                            "Thất bại","Đã xảy ra lỗi không mong muốn");
                }
            }
        });
        btnUpdateConference.focusedProperty().addListener(((observable, oldValue, newValue) -> {
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

        cbConferenceHours.setItems(FXCollections.observableList(hours));
        cbConferenceMinute.setItems(FXCollections.observableList(minutes));
        cbConferenceHours.getSelectionModel().select(7);
        cbConferenceMinute.getSelectionModel().select(0);

        dpConferenceDate.setValue(LocalDate.now());
    }

    private boolean userInputValid(){
        StringBuilder builder = new StringBuilder();
        if(placeSelected == null){
            builder.append("Bạn chưa chọn địa điểm\n");
        }
        else{
            if(txtConferenceName.getText().isEmpty()){
                builder.append("Tên hội nghị không được trống\n");
            }
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
        return String.format("%s.%s", new Date().getTime(), imageName.substring(pos+1));
    }

    private boolean saveImage(File file, String imagePath){
        boolean result = true;
        if(file != null){
            try {
                new File(CreateConferenceBLL.IMAGE_FOLDER).mkdir();
                ImageIO.write(SwingFXUtils.fromFXImage(conferenceImage.getImage(),
                        null), imagePath.substring(imagePath.lastIndexOf(".")+1), new File(String.format("%s/%s", IMAGE_FOLDER, imagePath)));
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
                return String.format("Tên địa điểm:%s%nĐịa chỉ: %s%nGiới hạn: %d người",
                        object.getName(), object.getAddress(), object.getLimitPerson());
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
            MyStage.openNewStage("Tạo địa điểm mới",
                    getClass().getResource("/GUI/CreatePlaceGUI.fxml"));
            updateComboboxValue();
        });
    }

    @Override
    public void setValue(Object value) {
        this.conference = (Conference)value;

        txtConferenceName.setText(conference.getName());
        cbBoxAddress.getSelectionModel().select(conference.getPlaceByPlaceId());
        txtLimitPerson.setText(conference.getLimitPerson().toString());
        txtConferenceShortDescription.setText(conference.getShortDescription());
        txtConferenceDetailDescription.setText(conference.getDetailDescription());
        Date date = conference.getHoldTime();
        dpConferenceDate.setValue(LocalDate.of(1900+date.getYear(),1+date.getMonth(),date.getDate()));
        cbConferenceHours.getSelectionModel().select(date.getHours());
        cbConferenceMinute.getSelectionModel().select(date.getMinutes());
        txtConferenceHour.setText(conference.getConferenceTime()/60+"");
        txtConferenceMinute.setText(conference.getConferenceTime()%60+"");

        imageFile = new File(String.format("%s/%s", CreateConferenceBLL.IMAGE_FOLDER, conference.getImage()));
        if(imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            conferenceImage.setImage(image);
        }
    }
}