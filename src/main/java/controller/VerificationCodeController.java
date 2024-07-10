package controller;

import model.App;
import model.Result;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;

import static view.Tools.saveUserSession;

public class VerificationCodeController {
    public static Result verify(String code) {
        try {
            if (DatabaseConnection.verifyCode(User.getCurrentUser().getUsername(), code)) {
                App.getServerConnection().sendMessage("login:" + User.getCurrentUser().getUsername());
                saveUserSession(User.getCurrentUser());  // Save session
                return new Result(true, "Verification Successful", "Your account has been successfully verified.");
            } else {
                return new Result(false, "Verification Failed", "Invalid or expired verification code.");
            }
        } catch (SQLException e) {
            return new Result(false, "Verification Failed", "An error occurred while verifying the code: " + e.getMessage());
        }
    }
}
