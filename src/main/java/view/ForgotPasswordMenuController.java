package view;

import controller.AppController;
import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.App;
import util.DatabaseConnection;

import java.sql.SQLException;

public class ForgotPasswordMenuController {

    @FXML
    public TextField usernameField;
    @FXML
    private VBox securityQuestionBox;
    @FXML
    public TextField securityQuestionField;
    @FXML
    public TextField securityAnswerField;
    @FXML
    public VBox newPasswordBox;
    @FXML
    public PasswordField newPasswordField;
    @FXML
    public PasswordField confirmNewPasswordField;

    @FXML
    public void handleSubmitButtonAction() throws SQLException {
        String username = usernameField.getText();

        // Check if the username exists using DatabaseUtils.isUserTaken method
        if (DatabaseConnection.getUser(username) == null) {
            Tools.showAlert("Error", "Invalid Username", "The entered username does not exist.");
            return;
        }

        // Retrieve the security question using DatabaseUtils.getSecurityQuestion method
        String securityQuestion = DatabaseConnection.getSecurityQuestion(username);
        if (securityQuestion != null && !securityQuestion.isEmpty()) {
            securityQuestionField.setText(securityQuestion);
            securityQuestionBox.setVisible(true);
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while checking the username. Please try again.");
        }
    }

    @FXML
    public void handleValidateAnswerButtonAction() throws SQLException {
        String username = usernameField.getText();
        String answer = securityAnswerField.getText();

        boolean isValidAnswer = DatabaseConnection.validateSecurityAnswer(username, answer);

        if (isValidAnswer) {
            newPasswordBox.setVisible(true);
        } else {
            Tools.showAlert("Error", "Invalid Answer", "The entered answer is incorrect.");
        }
    }

    @FXML
    public void handleSetPasswordButtonAction() {
        String username = usernameField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (!newPassword.equals(confirmNewPassword)) {
            Tools.showAlert("Error", "Password Mismatch", "Password and its confirmation do not match.");
            return;
        }

        // Update the password using DatabaseUtils method
        boolean isPasswordUpdated = DatabaseConnection.updatePassword(username, newPassword);

        if (isPasswordUpdated) {
            Tools.showAlert("Success", "Password Reset Successful", "Your password has been reset successfully.");
            AppController.loadScene(Menu.LOGIN_MENU);
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while updating the password. Please try again.");
        }
    }

    @FXML
    private void handleBack() {
        AppController.loadScene(Menu.LOGIN_MENU);
    }

}
