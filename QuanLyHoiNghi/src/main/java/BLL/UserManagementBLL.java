package BLL;

import DAO.UserDAO;
import DTO.User;
import Utils.MyAlert;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class UserManagementBLL implements Initializable {
    private static final String[] userRole = {"Tất cả", "Admin", "User"};
    private static final String[] userStatus = {"Tất cả", "Hoạt động", "Bị chặn"};

    @FXML
    private TableView<User> tbUser;

    @FXML
    private TableColumn<User, String> tbcUserName;

    @FXML
    private TableColumn<User, String> tbcName;

    @FXML
    private TableColumn<User, String> tbcEmail;

    @FXML
    private TableColumn<User, String> tbcAdmin;

    @FXML
    private TableColumn<User, String> tbcBlock;

    @FXML
    private Button btnBlock;

    @FXML
    private Button btnAdmin;

    @FXML
    private Button btnSeeConference;


    @FXML
    private TextField lbSearch;

    @FXML
    private ComboBox<String> cbUserStatus;

    @FXML
    private ComboBox<String> cbUserRole;

    ObservableList<User> userObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDataTableView();
        setCellFactory();
        initComboBox();

        tbUser.setPlaceholder(new Label("Không tìm thấy User nào!!!"));

        tbUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(newValue.getIsBlocked()){
                    btnBlock.setId("unblock");
                    btnBlock.setText("Bỏ chặn");
                }
                else{
                    btnBlock.setId("block");
                    btnBlock.setText("Chặn");
                }
                if(newValue.getIsAdmin()){
                    btnAdmin.setId("makeUser");
                    btnAdmin.setText("Chỉ định user");
                }
                else{
                    btnAdmin.setId("makeAdmin");
                    btnAdmin.setText("Chỉ định admin");
                }
            }
        });

        btnBlock.setOnAction(event -> {
            User userSelected = tbUser.getSelectionModel().getSelectedItem();
            int posSelected = userObservableList.indexOf(userSelected);
            if (userSelected == null) {
                MyAlert.show(Alert.AlertType.ERROR, "Vui lòng chọn User!!!");
            }
            else{
                Optional<ButtonType> optional;
                if(btnBlock.getId().equals("block")){
                    optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Chặn User", "Bạn có thực sự muốn chặn",
                            String.format("%s - %s", userSelected.getUsername(), userSelected.getName()));
                    userSelected.setIsBlocked(true);
                }
                else{
                    optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Bỏ chặn User", "Bạn có thực sự muốn bỏ chặn",
                            String.format("%s - %s", userSelected.getUsername(), userSelected.getName()));
                    userSelected.setIsBlocked(false);
                }

                if(!optional.get().getButtonData().isCancelButton()){
                    boolean updateUser = UserDAO.updateUser(userSelected);
                    if(updateUser){
                        MyAlert.show(Alert.AlertType.INFORMATION, "Thành công");
                        userObservableList.set(posSelected, userSelected);
                        tbUser.refresh();

                    }
                }
            }
        });

        btnAdmin.setOnAction(event -> {
            User userSelected = tbUser.getSelectionModel().getSelectedItem();
            int posSelected = userObservableList.indexOf(userSelected);
            if (userSelected == null) {
                MyAlert.show(Alert.AlertType.ERROR, "Vui lòng chọn User!!!");
            }
            else {
                Optional<ButtonType> optional;
                if (btnAdmin.getId().equals("makeAdmin")) {
                    optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Chỉ định làm admin", "Bạn có thực sự chỉ định làm admin",
                            String.format("%s - %s", userSelected.getUsername(), userSelected.getName()));
                    userSelected.setIsAdmin(true);
                } else {
                    optional = MyAlert.show(Alert.AlertType.CONFIRMATION, "Chỉ định làm user", "Bạn có thực sự muốn chỉ định làm user",
                            String.format("%s - %s", userSelected.getUsername(), userSelected.getName()));
                    userSelected.setIsAdmin(false);
                }
                if (!optional.get().getButtonData().isCancelButton()) {
                    boolean updateUser = UserDAO.updateUser(userSelected);
                    if (updateUser) {
                        MyAlert.show(Alert.AlertType.INFORMATION, "Thành công");
                        userObservableList.set(posSelected, userSelected);
                        tbUser.refresh();

                    }
                }
            }
        });


        lbSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                userObservableList = FXCollections.observableList(UserDAO.getUsers());
            } else{
                List<User> userList = UserDAO.findUserByKey(newValue);
                userObservableList = FXCollections.observableList(userList);
            }
            filterUser();
        });

        cbUserRole.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> filterUser());

        cbUserStatus.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> filterUser());


        btnSeeConference.setOnAction(event -> {
            System.out.println("See all conference of user");
            MyAlert.show(Alert.AlertType.INFORMATION, "See all conference of user");
        });
    }

    private void filterUser(){
        List<User> userList = new ArrayList<>();
        int role = cbUserRole.getSelectionModel().getSelectedIndex();
        int status = cbUserStatus.getSelectionModel().getSelectedIndex();
        for (User user : userObservableList) {
            if (role == 0 && status == 0) {
                userList.add(user);
            } else if (role == 0 && status == 1) {
                if (!user.getIsBlocked()) {
                    userList.add(user);
                }
            } else if (role == 0 && status == 2) {
                if (user.getIsBlocked()) {
                    userList.add(user);
                }
            } else if (role == 1 && status == 0) {
                if (user.getIsAdmin()) {
                    userList.add(user);
                }
            } else if (role == 2 && status == 0) {
                if (!user.getIsAdmin()) {
                    userList.add(user);
                }
            } else {
                int uAdmin = user.getIsAdmin() ? 1 : 2;
                int uBlock = user.getIsBlocked() ? 2 : 1;
                if (uAdmin == role && uBlock == status) {
                    userList.add(user);
                }
            }
        }
        tbUser.setItems(FXCollections.observableList(userList));
    }

    private void initComboBox() {
        cbUserStatus.setItems(FXCollections.observableList(Arrays.asList(userStatus)));
        cbUserRole.setItems(FXCollections.observableList(Arrays.asList(userRole)));
        cbUserRole.getSelectionModel().select(0);
        cbUserStatus.getSelectionModel().select(0);
    }

    private void loadDataTableView() {
        userObservableList = FXCollections.observableList(UserDAO.getUsers());
        tbUser.setItems(userObservableList);
    }

    private void setCellFactory() {
        tbcUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbcAdmin.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {
            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                if(param.getValue().getIsAdmin()){
                    return userRole[1];
                }
                return userRole[2];
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        tbcBlock.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }

            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                if(param.getValue().getIsBlocked()){
                    return userStatus[2];
                }
                return userStatus[1];
            }
        });
    }

}