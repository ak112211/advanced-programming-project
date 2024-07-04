package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Deck;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://37.152.188.83:3306/gwent";
    private static final String USER = "gwentUser";
    private static final String PASSWORD = "alikh53";
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
        String query = "UPDATE Users SET password = ? WHERE username = ?";
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

    public static void updateUserProfile(User user, String oldUsername, boolean isTwoFactorOn) throws SQLException {
        String query = "UPDATE Users SET username = ?, nickname = ?, email = ?, password = ? , two_factor_on = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            user.setTwoFactorOn(isTwoFactorOn);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getNickname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setBoolean(5, user.isTwoFactorOn());
            stmt.setString(6, oldUsername); // Assuming there is an id field to uniquely identify the user
            stmt.executeUpdate();
        }

        if (!user.getUsername().equals(oldUsername)) {
            updateUsername(oldUsername, user.getUsername());
        }
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
        String query = "SELECT answer FROM Users WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedAnswer = resultSet.getString("answer");
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

    public static void updateUserScore(User user) throws SQLException {
        String query = "UPDATE Users SET high_score = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getHighScore());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.executeUpdate();
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
        List<String> Games = new ArrayList<>();
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

                    Games.add(String.format("Opponent: %s, Date: %s, Rounds: [%d, %d, %d] - [%d, %d, %d], Final Scores: %d - %d, Winner: %s",
                            opponent, date, player1Round1, player1Round2, player1Round3, player2Round1, player2Round2, player2Round3, player1FinalScore, player2FinalScore, winner));
                }
            }
        }
        return Games;
    }

    public static void updateGame(Game game) throws SQLException {
        String query = "UPDATE Games SET player1 = ?, player2 = ?, date = ?, status = ?, winner = ?, game_data = ?, is_online = ? WHERE game_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
            preparedStatement.setInt(8, game.getID());
            preparedStatement.executeUpdate();
        }
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

            game.setID(getTotalGamesCount() + 1);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Card.class, new CardSerializer())
                    .registerTypeAdapter(Leader.class, new LeaderSerializer())
                    .setPrettyPrinting()
                    .create();
            String gameData = gson.toJson(game);

            preparedStatement.setString(6, gameData);
            preparedStatement.setBoolean(7, game.isOnline());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int gameId = generatedKeys.getInt(1);
                    game.setID(gameId);
                    if (game.isOnline()) {
                        addGameToUser(game.getPlayer1().getUsername(), gameId);
                        addGameToUser(game.getPlayer2().getUsername(), gameId);
                    } else {
                        addGameToUser(game.getPlayer1().getUsername(), gameId);
                    }
                } else {
                    throw new SQLException("Creating game failed, no ID obtained.");
                }
            }
        }
    }

    private static void addGameToUser(String username, int gameId) throws SQLException {
        String query = "UPDATE Users SET Games = JSON_ARRAY_APPEND(Games, '$', ?) WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteGame(int gameId) throws SQLException {
        // Get the game from the database to find the players
        Game game = getGame(gameId);
        if (game == null) {
            throw new SQLException("Game not found.");
        }

        // Remove game ID from players' game lists
        removeGameFromUser(game.getPlayer1().getUsername(), gameId);
        if (game.isOnline() && game.getPlayer2() != null) {
            removeGameFromUser(game.getPlayer2().getUsername(), gameId);
        }

        // Delete the game record from the database
        String query = "DELETE FROM Games WHERE game_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);
            preparedStatement.executeUpdate();
        }
    }

    public static List<Game> getSavedOfflineGames(String username) throws SQLException {
        List<Game> Games = new ArrayList<>();
        String query = "SELECT game_id, game_data FROM Games WHERE (player1 = ? OR player2 = ?) AND is_online = FALSE AND status = 'PENDING'";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String gameData = resultSet.getString("game_data");
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Game.class, new GameDeserializer())
                            .create();
                    Game game = gson.fromJson(gameData, Game.class);
                    Games.add(game);
                }
            }
        }
        return Games;
    }


    private static void removeGameFromUser(String username, int gameId) throws SQLException {
        User user = getUser(username);
        if (user == null) {
            throw new SQLException("User not found.");
        }
        List<Integer> gameIds = user.getGames();
        gameIds.remove(Integer.valueOf(gameId));
        String updatedGamesJson = new Gson().toJson(gameIds);

        String query = "UPDATE Users SET Games = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, updatedGamesJson);
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
        String query = "INSERT INTO Users (username, nickname, email, password, security_question, answer, high_score, deck, decks, play_card, friends, Games, verified, two_factor_on) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, !Objects.equals(user.getSecurityQuestion(), "") ? user.getSecurityQuestion() : "");
            preparedStatement.setString(6, user.getAnswer() != null ? user.getAnswer() : "");
            preparedStatement.setInt(7, user.getHighScore());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Card.class, new CardSerializer())
                    .registerTypeAdapter(Leader.class, new LeaderSerializer())
                    .setPrettyPrinting()
                    .create();


            preparedStatement.setString(8, user.getDeck() != null ? gson.toJson(user.getDeck()) : "");
            preparedStatement.setString(9, user.getDecks() != null ? gson.toJson(user.getDecks()) : "");
            preparedStatement.setString(10, user.getPlayCard() != null ? gson.toJson(user.getPlayCard()) : "");
            preparedStatement.setString(11, user.getFriends() != null ? GSON.toJson(user.getFriends()) : "[]");
            preparedStatement.setString(12, user.getGames() != null ? GSON.toJson(user.getGames()) : "[]");
            preparedStatement.setBoolean(13, false);
            preparedStatement.setBoolean(14, false);
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
                    String securityQuestion = resultSet.getString("security_question");
                    String answer = resultSet.getString("answer");
                    boolean verified = resultSet.getBoolean("verified");
                    boolean twoFactorOn = resultSet.getBoolean("two_factor_on");
                    int highScore = resultSet.getInt("high_score");

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Deck.class, new DeckDeserializer());
                    gsonBuilder.registerTypeAdapter(Game.class, new GameDeserializer());
                    Gson gson = gsonBuilder.create();
                    Deck deck = Deck.fromJson(resultSet.getString("deck"));

                    ArrayList<Deck> decks = gson.fromJson(resultSet.getString("decks"), new TypeToken<ArrayList<Deck>>() {
                    }.getType());

                    List<Integer> gameIds = gson.fromJson(resultSet.getString("Games"), new TypeToken<List<Integer>>() {}.getType());

                    List<String> friends = GSON.fromJson(resultSet.getString("friends"), ArrayList.class);

                    User user = new User(username, nickname != null ? nickname : "", email != null ? email : "", password != null ? password : "");
                    user.setSecurityQuestion(securityQuestion);
                    user.setAnswer(answer != null ? answer : "");
                    user.setHighScore(highScore);
                    user.setGames(gameIds);
                    user.setDeck(deck != null ? deck : new Deck());
                    user.setDecks(decks != null ? decks : new ArrayList<>());
                    user.setPlayCard(null);
                    user.setFriends(friends != null ? friends : new ArrayList<>());
                    user.setVerified(verified);
                    user.setTwoFactorOn(twoFactorOn);
                    return user;
                } else {
                    return null;
                }
            }
        }
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

    public static List<String> getFriendsList(String username) throws SQLException {
        String query = "SELECT friends FROM Users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String friendsJson = resultSet.getString("friends");
                    if (friendsJson != null && !friendsJson.isEmpty()) {
                        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                        return GSON.fromJson(friendsJson, listType);
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public static void saveMessage(String sender, String recipient, String message) throws SQLException {
        String query = "INSERT INTO messages (sender, recipient, message) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, recipient);
            stmt.setString(3, message);
            stmt.executeUpdate();
        }
    }

    public static boolean saveGameRequest(String sender, String recipient) throws SQLException {
        String query = "INSERT INTO gamerequests (sender, recipient, status) VALUES (?, ?, 'pending')";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, recipient);
            stmt.executeUpdate();
            return true;
        }
    }

    public static List<String> getGameRequests(String username) throws SQLException {
        String query = "SELECT * FROM gamerequests WHERE recipient = ? AND status = 'pending' ORDER BY timestamp DESC";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> requests = new ArrayList<>();
                while (rs.next()) {
                    String sender = rs.getString("sender");
                    String timestamp = rs.getString("timestamp");
                    requests.add("Game request from " + sender + " (" + timestamp + ")");
                }
                return requests;
            }
        }
    }

    public static void updateGameRequestStatus(String sender, String recipient, String status) throws SQLException {
        String query = "UPDATE gamerequests SET status = ? WHERE sender = ? AND recipient = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, sender);
            stmt.setString(3, recipient);
        }
    }

    public static void acceptGameRequest(String sender, String recipient) throws SQLException {
        updateGameRequestStatus(sender, recipient, "accepted");
    }

    public static void declineGameRequest(String sender, String recipient) throws SQLException {
        updateGameRequestStatus(sender, recipient, "declined");
        deleteGameRequest(sender, recipient);
    }

    public static void deleteGameRequest(String sender, String recipient) throws SQLException {
        String query = "DELETE FROM gamerequests WHERE sender = ? AND recipient = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, recipient);
            int rowsDeleted = stmt.executeUpdate();
        }
    }

    public static boolean sendFriendRequest(String username, String friendUsername) throws SQLException {
        // Check if the friend already exists in the friends list
        List<String> friends = getFriendsList(username);
        if (friends.contains(friendUsername)) {
            return false; // Friend already in the list
        }

        // Check if the friend request already exists
        String checkQuery = "SELECT COUNT(*) FROM friendrequests WHERE sender = ? AND recipient = ?";
        try (Connection connection = getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            checkStmt.setString(2, friendUsername);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Friend request already sent
                }
            }
        }

        // Insert the friend request into the friendrequests table
        String query = "INSERT INTO friendrequests (sender, recipient, status) VALUES (?, ?, 'pending')";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, friendUsername);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public static List<String> getFriendRequests(String username) throws SQLException {
        String query = "SELECT * FROM friendrequests WHERE recipient = ? AND status = 'pending' ORDER BY timestamp DESC";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> requests = new ArrayList<>();
                while (rs.next()) {
                    String sender = rs.getString("sender");
                    String timestamp = rs.getString("timestamp");
                    requests.add("Friend request from " + sender + " (" + timestamp + ")");
                }
                return requests;
            }
        }
    }

    public static void declineFriendRequest(String sender, String recipient) throws SQLException {
        updateFriendRequestStatus(sender, recipient, "declined");
    }

    private static void updateFriendRequestStatus(String sender, String recipient, String status) throws SQLException {
        String query = "UPDATE friendrequests SET status = ? WHERE sender = ? AND recipient = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, sender);
            stmt.setString(3, recipient);
        }
    }

    public static void acceptFriendRequest(String friendUsername, String username) throws SQLException {
        String acceptQuery = "UPDATE friendrequests SET status = 'accepted' WHERE sender = ? AND recipient = ?";
        try (Connection connection = getConnection();
             PreparedStatement acceptStmt = connection.prepareStatement(acceptQuery)) {
            acceptStmt.setString(1, friendUsername);
            acceptStmt.setString(2, username);
            int rowsUpdated = acceptStmt.executeUpdate();
            if (rowsUpdated > 0) {
                addFriend(username, friendUsername);
                addFriend(friendUsername, username);
            }
        }
    }

    public static void addFriend(String username, String friendUsername) throws SQLException {
        List<String> friends = getFriendsList(username);
        if (friends.contains(friendUsername)) {
            return;
        }
        friends.add(friendUsername);
        String updatedFriendsJson = GSON.toJson(friends);

        String query = "UPDATE Users SET friends = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, updatedFriendsJson);
            preparedStatement.setString(2, username);
        }
    }

    // Method to insert a verification code
    public static void insertVerificationCode(String username, String code, LocalDateTime expiration) throws SQLException {
        String query = "INSERT INTO email_verification (username, code, expiration) VALUES (?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, code);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(expiration));
            preparedStatement.executeUpdate();
        }
    }

    // Method to verify the code
    public static boolean verifyCode(String username, String code) throws SQLException {
        String query = "SELECT verified FROM email_verification WHERE username = ? AND code = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    boolean verified = resultSet.getBoolean("verified");

                    if (verified) {
                        return false; // Code already used
                    }

                    markCodeAsVerified(username, code);
                    return true;
                }
                return false;
            }
        }
    }

    // Method to mark the code as verified
    public static void markCodeAsVerified(String username, String code) throws SQLException {
        String updateVerificationQuery = "UPDATE email_verification SET verified = TRUE WHERE username = ? AND code = ?";
        String updateUserVerifiedQuery = "UPDATE Users SET verified = TRUE WHERE username = ?";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); // Begin transaction

            try (PreparedStatement updateVerificationStmt = connection.prepareStatement(updateVerificationQuery);
                 PreparedStatement updateUserVerifiedStmt = connection.prepareStatement(updateUserVerifiedQuery)) {

                // Update the email_verification table
                updateVerificationStmt.setString(1, username);
                updateVerificationStmt.setString(2, code);
                updateVerificationStmt.executeUpdate();

                // Update the Users table
                updateUserVerifiedStmt.setString(1, username);
                updateUserVerifiedStmt.executeUpdate();

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on error
                throw e;
            }
        }
    }

    public static void updateUsername(String oldUsername, String newUsername) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); // Begin transaction

            try {
                // Update friends lists
                String getUsersQuery = "SELECT username, friends FROM Users";
                try (PreparedStatement stmt = connection.prepareStatement(getUsersQuery);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String username = rs.getString("username");
                        String friendsJson = rs.getString("friends");
                        List<String> friends = GSON.fromJson(friendsJson, new TypeToken<List<String>>() {}.getType());

                        if (friends.contains(oldUsername)) {
                            friends.remove(oldUsername);
                            friends.add(newUsername);
                            String updatedFriendsJson = GSON.toJson(friends);

                            String updateFriendsQuery = "UPDATE Users SET friends = ? WHERE username = ?";
                            try (PreparedStatement updateStmt = connection.prepareStatement(updateFriendsQuery)) {
                                updateStmt.setString(1, updatedFriendsJson);
                                updateStmt.setString(2, username);
                                updateStmt.executeUpdate();
                            }
                        }
                    }
                }

                // Update messages table
                String updateMessagesQuery = "UPDATE messages SET sender = ? WHERE sender = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateMessagesQuery)) {
                    stmt.setString(1, newUsername);
                    stmt.setString(2, oldUsername);
                    stmt.executeUpdate();
                }
                updateMessagesQuery = "UPDATE messages SET recipient = ? WHERE recipient = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateMessagesQuery)) {
                    stmt.setString(1, newUsername);
                    stmt.setString(2, oldUsername);
                    stmt.executeUpdate();
                }

                // Update friend requests table
                String updateFriendRequestsQuery = "UPDATE friendrequests SET sender = ? WHERE sender = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateFriendRequestsQuery)) {
                    stmt.setString(1, newUsername);
                    stmt.setString(2, oldUsername);
                    stmt.executeUpdate();
                }
                updateFriendRequestsQuery = "UPDATE friendrequests SET recipient = ? WHERE recipient = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateFriendRequestsQuery)) {
                    stmt.setString(1, newUsername);
                    stmt.setString(2, oldUsername);
                    stmt.executeUpdate();
                }

                // Update Games table
                String updateGamesQuery = "UPDATE Games SET player1 = ? WHERE player1 = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateGamesQuery)) {
                    stmt.setString(1, newUsername);
                    stmt.setString(2, oldUsername);
                    stmt.executeUpdate();
                }
                updateGamesQuery = "UPDATE Games SET player2 = ? WHERE player2 = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateGamesQuery)) {
                    stmt.setString(1, newUsername);
                    stmt.setString(2, oldUsername);
                    stmt.executeUpdate();
                }

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on error
                throw e;
            }
        }
    }

    public static List<String> getMessagesBetweenUsers(String username1, String username2) throws SQLException {
        String query = "SELECT sender, message, timestamp FROM messages " +
                "WHERE (sender = ? AND recipient = ?) OR (sender = ? AND recipient = ?) " +
                "ORDER BY timestamp ASC";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username1);
            stmt.setString(2, username2);
            stmt.setString(3, username2);
            stmt.setString(4, username1);
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> messages = new ArrayList<>();
                while (rs.next()) {
                    String sender = rs.getString("sender");
                    String message = rs.getString("message");
                    String timestamp = rs.getString("timestamp");
                    messages.add(String.format("%s [%s]: %s", sender, timestamp, message));
                }
                return messages;
            }
        }
    }

    public static int getTotalGamesCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Games";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }

    public static void saveDeck() {
        String query = "UPDATE Users SET deck = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, User.getCurrentUser().getDeck().toJson());
            preparedStatement.setString(2, User.getCurrentUser().getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
