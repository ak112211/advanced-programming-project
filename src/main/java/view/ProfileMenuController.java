package view;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.App;
import model.User;

public class ProfileMenuController {

    @FXML
    private TextField currentUsernameField;
    @FXML
    private TextField newUsernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;

    @FXML
    private void initialize() {
        User currentUser = User.getCurrentUser();
        currentUsernameField.setText(currentUser.getUsername());
        nicknameField.setText(currentUser.getNickname());
        emailField.setText(currentUser.getEmail());
    }

    @FXML
    private void handleUpdateButtonAction() {
        String currentUsername = currentUsernameField.getText();
        String newUsername = newUsernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String request = String.format("updateProfile %s %s %s %s", currentUsername, newUsername, nickname, email);
        String response = App.getServerConnection().sendRequest(request);
        Tools.showAlert(response);
    }

    public void handleBack() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }

}
