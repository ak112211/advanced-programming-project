package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/UserDB";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void saveUser(String username, String nickname, String email, String password) throws SQLException {
        String query = "INSERT INTO Users (username, nickname, email, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, nickname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        }
    }

    public static boolean isUsernameTaken(String username) throws SQLException {
        String query = "SELECT username FROM Users WHERE username = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean checkPassword(String username, String password) throws SQLException {
        String query = "SELECT password FROM Users WHERE username = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(password);
                } else {
                    return false;
                }
            }
        }
    }

    public static void updatePassword(String username, String newPassword) throws SQLException {
        String query = "UPDATE Users SET password = ? WHERE username = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public static void updateUserProfile(String currentUsername, String newUsername, String nickname, String email) throws SQLException {
        String query = "UPDATE Users SET username = ?, nickname = ?, email = ? WHERE username = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, nickname);
            stmt.setString(3, email);
            stmt.setString(4, currentUsername);
            stmt.executeUpdate();
        }
    }

    public static String getSecurityQuestion(String username) throws SQLException {
        String query = "SELECT security_question FROM Users WHERE username = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("security_question");
                } else {
                    return null;
                }
            }
        }
    }

    public static boolean validateSecurityAnswer(String username, String answer) throws SQLException {
        String query = "SELECT security_answer FROM Users WHERE username = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedAnswer = rs.getString("security_answer");
                    return storedAnswer.equals(answer);
                } else {
                    return false;
                }
            }
        }
    }

    public static int getDrawsCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Games WHERE (player1 = ? OR player2 = ?) AND winner IS NULL";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    public static int getWinsCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Games WHERE winner = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    public static int getLossesCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Games WHERE (player1 = ? OR player2 = ?) AND winner IS NOT NULL AND winner <> ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    public static List<String> getRecentGames(String username, int n) throws SQLException {
        String query = "SELECT * FROM Games WHERE player1 = ? OR player2 = ? ORDER BY date DESC LIMIT ?";

        List<String> games = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setInt(3, n);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String opponent = rs.getString("player1").equals(username) ? rs.getString("player2") : rs.getString("player1");
                    String date = rs.getString("date");
                    int player1Round1 = rs.getInt("player1_round1");
                    int player1Round2 = rs.getInt("player1_round2");
                    int player1Round3 = rs.getInt("player1_round3");
                    int player2Round1 = rs.getInt("player2_round1");
                    int player2Round2 = rs.getInt("player2_round2");
                    int player2Round3 = rs.getInt("player2_round3");
                    int player1FinalScore = rs.getInt("player1_final_score");
                    int player2FinalScore = rs.getInt("player2_final_score");
                    String winner = rs.getString("winner");

                    games.add(String.format("Opponent: %s, Date: %s, Rounds: [%d, %d, %d] - [%d, %d, %d], Final Scores: %d - %d, Winner: %s",
                            opponent, date, player1Round1, player1Round2, player1Round3, player2Round1, player2Round2, player2Round3, player1FinalScore, player2FinalScore, winner));
                }
            }
        }

        return games;
    }

}
