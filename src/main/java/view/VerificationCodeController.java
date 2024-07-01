package view;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import model.App;
import model.Game;
import model.User;
import util.DatabaseConnection;
import util.EmailSender;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static view.Tools.generateVerificationCode;
import static view.Tools.sendVerificationCode;

public class VerificationCodeController {

    @FXML
    private TextField codeField;
    @FXML
    private Button verifyButton;
    @FXML
    private Button resendButton;

    @FXML
    private void handleVerifyButtonAction() {
        String code = codeField.getText();

        try {
            if (DatabaseConnection.verifyCode(User.getCurrentUser().getUsername(), code)) {
                Tools.showAlert("Success", "Verification Successful", "Your account has been successfully verified.");
                App.getServerConnection().setLogin(User.getCurrentUser().getUsername());
                App.loadScene(Menu.MAIN_MENU.getPath());
            } else {
                Tools.showAlert("Error", "Verification Failed", "Invalid or expired verification code.");
            }
        } catch (SQLException e) {
            Tools.showAlert("Error", "Verification Failed", "An error occurred while verifying the code: " + e.getMessage());
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        App.setIsLoggedIn(false);
        User.setCurrentUser(null);
        Game.setCurrentGame(null);
        App.loadScene(Menu.LOGIN_MENU.getPath());
    }

    @FXML
    public void handleResend(ActionEvent actionEvent) throws SQLException {
        sendVerificationCode(User.getCurrentUser());
    }
}
