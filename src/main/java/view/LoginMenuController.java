package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.User;

public class LoginMenuController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String request = String.format("login %s %s", username, password);
        String response = App.getServerConnection().sendRequest(request);

        if (response.startsWith("Login successful.")) {
            String[] responseParts = response.split("\\|");
            if (responseParts.length > 1) {
                String serializedUser = responseParts[1];
                User currentUser = User.deserializeUser(serializedUser);
                User.setCurrentUser(currentUser);
                App.loadScene(Menu.MAIN_MENU.getPath());
            }
        } else {
            Tools.showAlert(response);
        }
    }


    @FXML
    public void handleForgotPasswordButtonAction() {
        App.loadScene(Menu.FORGET_PASSWORD_MENU.getPath());
    }

    @FXML
    public void handleRegisterButtonAction() {
        App.loadScene(Menu.REGISTER_MENU.getPath());
    }


}
