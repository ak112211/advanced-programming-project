package view;

import controller.RegisterController;
import enums.Menu;
import enums.SecurityQuestion;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Result;

public class RegisterMenuController {

    @FXML
    public TextField usernameField;
    @FXML
    public TextField nicknameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField confirmPasswordField;
    @FXML
    public ComboBox<SecurityQuestion> securityQuestionComboBox;
    @FXML
    public TextField securityAnswerField;
    @FXML
    public TextField securityAnswerConfirmField;

    @FXML
    public void initialize() {
        passwordField.setVisible(true);
        confirmPasswordField.setVisible(true);
        securityQuestionComboBox.getItems().setAll(SecurityQuestion.values());
    }

    @FXML
    public void handleRegisterButtonAction() {
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        SecurityQuestion securityQuestion = securityQuestionComboBox.getValue();
        String securityAnswer = securityAnswerField.getText().trim();
        String securityAnswerConfirm = securityAnswerConfirmField.getText().trim();
        Result result = RegisterController.register(username, nickname, email, password, confirmPassword,
                securityQuestion, securityAnswer, securityAnswerConfirm);

        Tools.showAlert(result);
        if (result.isSuccess()) {
            Tools.loadScene(Menu.VERIFY_MENU); // Load the verification code scene
        }
    }

    @FXML
    public void handleBack() {
        Tools.loadScene(Menu.LOGIN_MENU);
    }

    @FXML
    public void handleRandomPassword() {
        String password = Tools.generateRandomPassword();
        passwordField.setText(password);
        confirmPasswordField.setText(password);
    }
}
