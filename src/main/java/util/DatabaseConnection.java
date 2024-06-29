package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
    private static final Gson GSON = new Gson();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
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

    public static boolean updatePassword(String username, String password) {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean updateUserProfile(String username, String nickname, String email) {
        String query = "UPDATE users SET username = ?, nickname = ?, email = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, nickname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, User.getCurrentUser().getUsername());
            int rowsUpdated = preparedStatement.executeUpdate();
            User.getCurrentUser().setUsername(username);
            User.getCurrentUser().setNickname(nickname);
            User.getCurrentUser().setEmail(email);

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

    public static void saveGame(Game game) throws SQLException {
        String query = "INSERT INTO Games (player1, player2, date, status, winner, game_data, is_online) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, game.getPlayer1().getUsername());
            preparedStatement.setString(2, game.getPlayer2().getUsername());
            preparedStatement.setTimestamp(3, new Timestamp(game.getDate().getTime()));
            preparedStatement.setString(4, game.getStatus().name());
            preparedStatement.setString(5, game.getWinner() != null ? game.getWinner().getUsername() : null);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Card.class, new CardSerializer())
                    .registerTypeAdapter(Leader.class, new LeaderSerializer())
                    .setPrettyPrinting()
                    .create();
            String gameData = gson.toJson(game);

            preparedStatement.setString(6, gameData);
            preparedStatement.setBoolean(7, game.isOnline());
            preparedStatement.executeUpdate();

            // Retrieve the generated game ID
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int gameId = generatedKeys.getInt(1);
                    addGameToUser(game.getPlayer1().getUsername(), gameId);
                    addGameToUser(game.getPlayer2().getUsername(), gameId);
                } else {
                    throw new SQLException("Creating game failed, no ID obtained.");
                }
            }
        }
    }

    private static void addGameToUser(String username, int gameId) throws SQLException {
        String query = "UPDATE Users SET games = JSON_ARRAY_APPEND(games, '$', ?) WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, gameId);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }
    }

    public static Game getGame(int gameId) throws SQLException {
        String query = "SELECT game_data FROM Games WHERE game_id = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String gameData = resultSet.getString("game_data");

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Deck.class, new DeckDeserializer());
                    gsonBuilder.registerTypeAdapter(Game.class, new GameDeserializer());
                    Gson gson = gsonBuilder.create();

                    return gson.fromJson(gameData, Game.class);
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
            preparedStatement.setString(8, user.getDeck().toJson());
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Card.class, new CardSerializer())
                    .registerTypeAdapter(Leader.class, new LeaderSerializer())
                    .setPrettyPrinting()
                    .create();

            preparedStatement.setString(9, gson.toJson(user.getDecks()));
            preparedStatement.setString(10, null);
            preparedStatement.setString(11, GSON.toJson(user.getFriends()));
            preparedStatement.setString(12, gson.toJson(user.getGames()));

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


                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Deck.class, new DeckDeserializer());
                    gsonBuilder.registerTypeAdapter(Game.class, new GameDeserializer());
                    Gson gson = gsonBuilder.create();
                    Deck deck = Deck.fromJson(resultSet.getString("deck"));
                    ArrayList<Deck> decks = gson.fromJson(resultSet.getString("decks"), new TypeToken<ArrayList<Deck>>() {
                    }.getType());
                    ArrayList<Game> games = gson.fromJson(resultSet.getString("games"), new TypeToken<ArrayList<Game>>() {
                    }.getType());


                    Card playCard = null;
                    List<String> friends = GSON.fromJson(resultSet.getString("friends"), ArrayList.class);

                    User user = new User(username, nickname != null ? nickname : "", email != null ? email : "", password != null ? password : "");
                    user.setQuestionNumber(questionNumber);
                    user.setAnswer(answer != null ? answer : "");
                    user.setHighScore(highScore);
                    user.setGames(games);
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

    public static int getUserRank(String username) throws SQLException {
        String userExistsQuery = "SELECT COUNT(*) FROM Users WHERE username = ?";
        String rankQuery = "SELECT COUNT(*) + 1 FROM Users WHERE high_score > (SELECT high_score FROM Users WHERE username = ?)";

        try (Connection conn = getConnection();
             PreparedStatement userExistsStmt = conn.prepareStatement(userExistsQuery);
             PreparedStatement rankStmt = conn.prepareStatement(rankQuery)) {

            userExistsStmt.setString(1, username);
            try (ResultSet rs = userExistsStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new SQLException("User not found.");
                }
            }

            rankStmt.setString(1, username);
            try (ResultSet rs = rankStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("User rank calculation failed.");
                }
            }
        }
    }


    public static List<User> getTopUsers(int limit) throws SQLException {
        String query = "SELECT username, nickname, high_score FROM Users ORDER BY high_score DESC LIMIT ?";
        List<User> topUsers = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String nickname = rs.getString("nickname");
                    int highScore = rs.getInt("high_score");

                    User user = new User(username, nickname, null, null);
                    user.setHighScore(highScore);
                    topUsers.add(user);
                }
            }
        }
        return topUsers;
    }


    public static void saveMessage(String sender, String receiver, String message) throws SQLException {
        String query = "INSERT INTO Messages (sender, receiver, message) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setString(3, message);
            stmt.executeUpdate();
        }
    }

    public static String getMessages(String username) throws SQLException {
        String query = "SELECT * FROM Messages WHERE receiver = ?";
        StringBuilder messages = new StringBuilder();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.append(rs.getString("sender")).append(": ").append(rs.getString("message")).append("\n");
                }
            }
        }
        return messages.toString();
    }

    public static void saveGameRequest(String sender, String receiver) throws SQLException {
        String query = "INSERT INTO GameRequests (sender, receiver) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.executeUpdate();
        }
    }

    public static String getGameRequests(String username) throws SQLException {
        String query = "SELECT * FROM GameRequests WHERE receiver = ?";
        StringBuilder requests = new StringBuilder();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.append(rs.getString("sender")).append(" has challenged you to a game.\n");
                }
            }
        }
        return requests.toString();
    }

}
