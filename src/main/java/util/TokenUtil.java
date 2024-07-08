package util;

import model.Token;
import model.User;
import view.LoginMenuController;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.prefs.Preferences;

public class TokenUtil {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static void saveToken(User user) throws SQLException {
        String token = generateToken();
        LocalDateTime expiration = LocalDateTime.now().plusDays(7); // Token valid for 7 days

        Token tokenObject = new Token();
        tokenObject.setUserId(user.getUsername());
        tokenObject.setToken(token);
        tokenObject.setExpiration(expiration);

        DatabaseConnection.insertToken(tokenObject);

        Preferences preferences = Preferences.userNodeForPackage(LoginMenuController.class);
        preferences.put("token", token);
    }

    public static boolean isTokenValid(String token) throws SQLException {
        Token tokenObject = DatabaseConnection.getTokenByToken(token);

        if (tokenObject == null) {
            return false;
        }

        return tokenObject.getExpiration().isAfter(LocalDateTime.now());
    }
}
