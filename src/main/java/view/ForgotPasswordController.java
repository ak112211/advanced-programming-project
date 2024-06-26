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
            Tools.showAlert("Error", "Invalid Username", "The entered username does not exist.");
            return;
        }

        response = App.getServerConnection().sendRequest("getSecurityQuestion " + username);
        if (response != null && !response.isEmpty()) {
            securityQuestionField.setText(response);
            securityQuestionBox.setVisible(true);
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while checking the username. Please try again.");
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
            Tools.showAlert("Error", "Invalid Answer", "The entered answer is incorrect.");
        }
    }

    @FXML
    public void handleSetPasswordButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (!newPassword.equals(confirmNewPassword)) {
            Tools.showAlert("Error", "Password Mismatch", "Password and its confirmation do not match.");
            return;
        }

        String response = App.getServerConnection().sendRequest("updatePassword " + username + " " + newPassword);
        if ("success".equals(response)) {
            Tools.showAlert("Success", "Password Reset Successful", "Your password has been reset successfully.");
            App.loadScene("/fxml/LoginScreen.fxml");
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while updating the password. Please try again.");
        }
    }


    public void handleBack(ActionEvent actionEvent) {
        App.loadScene("/fxml/LoginScreen.fxml");
    }

}
