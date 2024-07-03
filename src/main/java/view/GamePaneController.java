package view;

import enums.Menu;
import enums.Row;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import enums.leaders.MonstersLeaders;
import enums.leaders.RealmsNorthernLeaders;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.App;
import model.Deck;
import model.Game;
import model.User;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Decoy;
import model.abilities.instantaneousabilities.Spy;
import model.abilities.persistentabilities.Weather;
import model.card.Card;
import model.card.Leader;
import util.DatabaseConnection;

import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

import static util.DatabaseConnection.updateUserProfile;
import static util.DatabaseConnection.updateUserScore;
import static view.Tools.showAlert;

public class GamePaneController implements Initializable {
    @FXML
    public VBox pauseMenu;
    @FXML
    public Button exit;
    @FXML
    public Button exitSave;
    @FXML
    public Button quit;
    @FXML
    private Pane gamePane;
    @FXML
    private ImageView background;
    @FXML
    private Pane overlayPane;
    @FXML
    private Label player1NameLabel;
    @FXML
    private Label player2NameLabel;
    @FXML
    private Label player1ScoreLabel;
    @FXML
    private Label player2ScoreLabel;
    @FXML
    private HBox player1Hand;
    @FXML
    private HBox player2Hand;
    @FXML
    private HBox player1CloseCombat;
    @FXML
    private HBox player2CloseCombat;
    @FXML
    private HBox player1Ranged;
    @FXML
    private HBox player2Ranged;
    @FXML
    private HBox player1Siege;
    @FXML
    private HBox player2Siege;
    @FXML
    private HBox player1CloseCombatSpell;
    @FXML
    private HBox player2CloseCombatSpell;
    @FXML
    private HBox player1RangedSpell;
    @FXML
    private HBox player2RangedSpell;
    @FXML
    private HBox player1SiegeSpell;
    @FXML
    private HBox player2SiegeSpell;
    @FXML
    public HBox weather;
    @FXML
    private VBox player1Score;
    @FXML
    private VBox player2Score;
    @FXML
    private StackPane player1Graveyard;
    @FXML
    private StackPane player2Graveyard;
    @FXML
    private StackPane player1Leader;
    @FXML
    private StackPane player2Leader;

    public MediaPlayer mediaPlayer; // Assuming this handles your background music
    public boolean isMute;
    private HashMap<Row, HBox> GET_ROW_BOX, GET_ROW_BOX_SPELL;

    private Game game;
    private Card selectedCard;

    @FXML
    private Label overlayMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GET_ROW_BOX = new HashMap<>() {{
            put(Row.PLAYER1_CLOSE_COMBAT, player1CloseCombat);
            put(Row.PLAYER1_RANGED, player1Ranged);
            put(Row.PLAYER1_SIEGE, player1Siege);
            put(Row.PLAYER1_WEATHER, weather);
            put(Row.PLAYER2_CLOSE_COMBAT, player2CloseCombat);
            put(Row.PLAYER2_RANGED, player2Ranged);
            put(Row.PLAYER2_SIEGE, player2Siege);
            put(Row.PLAYER2_WEATHER, weather);
        }};

        GET_ROW_BOX_SPELL = new HashMap<>() {{
            put(Row.PLAYER1_CLOSE_COMBAT, player1CloseCombatSpell);
            put(Row.PLAYER1_RANGED, player1RangedSpell);
            put(Row.PLAYER1_SIEGE, player1SiegeSpell);
            put(Row.PLAYER2_CLOSE_COMBAT, player2CloseCombatSpell);
            put(Row.PLAYER2_RANGED, player2RangedSpell);
            put(Row.PLAYER2_SIEGE, player2SiegeSpell);
        }};

        game = Game.getCurrentGame();
        if (game == null) {
            Deck deck1 = new Deck();
            Deck deck2 = new Deck();
            deck1.setFaction(Faction.REALMS_NORTHERN);
            deck2.setFaction(Faction.MONSTER);
            deck1.addCards(deck1.getFaction().getAllCards());
            deck2.addCards(deck2.getFaction().getAllCards());
            deck1.setLeader(RealmsNorthernLeaders.KING_OF_TEMERIA.getLeader());
            deck2.setLeader(MonstersLeaders.BRINGER_OF_DEATH.getLeader());

            User user1 = new User("username1", "nickname1", "email1", "password1");
            User user2 = new User("username2", "nickname2", "email2", "password2");
            user1.setDeck(deck1);
            user2.setDeck(deck2);
            game = new Game(user1, user2);
            Game.setCurrentGame(game);
        }
        game.initializeGameObjects();
        player1NameLabel.setText(game.getPlayer1().getUsername());
        player2NameLabel.setText(game.getPlayer2().getUsername());
        initializeCards();
        setupLeaderCards();
        showVetoOverlay(); // Show the veto overlay at the beginning of the game
        startTurn();

        if (game.isOnline()) {
            exitSave.setVisible(false);
            exit.setVisible(false);
            quit.setVisible(true);
        } else {
            exitSave.setVisible(true);
            exit.setVisible(true);
            quit.setVisible(false);
        }

        if (game.isPlayer1Turn()) {
            displayMessage("Turn of Player 1");
        } else {
            displayMessage("Turn of Player 2");
        }

        startServerListener();
    }

    private void setupBackgroundMusic() {
        Media media = new Media(getClass().getResource("/media/Ramin-Djawadi-Finale-128.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
    }

    private void togglePauseMenu() {
        boolean isVisible = pauseMenu.isVisible();
        pauseMenu.setVisible(!isVisible);
    }

    private void initializeCards() {
        game.getPlayer1Deck().forEach(this::createCardView);
        game.getPlayer1InHandCards().forEach(this::createCardView);
        game.getPlayer1GraveyardCards().forEach(this::createCardView);
        game.getPlayer1LeaderCard().setSmallImage();
        game.getPlayer2Deck().forEach(this::createCardView);
        game.getPlayer2InHandCards().forEach(this::createCardView);
        game.getPlayer2GraveyardCards().forEach(this::createCardView);
        game.getPlayer2LeaderCard().setSmallImage();
    }

    private void setupCardsOnBoard() {
        player1CloseCombat.getChildren().clear();
        player2CloseCombat.getChildren().clear();
        player1Ranged.getChildren().clear();
        player2Ranged.getChildren().clear();
        player1Siege.getChildren().clear();
        player2Siege.getChildren().clear();
        weather.getChildren().clear();
        player1CloseCombatSpell.getChildren().clear();
        player2CloseCombatSpell.getChildren().clear();
        player1RangedSpell.getChildren().clear();
        player2RangedSpell.getChildren().clear();
        player1SiegeSpell.getChildren().clear();
        player2SiegeSpell.getChildren().clear();

        for (Card card : game.getInGameCards()) {
            getRowBox(card).getChildren().add(card);
        }
    }

    private void setupLeaderCards() {
        player1Leader.getChildren().clear();
        player2Leader.getChildren().clear();

        Leader player1LeaderCard = game.getPlayer1LeaderCard();
        player1Leader.getChildren().add(player1LeaderCard);
        player1LeaderCard.setOnMouseClicked(event -> showLeaderCardOverlay(player1LeaderCard));

        Leader player2LeaderCard = game.getPlayer2LeaderCard();
        player2Leader.getChildren().add(player2LeaderCard);
        player1LeaderCard.setOnMouseClicked(event -> showLeaderCardOverlay(player2LeaderCard));
    }

    private void setupCardsInHand() {
        player1Hand.getChildren().clear();
        player2Hand.getChildren().clear();

        player1Hand.getChildren().addAll(game.getPlayer1InHandCards());
        player2Hand.getChildren().addAll(game.getPlayer2InHandCards());

        for (Card card : CardsPlace.IN_HAND.getPlayerCards(game)) {
            card.setOnMouseClicked(event -> {
                System.out.println("clicked");
                selectCard(card);
            });
        }
    }

    private void createCardView(Card card) {
        card.setSmallImage();
        card.setPowerText();
    }

    public void updateScore() {
        player1ScoreLabel.setText("Score: " + game.getPlayer1Points());
        player2ScoreLabel.setText("Score: " + game.getPlayer2Points());
    }

    private void showVetoOverlay() {
        VBox overlay = new VBox(10);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: white; -fx-padding: 10;");
        overlay.setPrefSize(400, 300);

        Text instruction = new Text("Veto up to two cards from your hand.");

        HBox handCards = new HBox(10);
        for (Card card : game.getPlayer1InHandCards()) {
            Button cardButton = new Button(card.getName());
            cardButton.setOnAction(event -> vetoCard(card));
            handCards.getChildren().add(cardButton);
        }

        Button passButton = new Button("Pass");
        passButton.setOnAction(event -> passVeto());

        overlay.getChildren().addAll(instruction, handCards, passButton);
        overlayPane.getChildren().add(overlay);
        overlayPane.setVisible(true);
    }

    private void vetoCard(Card card) {
        game.getPlayer1InHandCards().remove(card);
        Card newCard = game.getPlayer1Deck().get(new Random().nextInt(game.getPlayer1Deck().size()));
        game.getPlayer1InHandCards().add(newCard);
        setupCardsInHand();
    }

    private void passVeto() {
        // nextTurn(); // ridam dahane in khate code
        overlayPane.setVisible(false);
        overlayPane.getChildren().clear();
    }

    private void nextTurn() throws SQLException, IOException {
        game.switchSides();
        startTurn();
        if (game.isPlayer1Turn()) {
            displayMessage("Turn of " + game.getPlayer1().getUsername());
        } else {
            displayMessage("Turn of " + game.getPlayer2().getUsername());
        }
        if (game.isOnline()) {
            DatabaseConnection.updateGame(game);
            String player = game.isPlayer1Turn() ? game.getPlayer1().getUsername() : game.getPlayer2().getUsername();
            App.getServerConnection().sendMessage(player, User.getCurrentUser().getUsername() + " made move");
        }
    }

    private void startTurn() {
        updateScore();
        clearHighlights(); // Clear any row highlights, reset onMouseClick function of both cards and rows in game
        setupCardsInHand();
        setupCardsOnBoard();
    }

    private void selectCard(Card card) {
        selectedCard = card;

        // Highlighting and setting mouse handler for possible rows
        clearHighlights();
        setupCardsInHand();
        setupCardsOnBoard();
        boolean isPlayer1 = game.isPlayer1Turn() ^ card.getAbility() instanceof Spy;
        try {
            highlightRow(GET_ROW_BOX.get(card.getType().getRow(isPlayer1)),
                    card.getType().getRow(isPlayer1), card);
            if (card.getType() == Type.AGILE_UNIT) {
                if (isPlayer1) {
                    highlightRow(player1Ranged, Row.PLAYER1_RANGED, card);
                } else {
                    highlightRow(player2Ranged, Row.PLAYER2_RANGED, card);
                }
            }
        } catch (UnsupportedOperationException e) {
            if (card.getType() == Type.SPELL) {
                if (isPlayer1) {
                    if (game.canPlay(card, Row.PLAYER1_CLOSE_COMBAT)) {
                        highlightRow(player1CloseCombatSpell, Row.PLAYER1_CLOSE_COMBAT, card);
                    }
                    if (game.canPlay(card, Row.PLAYER1_RANGED)) {
                        highlightRow(player1RangedSpell, Row.PLAYER1_RANGED, card);
                    }
                    if (game.canPlay(card, Row.PLAYER1_SIEGE)) {
                        highlightRow(player1SiegeSpell, Row.PLAYER1_SIEGE, card);
                    }
                } else {
                    if (game.canPlay(card, Row.PLAYER2_CLOSE_COMBAT)) {
                        highlightRow(player2CloseCombatSpell, Row.PLAYER2_CLOSE_COMBAT, card);
                    }
                    if (game.canPlay(card, Row.PLAYER2_RANGED)) {
                        highlightRow(player2RangedSpell, Row.PLAYER2_RANGED, card);
                    }
                    if (game.canPlay(card, Row.PLAYER2_SIEGE)) {
                        highlightRow(player2SiegeSpell, Row.PLAYER2_SIEGE, card);
                    }
                }
            } else if (card.getType() == Type.DECOY) { // kodoom ghashangtare? code ye khati paiin ya kode bala?
                (isPlayer1 ?
                        Stream.concat(player1CloseCombat.getChildren().stream(),
                                Stream.concat(player1Ranged.getChildren().stream(), player1Siege.getChildren().stream())) :
                        Stream.concat(player2CloseCombat.getChildren().stream(),
                                Stream.concat(player2Ranged.getChildren().stream(), player2Siege.getChildren().stream()))
                ).filter(inGameCard -> Ability.canBeAffected((Card) inGameCard))
                        .forEach(inGameCard -> highlightCard((Card) inGameCard, card));
            }
        }
    }

    private void highlightRow(HBox rowBox, Row row, Card card) {
        rowBox.setStyle("-fx-background-color: rgba(255, 255, 150, 0.2);");
        rowBox.setOnMouseClicked(rowClickEvent -> {
            try {
                playCard(card, row);
            } catch (IllegalArgumentException e) {
                System.out.println("" + rowBox + row + card.getName());
                throw e;
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void highlightCard(Card inGameCard, Card card) {
        inGameCard.setStroke();
        inGameCard.setOnMouseClicked(rowClickEvent -> {
            ((Decoy) card.getAbility()).setReturnCard(inGameCard);
            try {
                playCard(card, inGameCard.getRow());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println(card.getType() + card.getName() + inGameCard.getRow());
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void clearHighlights() { // removes every highlight and removes onMouseClick of every card and row boxes
        getAllRowBoxes().forEach(rowBox -> {
            rowBox.setStyle("");
            rowBox.setOnMouseClicked(null);
        });
        game.getAllCards().forEach(inGameCard -> {
            inGameCard.removeStroke();
            inGameCard.setOnMouseClicked(null);
        });
    }

    private void playCard(Card card, Row row) throws SQLException, IOException {
        if (row.isPlayer1() ^ card.getAbility() instanceof Spy) {
            if (!game.player1PlayCard(card, row)) {
                throw new IllegalArgumentException("card cannot be played");
            }
        } else {
            if (!game.player2PlayCard(card, row)) {
                throw new IllegalArgumentException("card cannot be played");
            }
        }
        game.calculatePoints();
        nextTurn();
    }

    private void showLeaderCardOverlay(Leader leaderCard) {
        VBox overlay = new VBox(10);
        overlay.setAlignment(Pos.TOP_LEFT);
        overlay.setStyle("-fx-background-color: white; -fx-padding: 10;");
        overlay.setPrefSize(200, 400);

        Text name = new Text(leaderCard.getName());
        Text description = new Text(leaderCard.getDescription());

        overlay.getChildren().addAll(name, description);
        overlayPane.getChildren().add(overlay);
        overlayPane.setVisible(true);
    }

    private void showHandCardOverlay(Card handCard) {  //TODO
        VBox overlay = new VBox(10);
        overlay.setAlignment(Pos.TOP_RIGHT);
        overlay.setStyle("-fx-background-color: white; -fx-padding: 10;");
        overlay.setPrefSize(200, 400);

        Text name = new Text(handCard.getName());
        Text description = new Text(handCard.getDescription().toString());

        overlay.getChildren().addAll(name, description);
        overlayPane.getChildren().add(overlay);
        overlayPane.setVisible(true);
    }

    public void displayMessage(String message) {
        showOverlayMessage(message);
        // Hide message after a few seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> hideOverlayMessage());
        pause.play();
    }

    private void showRowOverlay(List<Card> rowCards) {
        VBox overlay = new VBox(10);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: white; -fx-padding: 10;");
        overlay.setPrefSize(400, 600);

        for (Card card : rowCards) {
            Text cardInfo = new Text(card.getName() + ": " + card.getDescription().toString());
            overlay.getChildren().add(cardInfo);
        }

        overlayPane.getChildren().add(overlay);
        overlayPane.setVisible(true);
    }

    private HBox getRowBox(Card card) {
        return Objects.requireNonNull(card.getType() == Type.SPELL ?
                GET_ROW_BOX_SPELL.get(card.getRow()) : GET_ROW_BOX.get(card.getRow()));
    }

    private List<HBox> getAllRowBoxes() {
        return Arrays.asList(player1CloseCombat, player1CloseCombatSpell, player1Ranged, player1RangedSpell,
                player1Siege, player1SiegeSpell, player2CloseCombat, player2CloseCombatSpell,
                player2Ranged, player2RangedSpell, player2Siege, player2SiegeSpell, weather);
    }

    public void showOverlayMessage(String message) {
        overlayMessage.setText(message);
        overlayMessage.setVisible(true);
    }

    public void hideOverlayMessage() {
        overlayMessage.setVisible(false);
    }

    @FXML
    public void handleQuit(ActionEvent actionEvent) {
        try {
            User player;
            if (game.isPlayer1Turn()) {
                player = game.getPlayer2();
            } else {
                player = game.getPlayer1();
            }

            game.setStatus(Game.GameStatus.COMPLETED);
            game.setWinner(player);
            DatabaseConnection.updateGame(game);
            hideOverlayMessage();
            game.getPlayer1().setHighScore(game.getPlayer1Points() + game.getPlayer1().getHighScore());
            game.getPlayer2().setHighScore(game.getPlayer2Points() + game.getPlayer2().getHighScore());

            updateUserScore(game.getPlayer1());
            updateUserScore(game.getPlayer2());

            Game.setCurrentGame(null);
            App.loadScene(Menu.MAIN_MENU.getPath());

        } catch (SQLException e) {
            showAlert("Error", "Failed to end game", "An error occurred while ending the game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSaveGameAndExit(ActionEvent actionEvent) throws SQLException {
        DatabaseConnection.updateGame(game);
        game.setStatus(Game.GameStatus.PENDING);

        App.loadScene(Menu.MAIN_MENU.getPath());
        hideOverlayMessage();
        Game.setCurrentGame(null);
    }

    @FXML
    public void handleEndGameWithoutSaving(ActionEvent actionEvent) {
        try {
            if (game != null) {
                int gameId = game.getID();
                System.out.println(gameId);
                DatabaseConnection.deleteGame(gameId);
                showAlert("Game ended without saving.");
                App.loadScene(Menu.MAIN_MENU.getPath());
                hideOverlayMessage();

            }
        } catch (SQLException e) {
            showAlert("Error ending game: " + e.getMessage());
        }
    }

    @FXML
    public void handleToggleSound(ActionEvent actionEvent) {
        if (isMute) {
            isMute = false;
            mediaPlayer.play();
        } else {
            isMute = true;
            mediaPlayer.stop();
        }
    }

    private void startServerListener() {
        new Thread(() -> {
            try {
                String input;
                while ((input = App.getServerConnection().getIn().readLine()) != null) {
                    handleServerEvent(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.endsWith("made move")) {
                startTurn();
            } else if (input.endsWith("ended game , you won!")) {
                Game.setCurrentGame(null);
                showAlert(input);
                App.loadScene(Menu.MAIN_MENU.getPath());
            }
        });
    }

}
