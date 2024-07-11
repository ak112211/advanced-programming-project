import controller.RegisterController;
import enums.SecurityQuestion;
import model.Result;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class RegisterControllerTest extends ApplicationTest {
    String username, nickname, email, password, confirmPassword, answer, confirmAnswer;
    SecurityQuestion securityQuestion;

    @Test
    public void usernameTaken() throws Exception {

    }

    private Result getResult() {
        return RegisterController.register(username, nickname, email, password, confirmPassword,
                securityQuestion, answer, confirmAnswer);
    }
}
