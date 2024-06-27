package util;

import com.google.gson.Gson;
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
    private static final Gson gson = new Gson();

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

    public static boolean updatePassword(String username, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean updateUserProfile(String currentUsername, String newUsername, String nickname, String email) {
        String query = "UPDATE users SET username = ?, nickname = ?, email = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, nickname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, currentUsername);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    public static void saveUser(User user) throws SQLException {
        String query = "INSERT INTO Users (username, nickname, email, password, question_number, answer, high_score, faction, leader, deck, decks, play_card, friends) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getNickname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.getQuestionNumber());
            stmt.setString(6, user.getAnswer());
            stmt.setInt(7, user.getHighScore());
            stmt.setString(8, user.getDeck().getFaction() != null ? user.getDeck().getFaction().toString() : null);
            stmt.setString(9, gson.toJson(user.getDeck().getLeader()));
            stmt.setString(10, gson.toJson(user.getDeck()));
            stmt.setString(11, gson.toJson(user.getDecks()));
            stmt.setString(12, gson.toJson(user.getPlayCard()));
            stmt.setString(13, gson.toJson(user.getFriends()));

            stmt.executeUpdate();
        }
    }

    public static User getUser(String username) throws SQLException {
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

                    Deck deck = gson.fromJson(rs.getString("deck"), Deck.class);
                    ArrayList<Deck> decks = gson.fromJson(rs.getString("decks"), ArrayList.class);
                    Card playCard = gson.fromJson(rs.getString("play_card"), Card.class);
                    List<String> friends = gson.fromJson(rs.getString("friends"), ArrayList.class);

                    User user = new User(username, nickname != null ? nickname : "", email != null ? email : "", password != null ? password : "");
                    user.setQuestionNumber(questionNumber);
                    user.setAnswer(answer != null ? answer : "");
                    user.setHighScore(highScore);
                    user.setDeck(deck != null ? deck : new Deck());
                    user.setDecks(decks != null ? decks : new ArrayList<>());
                    user.setPlayCard(playCard);
                    user.setFriends(friends != null ? friends : new ArrayList<>());

                    return user;
                } else {
                    return null;
                }
            }
        }
    }

    public static boolean addFriend(String username, String friendUsername) {
        String query = "INSERT INTO friends (username, friend_username) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, friendUsername);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
