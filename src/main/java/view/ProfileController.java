package view;

import controller.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ProfileController {

    @FXML
    private TextField currentUsernameField;
    @FXML
    private TextField newUsernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;

    @FXML
    private void handleUpdateButtonAction() {
        String currentUsername = currentUsernameField.getText();
        String newUsername = newUsernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String request = String.format("updateProfile %s %s %s %s", currentUsername, newUsername, nickname, email);
        String response = AppController.getServerConnection().sendRequest(request);
        showAlert(response);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
