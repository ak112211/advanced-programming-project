package view;

import enums.Menu;
import enums.SecurityQuestion;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    private CheckBox showPasswordCheckBox;
    @FXML
    private ComboBox<SecurityQuestion> securityQuestionComboBox;
    @FXML
    private TextField securityAnswerField;
    @FXML
    private TextField securityAnswerConfirmField;

    @FXML
    private void initialize() {
        passwordField.setVisible(false);
        confirmPasswordField.setVisible(false);
        showPasswordCheckBox.setSelected(false);
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
            if (DatabaseConnection.isUsernameTaken(username)) {
                String newUsername = suggestNewUsername(username);
                Tools.showAlert("Error", "Username Taken", "Username is already taken. Suggested username: " + newUsername);
            } else if (!isValidUsername(username)) {
                Tools.showAlert("Error", "Invalid Username", "Invalid username. Only letters, numbers, and '-' are allowed.");
            } else if (!isValidEmail(email)) {
                Tools.showAlert("Error", "Invalid Email", "Invalid email address.");
            } else if (!isValidPassword(password) && !password.equals("random")) {
                Tools.showAlert("Error", "Weak Password", "Invalid password. Password must be at least 8 characters long, include uppercase, lowercase, numbers, and special characters.");
            } else if (!password.equals(confirmPassword)) {
                Tools.showAlert("Error", "Password Mismatch", "Password and confirm password do not match.");
            } else if (securityQuestion == null || securityAnswer.isEmpty() || !securityAnswer.equals(securityAnswerConfirm)) {
                Tools.showAlert("Error", "Security Question", "Security question must be selected and answers must match.");
            } else {

                User user = new User(username, nickname, email, password);
                user.setQuestionNumber(securityQuestion.getNumber());
                user.setAnswer(securityAnswer);
                DatabaseConnection.saveUser(user);
                Tools.showAlert("Success", "Registration Successful", "User registered successfully.");
            }
        } catch (SQLException e) {
            Tools.showAlert("Error", "Registration Failed", "Error registering user: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        App.loadScene(Menu.LOGIN_MENU.getPath());
    }

    @FXML
    private void handleRandomPassword() {
        String password = generateRandomPassword();
        passwordField.setText(password);
        confirmPasswordField.setText(password);
    }

    @FXML
    private void handleShowPassword() {
        passwordField.setVisible(showPasswordCheckBox.isSelected());
        confirmPasswordField.setVisible(showPasswordCheckBox.isSelected());
    }

    private String suggestNewUsername(String username) {
        return username + new Random().nextInt(1000);
    }

    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) hasUpper = true;
            else if (Character.isLowerCase(character)) hasLower = true;
            else if (Character.isDigit(character)) hasDigit = true;
            else if (!Character.isLetterOrDigit(character)) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private String generateRandomPassword() {
        StringBuilder password = getInitialPassword();
        char[] passwordArray = password.toString().toCharArray();
        List<Character> passwordList = new ArrayList<>();
        for (char character : passwordArray) {
            passwordList.add(character);
        }
        Collections.shuffle(passwordList);
        password = new StringBuilder();
        for (char character : passwordList) {
            password.append(character);
        }
        return password.toString();
    }

    private static StringBuilder getInitialPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+<>?";
        String allChars = upper + lower + digits + special;
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        for (int i = 4; i < random.nextInt(12, 15); i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return password;
    }

}
