package server;

import enums.cardsinformation.Faction;
import model.Deck;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gwent";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

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

    public static void saveGame(Game game) throws SQLException, IOException {
        String query = "INSERT INTO Games (player1, player2, date, status, winner, game_data) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, game.getPlayer1().getUsername());
            stmt.setString(2, game.getPlayer2().getUsername());
            stmt.setTimestamp(3, new Timestamp(game.getDate().getTime()));
            stmt.setString(4, game.getStatus().name());
            stmt.setString(5, game.getWinner() != null ? game.getWinner().getUsername() : null);

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(game);
            objOut.flush();
            byte[] gameData = byteOut.toByteArray();

            stmt.setBytes(6, gameData);
            stmt.executeUpdate();
        }
    }

    public static Game getGame(int gameId) throws SQLException, IOException, ClassNotFoundException {
        String query = "SELECT game_data FROM Games WHERE game_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    byte[] gameData = rs.getBytes("game_data");
                    ByteArrayInputStream byteIn = new ByteArrayInputStream(gameData);
                    ObjectInputStream objIn = new ObjectInputStream(byteIn);
                    return (Game) objIn.readObject();
                } else {
                    return null;
                }
            }
        }
    }

    public static void saveUser(User user) throws SQLException, IOException {
        String query = "INSERT INTO Users (username, nickname, email, password, question_number, answer, high_score, faction, leader, deck, decks, play_card) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getNickname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.getQuestionNumber());
            stmt.setString(6, user.getAnswer());
            stmt.setInt(7, user.getHighScore());
            stmt.setString(8, user.getFaction().toString());

            ByteArrayOutputStream bos;
            ObjectOutputStream oos;

            // Serialize Leader
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(user.getLeader());
            stmt.setBytes(9, bos.toByteArray());

            // Serialize Deck
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(user.getDeck());
            stmt.setBytes(10, bos.toByteArray());

            // Serialize Decks
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(user.getDecks());
            stmt.setBytes(11, bos.toByteArray());

            // Serialize PlayCard
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(user.getPlayCard());
            stmt.setBytes(12, bos.toByteArray());

            stmt.executeUpdate();
        }
    }

    public static User getUser(String username) throws SQLException, IOException, ClassNotFoundException {
        String query = "SELECT * FROM Users WHERE username = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nickname = rs.getString("nickname");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    int questionNumber = rs.getInt("question_number");
                    String answer = rs.getString("answer");
                    int highScore = rs.getInt("high_score");
                    Faction faction = Faction.valueOf(rs.getString("faction"));

                    ByteArrayInputStream bis;
                    ObjectInputStream ois;

                    // Deserialize Leader
                    bis = new ByteArrayInputStream(rs.getBytes("leader"));
                    ois = new ObjectInputStream(bis);
                    Leader leader = (Leader) ois.readObject();

                    // Deserialize Deck
                    bis = new ByteArrayInputStream(rs.getBytes("deck"));
                    ois = new ObjectInputStream(bis);
                    Deck deck = (Deck) ois.readObject();

                    // Deserialize Decks
                    bis = new ByteArrayInputStream(rs.getBytes("decks"));
                    ois = new ObjectInputStream(bis);
                    ArrayList<Deck> decks = (ArrayList<Deck>) ois.readObject();

                    // Deserialize PlayCard
                    bis = new ByteArrayInputStream(rs.getBytes("play_card"));
                    ois = new ObjectInputStream(bis);
                    Card playCard = (Card) ois.readObject();

                    User user = new User(username, nickname, email, password);
                    user.setQuestionNumber(questionNumber);
                    user.setAnswer(answer);
                    user.setHighScore(highScore);
                    user.setFaction(faction);
                    user.setLeader(leader);
                    user.setDeck(deck);
                    user.getDecks().addAll(decks);
                    user.setPlayCard(playCard);

                    return user;
                } else {
                    return null;
                }
            }
        }
    }
}
