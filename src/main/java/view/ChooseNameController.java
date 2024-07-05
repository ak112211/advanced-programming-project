package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.App;
import util.DatabaseConnection;

import java.sql.SQLException;

public class ChooseNameController {

    @FXML
    private TextField usernameField;

    @FXML
    private void handleSubmitButtonAction() throws SQLException {
        ChooseDeckMenuController.name = usernameField.getText();
        App.loadScene(Menu.DECK_MENU.getPath());
    }

    @FXML
    private void handleBack() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }

}
