package controller;

import model.App;
import model.Result;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;

import static view.Tools.saveUserSession;
import static view.Tools.sendVerificationCode;

public class LoginController {
    public static Result login(String username, String password) {
        try {
            User user;
            if ((user = DatabaseConnection.getUser(username)) == null) {
                return new Result(false, "", "Invalid username.");
            } else if (DatabaseConnection.checkPassword(username, password)) {
                User.setCurrentUser(user);
                if (!user.isVerified()) {
                    sendVerificationCode(User.getCurrentUser());
                    return new Result(false, "Not verified", "Account not verified. Redirecting to verification screen.");
                } else {
                    saveUserSession(user);  // Save session
                    if (user.isTwoFactorOn()) {
                        sendVerificationCode(User.getCurrentUser());
                        return new Result(true, "Code needed for 2FA", "User logged in successfully. Please check your email for the verification code.");
                    } else {
                        App.getServerConnection().sendMessage("login:" + User.getCurrentUser().getUsername());
                        return new Result(true, "Login successful", "Welcome " + user.getNickname() + "!");
                    }
                }
            } else {
                return new Result(false, "", "Invalid password.");
            }
        } catch (SQLException e) {
            return new Result(false, "", "Error during login: " + e.getMessage());
        }
    }
}
