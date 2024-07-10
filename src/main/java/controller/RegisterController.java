package controller;

import enums.SecurityQuestion;
import model.App;
import model.Result;
import model.User;
import util.DatabaseConnection;
import view.Tools;

import java.sql.SQLException;

import static view.Tools.sendVerificationCode;

public class RegisterController {
    public static Result register(String username, String nickname, String email,
                                  String password, String confirmPassword,
                                  SecurityQuestion securityQuestion, String answer, String answerConfirm) {
        try {
            if (DatabaseConnection.getUser(username) != null) {
                String newUsername = Tools.suggestNewUsername(username);
                return new Result(false, "Username Taken", "Username is already taken. Suggested username: " + newUsername);
            } else if (!Tools.isValidUsername(username)) {
                return new Result(false, "Invalid Username", "Invalid username. Only letters, numbers, and '-' are allowed.");
            } else if (!Tools.isValidEmail(email)) {
                return new Result(false, "Invalid Email", "Invalid email address.");
            } else if (!Tools.isValidPassword(password)) {
                return new Result(false, "Weak Password", "Invalid password. Password must be at least 8 characters long, include uppercase, lowercase, numbers, and special characters.");
            } else if (!password.equals(confirmPassword)) {
                return new Result(false, "Password Mismatch", "Password and confirm password do not match.");
            } else if (securityQuestion == null || answer.isEmpty() || !answer.equals(answerConfirm)) {
                return new Result(false, "Security Question", "Security question must be selected and answers must match.");
            } else {
                User user = new User(username, nickname, email, password);
                user.setSecurityQuestion(securityQuestion.getQuestion());
                user.setAnswer(answer);
                user.setVerified(false);
                user.setTwoFactorOn(false);
                User.setCurrentUser(user);
                DatabaseConnection.saveUser(user);
                // Generate verification code
                sendVerificationCode(User.getCurrentUser());
                App.getServerConnection().sendMessage("register:" + username);
                return new Result(true, "Registration Successful", "User registered successfully. Please check your email for the verification code.");
            }
        } catch (SQLException e) {
            return new Result(false, "Registration Failed", "Error registering user: " + e.getMessage());
        }
    }
}
