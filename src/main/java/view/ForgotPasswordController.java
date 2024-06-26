package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.App;

public class ForgotPasswordController {

    @FXML
    private TextField usernameField;

    @FXML
    private VBox securityQuestionBox;

    @FXML
    private TextField securityQuestionField;

    @FXML
    private TextField securityAnswerField;

    @FXML
    private VBox newPasswordBox;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmNewPasswordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button validateAnswerButton;

    @FXML
    private Button setPasswordButton;

    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String response = App.getServerConnection().sendRequest("isUsernameTaken " + username);

        if ("false".equals(response)) {
            showAlert("Error", "Invalid Username", "The entered username does not exist.");
            return;
        }

        response = App.getServerConnection().sendRequest("getSecurityQuestion " + username);
        if (response != null && !response.isEmpty()) {
            securityQuestionField.setText(response);
            securityQuestionBox.setVisible(true);
        } else {
            showAlert("Error", "Database Error", "An error occurred while checking the username. Please try again.");
        }
    }

    @FXML
    public void handleValidateAnswerButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String answer = securityAnswerField.getText();
        String response = App.getServerConnection().sendRequest("validateSecurityAnswer " + username + " " + answer);

        if ("true".equals(response)) {
            newPasswordBox.setVisible(true);
        } else {
            showAlert("Error", "Invalid Answer", "The entered answer is incorrect.");
        }
    }

    @FXML
    public void handleSetPasswordButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("Error", "Password Mismatch", "Password and its confirmation do not match.");
            return;
        }

        String response = App.getServerConnection().sendRequest("updatePassword " + username + " " + newPassword);
        if ("success".equals(response)) {
            showAlert("Success", "Password Reset Successful", "Your password has been reset successfully.");
            App.loadScene("/fxml/LoginScreen.fxml");
        } else {
            showAlert("Error", "Database Error", "An error occurred while updating the password. Please try again.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleBack(ActionEvent actionEvent) {
        App.loadScene("/fxml/LoginScreen.fxml");
    }

}
