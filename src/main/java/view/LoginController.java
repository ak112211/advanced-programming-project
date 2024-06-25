package view;

import controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        showAlert(response);
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
