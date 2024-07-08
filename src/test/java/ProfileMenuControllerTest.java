import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import util.DatabaseConnection;
import view.ProfileMenuController;

import java.util.List;

public class ProfileMenuControllerTest extends ApplicationTest {
    private ProfileMenuController controller;
    private DatabaseConnection mockDb;
    private User testUser;

    @BeforeEach
    public void setUp() throws Exception {
        controller = new ProfileMenuController();
        mockDb = mock(DatabaseConnection.class);
        testUser = new User("testUser2", "testNickname", "alikhalaj53@gmail.com", "password123");

        controller.usernameField = new TextField();
        controller.nicknameField = new TextField();
        controller.emailField = new TextField();
        controller.oldPasswordField = new PasswordField();
        controller.passwordField = new PasswordField();
        controller.confirmPasswordField = new PasswordField();
        controller.highScoreLabel = new Label();
        controller.rankLabel = new Label();
        controller.gamesPlayedLabel = new Label();
        controller.drawsLabel = new Label();
        controller.winsLabel = new Label();
        controller.lossesLabel = new Label();
        controller.gameHistoryField = new TextField();
        controller.isTwoFactorOn = new CheckBox();

        User.setCurrentUser(testUser);

        // Add test user to the database
        DatabaseConnection.saveUser(testUser);
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Remove test user from the database
        DatabaseConnection.deleteUser(testUser.getUsername());
    }

    @Test
    public void testInitialize() {
        // Test initialize method
        controller.initialize();
        assertEquals(testUser.getUsername(), controller.usernameField.getText());
        assertEquals(testUser.getNickname(), controller.nicknameField.getText());
        assertEquals(testUser.getEmail(), controller.emailField.getText());
        assertFalse(controller.isTwoFactorOn.isSelected());
        assertEquals(testUser.getHighScore(), Integer.parseInt(controller.highScoreLabel.getText()));
        assertEquals(testUser.getRank(), Integer.parseInt(controller.rankLabel.getText()));
        assertEquals("0", controller.gamesPlayedLabel.getText());
    }

    @Test
    public void testHandleUpdateButtonAction() throws Exception {
        controller.usernameField.setText("newUser2");
        controller.nicknameField.setText("newNickname");
        controller.emailField.setText("new@example.com");
        controller.passwordField.setText("newPassword123");
        controller.confirmPasswordField.setText("newPassword123");
        controller.oldPasswordField.setText("password123");
        controller.isTwoFactorOn.setSelected(false);
    }

    @Test
    public void testHandleBack() {
        controller.handleBack();
        // Verify the scene change (you can use a custom App class to capture the scene change)
    }
}
