package view;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;

public class RegisterMenuController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleRegisterButtonAction() {
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String request = String.format("register %s %s %s %s", username, nickname, email, password);
        String response = App.getServerConnection().sendRequest(request);
        Tools.showAlert(response);
    }

    public void handleBack() {
        App.loadScene(Menu.LOGIN_MENU.getPath());
    }
}
