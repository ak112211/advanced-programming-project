package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Deck;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;
import util.CardSerializer;
import util.DeckDeserializer;
import util.GameDeserializer;
import util.LeaderSerializer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gwent";
    private static final String USER = "gwentUser";
    private static final String PASSWORD = "alikh53";
    private static final Gson GSON = new Gson();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();
        String query = "SELECT username FROM Users";  // Adjust table and column names as per your database schema

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
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

    public static void updateGame(Game game) throws SQLException {
        String query = "UPDATE Games SET player1 = ?, player2 = ?, date = ?, status = ?, winner = ?, game_data = ?, is_online = ?, " +
                "player1_round1_score = ?, player1_round2_score = ?, player1_round3_score = ?, " +
                "player2_round1_score = ?, player2_round2_score = ?, player2_round3_score = ?, " +
                "player1_final_score = ?, player2_final_score = ? WHERE game_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, game.getPlayer1().getUsername());
            preparedStatement.setString(2, game.getPlayer2().getUsername());
            preparedStatement.setTimestamp(3, new Timestamp(game.getDate().getTime()));
            preparedStatement.setString(4, game.getStatus().name());
            preparedStatement.setString(5, game.getWinnerUser() != null ? game.getWinnerUser().getUsername() : null);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Card.class, new CardSerializer())
                    .registerTypeAdapter(Leader.class, new LeaderSerializer())
                    .setPrettyPrinting()
                    .create();
            String gameData = gson.toJson(game);

            preparedStatement.setString(6, gameData);
            preparedStatement.setBoolean(7, game.isOnline());

            // Set the new score fields
            preparedStatement.setInt(8, game.getRoundsInfo().getCurrentRound() == 1 ? game.getPlayer1Points() : game.getRoundsInfo().getPlayer1Score(1));
            preparedStatement.setInt(9, game.getRoundsInfo().getCurrentRound() <= 2  ? game.getPlayer1Points() : game.getRoundsInfo().getPlayer1Score(2));
            preparedStatement.setInt(10, game.getRoundsInfo().getCurrentRound() <= 3 ? game.getPlayer1Points() : game.getRoundsInfo().getPlayer1Score(3));
            preparedStatement.setInt(11, game.getRoundsInfo().getCurrentRound() == 1 ? game.getPlayer2Points() : game.getRoundsInfo().getPlayer2Score(1));
            preparedStatement.setInt(12, game.getRoundsInfo().getCurrentRound() <= 2 ? game.getPlayer2Points() : game.getRoundsInfo().getPlayer2Score(2));
            preparedStatement.setInt(13, game.getRoundsInfo().getCurrentRound() <= 3 ? game.getPlayer2Points() : game.getRoundsInfo().getPlayer2Score(3));

            int player1TotalScore = 0;
            int player2TotalScore = 0;

            for (int i = 0 ; i < game.getRoundsInfo().getCurrentRound() - 1; i++) {
                player1TotalScore += game.getRoundsInfo().getPlayer2Score(i + 1);
                player2TotalScore += game.getRoundsInfo().getPlayer2Score(i + 1);
            }

            preparedStatement.setInt(14, player1TotalScore == 0 ? game.getPlayer1Points() : player1TotalScore);
            preparedStatement.setInt(15, player2TotalScore == 0 ? game.getPlayer2Points() : player2TotalScore);

            preparedStatement.setInt(16, game.getID());
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

}
