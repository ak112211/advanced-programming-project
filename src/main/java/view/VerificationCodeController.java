package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import model.App;
import util.DatabaseConnection;

import java.sql.SQLException;

public class VerificationCodeController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField codeField;
    @FXML
    private Button verifyButton;

    @FXML
    private void handleVerifyButtonAction() {
        String username = usernameField.getText();
        String code = codeField.getText();

        try {
            if (DatabaseConnection.verifyCode(username, code)) {
                Tools.showAlert("Success", "Verification Successful", "Your account has been successfully verified.");
                App.loadScene(Menu.LOGIN_MENU.getPath());
            } else {
                Tools.showAlert("Error", "Verification Failed", "Invalid or expired verification code.");
            }
        } catch (SQLException e) {
            Tools.showAlert("Error", "Verification Failed", "An error occurred while verifying the code: " + e.getMessage());
        }
    }
}
