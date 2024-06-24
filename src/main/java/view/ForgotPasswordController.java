package view;

import controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import util.DatabaseConnection;

import java.sql.SQLException;

public class ForgotPasswordController extends AppController {

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

        try {
            if (!DatabaseConnection.isUsernameTaken(username)) {
                showAlert("Error", "Invalid Username", "The entered username does not exist.");
                return;
            }

            String securityQuestion = DatabaseConnection.getSecurityQuestion(username);
            if (securityQuestion != null) {
                securityQuestionField.setText(securityQuestion);
                securityQuestionBox.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while checking the username. Please try again.");
        }
    }

    @FXML
    public void handleValidateAnswerButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String answer = securityAnswerField.getText();

        try {
            if (DatabaseConnection.validateSecurityAnswer(username, answer)) {
                newPasswordBox.setVisible(true);
            } else {
                showAlert("Error", "Invalid Answer", "The entered answer is incorrect.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while validating the answer. Please try again.");
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

        try {
            DatabaseConnection.updatePassword(username, newPassword);
            showAlert("Success", "Password Reset Successful", "Your password has been reset successfully.");
            // Navigate to the login screen
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while updating the password. Please try again.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
