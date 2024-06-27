package util;

import com.google.gson.Gson;
import model.Deck;
import model.Game;
import model.User;
import model.card.Card;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gwent";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static final Gson GSON = new Gson();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void saveUser(String username, String nickname, String email, String password) throws SQLException {
        String query = "INSERT INTO Users (username, nickname, email, password) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, nickname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean isUsernameTaken(String username) throws SQLException {
        String query = "SELECT username FROM Users WHERE username = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public static boolean checkPassword(String username, String password) throws SQLException {
        String query = "SELECT password FROM Users WHERE username = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
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

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("security_question");
                } else {
                    return null;
                }
            }
        }
    }

    public static boolean validateSecurityAnswer(String username, String answer) throws SQLException {
        String query = "SELECT security_answer FROM Users WHERE username = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedAnswer = resultSet.getString("security_answer");
                    return storedAnswer.equals(answer);
                } else {
                    return false;
                }
            }
        }
    }

    public static int getDrawsCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Games WHERE (player1 = ? OR player2 = ?) AND winner IS NULL";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    public static int getWinsCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Games WHERE winner = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    public static int getLossesCount(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Games WHERE (player1 = ? OR player2 = ?) AND winner IS NOT NULL AND winner <> ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    public static List<String> getRecentGames(String username, int n) throws SQLException {
        String query = "SELECT * FROM Games WHERE player1 = ? OR player2 = ? ORDER BY date DESC LIMIT ?";

        List<String> games = new ArrayList<>();

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, n);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String opponent = resultSet.getString("player1").equals(username) ? resultSet.getString("player2") : resultSet.getString("player1");
                    String date = resultSet.getString("date");
                    int player1Round1 = resultSet.getInt("player1_round1");
                    int player1Round2 = resultSet.getInt("player1_round2");
                    int player1Round3 = resultSet.getInt("player1_round3");
                    int player2Round1 = resultSet.getInt("player2_round1");
                    int player2Round2 = resultSet.getInt("player2_round2");
                    int player2Round3 = resultSet.getInt("player2_round3");
                    int player1FinalScore = resultSet.getInt("player1_final_score");
                    int player2FinalScore = resultSet.getInt("player2_final_score");
                    String winner = resultSet.getString("winner");

                    games.add(String.format("Opponent: %s, Date: %s, Rounds: [%d, %d, %d] - [%d, %d, %d], Final Scores: %d - %d, Winner: %s",
                            opponent, date, player1Round1, player1Round2, player1Round3, player2Round1, player2Round2, player2Round3, player1FinalScore, player2FinalScore, winner));
                }
            }
        }

        return games;
    }

    public static void saveGame(Game game) throws SQLException, IOException {
        String query = "INSERT INTO Games (player1, player2, date, status, winner, game_data) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, game.getPlayer1().getUsername());
            preparedStatement.setString(2, game.getPlayer2().getUsername());
            preparedStatement.setTimestamp(3, new Timestamp(game.getDate().getTime()));
            preparedStatement.setString(4, game.getStatus().name());
            preparedStatement.setString(5, game.getWinner() != null ? game.getWinner().getUsername() : null);

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(game);
            objOut.flush();
            byte[] gameData = byteOut.toByteArray();

            preparedStatement.setBytes(6, gameData);
            preparedStatement.executeUpdate();
        }
    }

    public static Game getGame(int gameId) throws SQLException, IOException, ClassNotFoundException {
        String query = "SELECT game_data FROM Games WHERE game_id = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    byte[] gameData = resultSet.getBytes("game_data");
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

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getQuestionNumber());
            preparedStatement.setString(6, user.getAnswer());
            preparedStatement.setInt(7, user.getHighScore());
            preparedStatement.setString(8, user.getDeck().getFaction() != null ? user.getDeck().getFaction().toString() : null);
            preparedStatement.setString(9, GSON.toJson(user.getDeck().getLeader()));
            preparedStatement.setString(10, GSON.toJson(user.getDeck()));
            preparedStatement.setString(11, GSON.toJson(user.getDecks()));
            preparedStatement.setString(12, GSON.toJson(user.getPlayCard()));
            preparedStatement.setString(13, GSON.toJson(user.getFriends()));

            preparedStatement.executeUpdate();
        }
    }

    public static User getUser(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nickname = resultSet.getString("nickname");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    int questionNumber = resultSet.getInt("question_number");
                    String answer = resultSet.getString("answer");
                    int highScore = resultSet.getInt("high_score");

                    Deck deck = GSON.fromJson(resultSet.getString("deck"), Deck.class);
                    ArrayList<Deck> decks = GSON.fromJson(resultSet.getString("decks"), ArrayList.class);
                    Card playCard = GSON.fromJson(resultSet.getString("play_card"), Card.class);
                    List<String> friends = GSON.fromJson(resultSet.getString("friends"), ArrayList.class);

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
