package view;

import enums.Menu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.App;
import model.User;
import util.DatabaseConnection;
import util.EmailSender;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    //Alert
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Check User Information Format
    public static boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) hasUpper = true;
            else if (Character.isLowerCase(character)) hasLower = true;
            else if (Character.isDigit(character)) hasDigit = true;
            else if (!Character.isLetterOrDigit(character)) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    public static String suggestNewUsername(String username) {
        return username + new Random().nextInt(1000);
    }

    public static String generateRandomPassword() {
        StringBuilder password = getInitialPassword();
        char[] passwordArray = password.toString().toCharArray();
        List<Character> passwordList = new ArrayList<>();
        for (char character : passwordArray) {
            passwordList.add(character);
        }
        Collections.shuffle(passwordList);
        password = new StringBuilder();
        for (char character : passwordList) {
            password.append(character);
        }
        return password.toString();
    }

    private static StringBuilder getInitialPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+<>?";
        String allChars = upper + lower + digits + special;
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        for (int i = 4; i < random.nextInt(12, 15); i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return password;
    }

    static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static void sendVerificationCode(User user) throws SQLException {
        String verificationCode = generateVerificationCode();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10); // Code valid for 10 minutes
        DatabaseConnection.insertVerificationCode(user.getUsername(), verificationCode, expirationTime);
        // Send verification email
        EmailSender.sendVerificationEmail(user.getEmail(), verificationCode);
    }

    public static void openMessagingWindow(String chatUsername) {
        try {
            FXMLLoader loader = new FXMLLoader(Tools.class.getResource("/fxml/MessagingMenu.fxml"));
            Parent root = loader.load();
            MessagingController controller = loader.getController();
            controller.setCurrentChatUser(chatUsername);

            Stage stage = new Stage();
            stage.setTitle("Messaging");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearUserSession() {
        Preferences prefs = Preferences.userNodeForPackage(LoginMenuController.class);
        prefs.remove("username");
        prefs.remove("password");
    }

    public static void loadUserSession() {
        Preferences prefs = Preferences.userNodeForPackage(LoginMenuController.class);
        String username = prefs.get("username", null);
        String password = prefs.get("password", null);

        if (username != null && password != null) {
            try {
                User user = DatabaseConnection.getUser(username);
                if (user != null && DatabaseConnection.checkPassword(username, password)) {
                    User.setCurrentUser(user);
                    if (!user.isVerified()) {
                        sendVerificationCode(User.getCurrentUser());
                        App.loadScene(Menu.VERIFY_MENU.getPath());
                    } else {
                        App.setIsLoggedIn(true);
                        if (user.isTwoFactorOn()) {
                            sendVerificationCode(User.getCurrentUser());
                            App.loadScene(Menu.VERIFY_MENU.getPath());
                        } else {

                            App.loadScene(Menu.MAIN_MENU.getPath());
                            App.getServerConnection().setLogin(User.getCurrentUser().getUsername());

                        }
                    }
                }
            } catch (SQLException e) {
                Tools.showAlert("Error loading session: " + e.getMessage());
            }
        } else {
            App.loadScene(Menu.LOGIN_MENU.getPath());
        }
    }

}
