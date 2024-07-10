package view;

import enums.Menu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import model.App;
import model.Token;
import model.User;
import util.DatabaseConnection;
import util.TokenUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    // Media
    public static Media getMedia(String path) {
        return new Media(Objects.requireNonNull(Tools.class.getResource(path)).toExternalForm());
    }

    public static MediaPlayer getMediaPlayer(String path) {
        return new MediaPlayer(getMedia(path));
    }

    // Image
    public static Image getImage(String path) {
        return new Image(Objects.requireNonNull(Tools.class.getResource(path)).toExternalForm());
    }

    public static ImageView getImageView(String path) {
        return new ImageView(getImage(path));
    }

    public static ImagePattern getImagePattern(String path) {
        return new ImagePattern(getImage(path));
    }

    // Alert
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

    // Load
    public static void loadScene(Menu menu) {
        Object currentController = App.getCurrentController();
        try {
            if (currentController != null) {
                switch (currentController) {
                    case ChatMenuController chatMenuController -> chatMenuController.cleanup();
                    case LobbyController lobbyController -> lobbyController.cleanup();
                    case GamePaneController gamePaneController -> gamePaneController.cleanup();
                    case MainMenuController mainMenuController -> mainMenuController.cleanup();
                    case MessagingController messagingController -> messagingController.cleanup();
                    case ScoreboardController scoreboardController -> scoreboardController.cleanup();
                    case ViewGamePlayController viewGamePlayController -> viewGamePlayController.cleanup();
                    default -> {
                    }
                }
            }
            FXMLLoader loader = new FXMLLoader(App.class.getResource(menu.getPath()));
            Parent root = loader.load();
            App.setCurrentController(loader.getController());
            Stage stage = App.getStage();
            App.setMenu(menu);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("/css/styles.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Verification

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
        System.out.println(user.getEmail() + ":" + verificationCode);
        App.getServerConnection().sendMessage("send verification for:" + user.getEmail() + ":" + verificationCode);
    }

    public static void openMessagingWindow(String chatUsername) {
        try {
            if (App.getMessagingStage() != null) {
                App.getMessagingStage().close();
                App.setMessagingStage(null);
            }

            FXMLLoader loader = new FXMLLoader(Tools.class.getResource("/fxml/MessagingMenu.fxml"));
            Parent root = loader.load();
            MessagingController controller = loader.getController();
            controller.setCurrentChatUser(chatUsername);

            Stage stage = new Stage();
            stage.setTitle("Messaging");
            stage.setScene(new Scene(root));
            stage.show();
            App.setMessagingStage(stage);
            stage.setOnCloseRequest(event -> {
                controller.cleanup();
                App.setMessagingStage(null);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearUserSession() {
        Preferences preferences = Preferences.userNodeForPackage(LoginMenuController.class);
        preferences.remove("token");

        // Optionally delete token from database
        User user = User.getCurrentUser();
        if (user != null) {
            try {
                DatabaseConnection.deleteTokenByUserId(user.getUsername());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadUserSession() {
        Preferences preferences = Preferences.userNodeForPackage(LoginMenuController.class);
        String token = preferences.get("token", null);

        if (token != null) {
            try {
                if (TokenUtil.isTokenValid(token)) {
                    Token tokenObject = DatabaseConnection.getTokenByToken(token);
                    assert tokenObject != null;
                    User user = DatabaseConnection.getUser(tokenObject.getUserId());
                    User.setCurrentUser(user);
                    loadScene(Menu.MAIN_MENU);
                    assert user != null;
                    App.getServerConnection().sendMessage("login:" + user.getUsername());
                    return;
                }
            } catch (SQLException e) {
                Tools.showAlert("Error loading session: " + e.getMessage());
            }
        }
        loadScene(Menu.LOGIN_MENU);
    }

    public static void saveUserSession(User user) {
        try {
            TokenUtil.saveToken(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
