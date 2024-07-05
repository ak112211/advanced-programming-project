package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;

import static view.Tools.saveUserSession;
import static view.Tools.sendVerificationCode;

public class LoginMenuController {

    @FXML
    private ImageView backgroundImageView;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField textField;
    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private void initialize() {
        // Bind the visibility of the textField to the showPasswordCheckBox
        textField.managedProperty().bind(showPasswordCheckBox.selectedProperty());
        textField.visibleProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordField.managedProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordField.visibleProperty().bind(showPasswordCheckBox.selectedProperty().not());
        backgroundImageView.setImage(Tools.getImage("/gwentImages/img/maxresdefault.jpg"));

        // Bind the text properties to keep them synchronized
        textField.textProperty().bindBidirectional(passwordField.textProperty());

    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user;
        try {
            if ((user = DatabaseConnection.getUser(username)) == null) {
                Tools.showAlert("Invalid username.");
            } else if (DatabaseConnection.checkPassword(username, password)) {
                User.setCurrentUser(user);
                if (!user.isVerified()) {
                    sendVerificationCode(User.getCurrentUser());
                    Tools.showAlert("Account not verified. Redirecting to verification screen.");
                    App.loadScene(Menu.VERIFY_MENU.getPath());
                } else {
                    saveUserSession(user);  // Save session
                    if (user.isTwoFactorOn()) {
                        sendVerificationCode(User.getCurrentUser());
                        Tools.showAlert("Code needed", "Login Successful", "User logged in successfully. Please check your email for the verification code.");
                        App.loadScene(Menu.VERIFY_MENU.getPath());
                    } else {
                        App.loadScene(Menu.MAIN_MENU.getPath());
                        App.getServerConnection().sendMessage("login:" + User.getCurrentUser().getUsername());
                        Tools.showAlert("Login successful. Welcome " + user.getNickname() + "!");
                    }
                }
            } else {
                Tools.showAlert("Invalid password.");
            }
        } catch (SQLException e) {
            Tools.showAlert("Error during login: " + e.getMessage());
        }
    }

    @FXML
    private void handleForgotPasswordButtonAction() {
        App.loadScene(Menu.FORGET_PASSWORD_MENU.getPath());
    }

    @FXML
    private void handleRegisterButtonAction() {
        App.loadScene(Menu.REGISTER_MENU.getPath());
    }


}
