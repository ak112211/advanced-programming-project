package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ChooseNameMenuController {

    @FXML
    private TextField usernameField;

    @FXML
    private void handleSubmitButtonAction() throws SQLException {
        ChooseDeckMenuController.name = usernameField.getText();
        Tools.loadScene(Menu.DECK_MENU);
    }

    @FXML
    private void handleBack() {
        Tools.loadScene(Menu.MAIN_MENU);
    }

}
