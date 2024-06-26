package view;

import controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import util.ServerConnection;

import static controller.AppController.loadScene;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String request = String.format("login %s %s", username, password);
        String response = AppController.getServerConnection().sendRequest(request);

        if (response.startsWith("Login successful.")) {
            String[] responseParts = response.split("\\|");
            if (responseParts.length > 1) {
                String serializedUser = responseParts[1];
                User currentUser = User.deserializeUser(serializedUser);
                User.setCurrentUser(currentUser);
                loadScene("/fxml/MainMenu.fxml");
            }
        } else {
            showAlert(response);
        }
    }


    @FXML
    public void handleForgotPasswordButtonAction(ActionEvent event) {
        loadScene("/fxml/forgot_password.fxml");
    }

    @FXML
    public void handleRegisterButtonAction(ActionEvent event) {
        loadScene("/fxml/RegistrationScreen.fxml");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
