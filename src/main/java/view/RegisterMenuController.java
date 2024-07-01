package view;

import enums.Menu;
import enums.SecurityQuestion;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;
import util.EmailSender;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class RegisterMenuController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private ComboBox<SecurityQuestion> securityQuestionComboBox;
    @FXML
    private TextField securityAnswerField;
    @FXML
    private TextField securityAnswerConfirmField;

    @FXML
    private void initialize() {
        passwordField.setVisible(true);
        confirmPasswordField.setVisible(true);
        securityQuestionComboBox.getItems().setAll(SecurityQuestion.values());
    }

    @FXML
    private void handleRegisterButtonAction() {
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        SecurityQuestion securityQuestion = securityQuestionComboBox.getValue();
        String securityAnswer = securityAnswerField.getText().trim();
        String securityAnswerConfirm = securityAnswerConfirmField.getText().trim();

        try {
            if (DatabaseConnection.getUser(username) != null) {
                String newUsername = Tools.suggestNewUsername(username);
                Tools.showAlert("Error", "Username Taken", "Username is already taken. Suggested username: " + newUsername);
            } else if (!Tools.isValidUsername(username)) {
                Tools.showAlert("Error", "Invalid Username", "Invalid username. Only letters, numbers, and '-' are allowed.");
            } else if (!Tools.isValidEmail(email)) {
                Tools.showAlert("Error", "Invalid Email", "Invalid email address.");
            } else if (!Tools.isValidPassword(password) && !password.equals("random")) {
                Tools.showAlert("Error", "Weak Password", "Invalid password. Password must be at least 8 characters long, include uppercase, lowercase, numbers, and special characters.");
            } else if (!password.equals(confirmPassword)) {
                Tools.showAlert("Error", "Password Mismatch", "Password and confirm password do not match.");
            } else if (securityQuestion == null || securityAnswer.isEmpty() || !securityAnswer.equals(securityAnswerConfirm)) {
                Tools.showAlert("Error", "Security Question", "Security question must be selected and answers must match.");
            } else {
                User user = new User(username, nickname, email, password);
                user.setSecurityQuestion(securityQuestion.getQuestion());
                user.setAnswer(securityAnswer);
                user.setVerified(false);
                DatabaseConnection.saveUser(user);

                // Generate verification code
                String verificationCode = generateVerificationCode();
                LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10); // Code valid for 10 minutes
                DatabaseConnection.insertVerificationCode(username, verificationCode, expirationTime);

                // Send verification email
                EmailSender.sendVerificationEmail(email, verificationCode);

                Tools.showAlert("Success", "Registration Successful", "User registered successfully. Please check your email for the verification code.");
                App.loadScene(Menu.VERIFY_MENU.getPath()); // Load the verification code scene
            }
        } catch (SQLException e) {
            Tools.showAlert("Error", "Registration Failed", "Error registering user: " + e.getMessage());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @FXML
    private void handleBack() {
        App.loadScene(Menu.LOGIN_MENU.getPath());
    }

    @FXML
    private void handleRandomPassword() {
        String password = Tools.generateRandomPassword();
        passwordField.setText(password);
        confirmPasswordField.setText(password);
    }
}
