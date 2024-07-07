import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import util.DatabaseConnection;
import view.ForgotPasswordMenuController;

public class ForgotPasswordMenuControllerTest extends ApplicationTest {

    private ForgotPasswordMenuController controller;

    @Start
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ForgotPasswordMenu.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testHandleSubmitButtonAction() throws Exception {
        DatabaseConnection mockDb = mock(DatabaseConnection.class);
        when(mockDb.getUser("testUser2")).thenReturn(new User("testUser2", "testNickname", "alikhalaj53@gmail.com", "password123"));
        when(mockDb.getSecurityQuestion("testUser2")).thenReturn("What is your pet's name?");

        controller.usernameField.setText("testUser2");
        controller.handleSubmitButtonAction();

        verify(mockDb).getSecurityQuestion("testUser2");
        assertEquals("What is your pet's name?", controller.securityQuestionField.getText());
    }

    @Test
    public void testHandleValidateAnswerButtonAction() throws Exception {
        DatabaseConnection mockDb = mock(DatabaseConnection.class);
        when(mockDb.validateSecurityAnswer("testUser2", "correctAnswer")).thenReturn(true);

        controller.usernameField.setText("testUser2");
        controller.securityAnswerField.setText("correctAnswer");
        controller.handleValidateAnswerButtonAction();

        verify(mockDb).validateSecurityAnswer("testUser2", "correctAnswer");
        assertTrue(controller.newPasswordBox.isVisible());
    }

    @Test
    public void testHandleSetPasswordButtonAction() throws Exception {
        DatabaseConnection mockDb = mock(DatabaseConnection.class);
        when(mockDb.updatePassword("testUser2", "newPassword")).thenReturn(true);

        controller.usernameField.setText("testUser2");
        controller.newPasswordField.setText("newPassword");
        controller.confirmNewPasswordField.setText("newPassword");
        controller.handleSetPasswordButtonAction();

        verify(mockDb).updatePassword("testUser2", "newPassword");
        // Assuming App.getCurrentScene() and other related methods are static and setup for testing
    }
}
