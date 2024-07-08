import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.testfx.framework.junit5.ApplicationTest;
import view.MainMenuController;
import view.RegisterMenuController;

import java.util.ArrayList;

public class MainMenuControllerTest extends ApplicationTest {
    private MainMenuController controller;

    @BeforeEach
    public void setUp() throws Exception {
        controller.usernameField = new Label();
        controller.backgroundImageView = new ImageView();
        controller.savedGamesListView = new ListView<>();
        controller.savedGames = new ArrayList<>();

        controller.initialize();
    }

    @Test
    public void testHandleContinueGameButtonAction_Fail() throws Exception {
        assertNull(controller.savedGamesListView.getSelectionModel().getSelectedItems());
    }
}
