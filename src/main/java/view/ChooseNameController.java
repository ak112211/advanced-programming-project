package view;

import controller.AppController;
import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.App;

import java.sql.SQLException;

public class ChooseNameController {

    @FXML
    private TextField usernameField;

    @FXML
    private void handleSubmitButtonAction() throws SQLException {
        ChooseDeckMenuController.name = usernameField.getText();
        AppController.loadScene(Menu.DECK_MENU.getPath());
    }

    @FXML
    private void handleBack() {
        AppController.loadScene(Menu.MAIN_MENU.getPath());
    }

}
