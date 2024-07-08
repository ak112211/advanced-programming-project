import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import util.DatabaseConnection;
import enums.SecurityQuestion;
import view.RegisterMenuController;
import view.Tools;

public class RegisterMenuControllerTest extends ApplicationTest {
    private RegisterMenuController controller;

    @BeforeEach
    public void setUp() throws Exception {
        controller = new RegisterMenuController();

        controller.usernameField = new TextField();
        controller.nicknameField = new TextField();
        controller.emailField = new TextField();
        controller.passwordField = new TextField();
        controller.confirmPasswordField = new TextField();
        controller.securityQuestionComboBox = new ComboBox<>();
        controller.securityAnswerField = new TextField();
        controller.securityAnswerConfirmField = new TextField();

        controller.initialize();
    }

    @Test
    public void testHandleRegisterButtonAction_Successful() throws Exception {
        controller.usernameField.setText("newUser");
        controller.nicknameField.setText("newNickname");
        controller.emailField.setText("alikhalaj53@gmail.com");
        controller.passwordField.setText("Password123!");
        controller.confirmPasswordField.setText("Password123!");
        controller.securityQuestionComboBox.setValue(SecurityQuestion.FAVORITE_BOOK);
        controller.securityAnswerField.setText("Dog");
        controller.securityAnswerConfirmField.setText("Dog");
        User user = new User(controller.usernameField.getText(), controller.nicknameField.getText(), controller.emailField.getText(), controller.passwordField.getText());

        assertNull(DatabaseConnection.getUser("newUser"));
        DatabaseConnection.saveUser(user);
        assertNotNull(DatabaseConnection.getUser("newUser"));
        DatabaseConnection.deleteUser(user.getUsername());
    }


    @Test
    public void testHandleRegisterButtonAction_InvalidEmail() throws Exception {
        controller.usernameField.setText("newUser");
        controller.emailField.setText("invalidEmail");
        controller.passwordField.setText("Password123!");
        controller.confirmPasswordField.setText("Password123!");
        assertFalse(Tools.isValidEmail(controller.emailField.getText()));
    }

    @Test
    public void testHandleRegisterLeagueItems() throws Exception {
        controller.usernameField.setText("bbkk");
        controller.emailField.setText("alikhalaj53@gmail.com");
        controller.passwordField.setText("1212");
        controller.confirmPasswordField.setText("1212");
        controller.securityQuestionComboBox.setValue(SecurityQuestion.FIRST_SCHOOL);
        controller.securityAnswerField.setText("aa");
        controller.securityAnswerConfirmField.setText("aa");
        User user = new User(controller.usernameField.getText(), controller.nicknameField.getText(), controller.emailField.getText(), controller.passwordField.getText());
        user.setSecurityQuestion(controller.securityQuestionComboBox.getValue().getQuestion());
        user.setAnswer(controller.securityAnswerField.getText());
        user.setVerified(false);
        user.setTwoFactorOn(false);
        User.setCurrentUser(user);
        DatabaseConnection.saveUser(user);
        controller.usernameField.setText("bbk2");
        user = new User(controller.usernameField.getText(), controller.nicknameField.getText(), controller.emailField.getText(), controller.passwordField.getText());
        user.setSecurityQuestion(controller.securityQuestionComboBox.getValue().getQuestion());
        user.setAnswer(controller.securityAnswerField.getText());
        user.setVerified(false);
        user.setTwoFactorOn(false);
        User.setCurrentUser(user);
        DatabaseConnection.saveUser(user);
        controller.usernameField.setText("ab12");
        user = new User(controller.usernameField.getText(), controller.nicknameField.getText(), controller.emailField.getText(), controller.passwordField.getText());
        user.setSecurityQuestion(controller.securityQuestionComboBox.getValue().getQuestion());
        user.setAnswer(controller.securityAnswerField.getText());
        user.setVerified(false);
        user.setTwoFactorOn(false);
        User.setCurrentUser(user);
        DatabaseConnection.saveUser(user);
        controller.usernameField.setText("bkh2");
        user = new User(controller.usernameField.getText(), controller.nicknameField.getText(), controller.emailField.getText(), controller.passwordField.getText());
        user.setSecurityQuestion(controller.securityQuestionComboBox.getValue().getQuestion());
        user.setAnswer(controller.securityAnswerField.getText());
        user.setVerified(false);
        user.setTwoFactorOn(false);
        User.setCurrentUser(user);
        DatabaseConnection.saveUser(user);
        controller.usernameField.setText("ab22");
        user = new User(controller.usernameField.getText(), controller.nicknameField.getText(), controller.emailField.getText(), controller.passwordField.getText());
        user.setSecurityQuestion(controller.securityQuestionComboBox.getValue().getQuestion());
        user.setAnswer(controller.securityAnswerField.getText());
        user.setVerified(false);
        user.setTwoFactorOn(false);
        User.setCurrentUser(user);
        DatabaseConnection.saveUser(user);
    }

    @Test
    public void testHandleRegisterButtonAction_PasswordMismatch() throws Exception {
        controller.usernameField.setText("newUser");
        controller.emailField.setText("new@example.com");
        controller.passwordField.setText("Password123!");
        controller.confirmPasswordField.setText("Password1234!");
        assertEquals(controller.passwordField.getText(), controller.confirmPasswordField.getText());
    }

    @Test
    public void testHandleRegisterButtonAction_SecurityQuestionMismatch() throws Exception {
        controller.usernameField.setText("newUser");
        controller.emailField.setText("new@example.com");
        controller.passwordField.setText("Password123!");
        controller.confirmPasswordField.setText("Password123!");
        controller.securityQuestionComboBox.setValue(SecurityQuestion.FIRST_SCHOOL);
        controller.securityAnswerField.setText("Dog");
        controller.securityAnswerConfirmField.setText("Cat");
        assertEquals(controller.securityAnswerField.getText(), controller.securityAnswerConfirmField.getText());
    }


    @Test
    public void testHandleRandomPassword() {
        controller.handleRandomPassword();
        assertEquals(controller.passwordField.getText(), controller.confirmPasswordField.getText());
        assertTrue(Tools.isValidPassword(controller.passwordField.getText()));
    }

}
