package view;

import controller.AppController;
import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;

import static view.Tools.*;

public class VerificationCodeController {

    @FXML
    private TextField codeField;

    @FXML
    private void handleVerifyButtonAction() {
        String code = codeField.getText();

        try {
            if (DatabaseConnection.verifyCode(User.getCurrentUser().getUsername(), code)) {
                Tools.showAlert("Success", "Verification Successful", "Your account has been successfully verified.");
                App.getServerConnection().sendMessage("login:" + User.getCurrentUser().getUsername());
                AppController.loadScene(Menu.MAIN_MENU);
                saveUserSession(User.getCurrentUser());  // Save session
            } else {
                Tools.showAlert("Error", "Verification Failed", "Invalid or expired verification code.");
            }
        } catch (SQLException e) {
            Tools.showAlert("Error", "Verification Failed", "An error occurred while verifying the code: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        clearUserSession();
        User.setCurrentUser(null);
        AppController.loadScene(Menu.LOGIN_MENU);
    }

    @FXML
    private void handleResend() throws SQLException {
        sendVerificationCode(User.getCurrentUser());
    }
}
