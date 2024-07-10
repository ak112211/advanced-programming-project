package view;

import controller.VerificationCodeController;
import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Result;
import model.User;

import java.sql.SQLException;

import static view.Tools.clearUserSession;
import static view.Tools.sendVerificationCode;

public class VerificationCodeMenuController {

    @FXML
    private TextField codeField;

    @FXML
    private void handleVerifyButtonAction() {
        String code = codeField.getText();

        Result result = VerificationCodeController.verify(code);
        Tools.showAlert(result);

        if (result.isSuccess()) {
            Tools.loadScene(Menu.MAIN_MENU);
        }
    }

    @FXML
    private void handleBack() {
        clearUserSession();
        User.setCurrentUser(null);
        Tools.loadScene(Menu.LOGIN_MENU);
    }

    @FXML
    private void handleResend() throws SQLException {
        sendVerificationCode(User.getCurrentUser());
    }
}
