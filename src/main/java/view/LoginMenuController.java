package view;

import controller.LoginController;
import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Result;

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

        Result result = LoginController.login(username, password);
        Tools.showAlert(result);

        if (result.getHeader().equals("Not verified")) {
            Tools.loadScene(Menu.VERIFY_MENU);
        } else if (result.getHeader().equals("Code needed for 2FA")) {
            Tools.loadScene(Menu.VERIFY_MENU);
        } else if (result.isSuccess()) {
            Tools.loadScene(Menu.MAIN_MENU);
        }
    }

    @FXML
    private void handleForgotPasswordButtonAction() {
        Tools.loadScene(Menu.FORGET_PASSWORD_MENU);
    }

    @FXML
    private void handleRegisterButtonAction() {
        Tools.loadScene(Menu.REGISTER_MENU);
    }


}
