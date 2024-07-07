package view;

import enums.Menu;
import enums.Row;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Type;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.App;
import model.Game;
import model.User;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.Decoy;
import model.abilities.instantaneousabilities.Spy;
import model.card.Card;
import model.card.Leader;
import util.DatabaseConnection;
import util.ServerConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

import static util.DatabaseConnection.updateUserScore;
// TODO use leader ability (taskResult="leader")
// TODO pass round (taskResult="pass")
// TODO show players name, avatar, faction, etc.
public class GamePaneController implements Initializable, ServerConnection.ServerEventListener {

    @FXML
    public Button makePublic;
    @FXML
    private Text player1TotalScore, player2TotalScore, player1CloseCombatTotalScore, player1RangedTotalScore,
            player1SiegeTotalScore, player2SiegeTotalScore, player2RangedTotalScore, player2CloseCombatTotalScore;
    @FXML
    private Button passButton;
    @FXML
    private VBox pauseMenu;
    @FXML
    private Button exit;
    @FXML
    private Button exitSave;
    @FXML
    private Button quit;
    @FXML
    private VBox cardDisplayVBox;
    @FXML
    private ImageView cardImageView;
    @FXML
    private Text cardDescriptionText;
    @FXML
    private Text cardNumText;
    @FXML
    private VBox chooseDisplayVBox;
    @FXML
    private ImageView chooseImageView;
    @FXML
    private Text chooseDescriptionText;
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
    private HBox weather;
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

    private MediaPlayer mediaPlayer; // Assuming this handles your background music
    private boolean isMute;
    private HashMap<Row, HBox> GET_ROW_BOX, GET_ROW_BOX_SPELL;


    private Game game;
    private Card selectedCard;

    private int currentIndex;

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
        game.setGamePaneController(this);
        player1NameLabel.setText(game.getPlayer1().getUsername());
        player2NameLabel.setText(game.getPlayer2().getUsername());
        initializeCards();
        setupLeaderCards();

        game.startGameThread(); // it handles starting from saved
        updateScene();

        App.getStage().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePauseMenu();
            }
        });

        if (game.isOnline()) {
            exitSave.setVisible(false);
            exit.setVisible(false);
            quit.setVisible(true);
            makePublic.setVisible(true);
            App.getServerConnection().addMessageListener(this);
        } else {
            exitSave.setVisible(true);
            exit.setVisible(true);
            quit.setVisible(false);
            makePublic.setVisible(false);
        }

        if (game.isPlayer1Turn()) {
            displayMessage("Turn of Player 1");
        } else {
            displayMessage("Turn of Player 2");
        }
    }

    private void setupBackgroundMusic() {
        mediaPlayer = Tools.getMediaPlayer("/media/Ramin-Djawadi-Finale-128.mp3");
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
    }

    private void togglePauseMenu() {
        boolean isVisible = pauseMenu.isVisible();
        pauseMenu.setVisible(!isVisible);
    }

    private void initializeCards() {
        game.getAllCards().forEach(this::createCardView);
        game.getPlayer1LeaderCard().setSmallImage();
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
        player2LeaderCard.setOnMouseClicked(event -> showLeaderCardOverlay(player2LeaderCard));
    }

    private void setupCardsInHand() {
        player1Hand.getChildren().clear();
        player2Hand.getChildren().clear();

        player1Hand.getChildren().addAll(game.getPlayer1InHandCards());
        player2Hand.getChildren().addAll(game.getPlayer2InHandCards());

        for (Card card : CardsPlace.IN_HAND.getPlayerCards(game)) {
            card.setOnMouseClicked(event -> selectCard(card));
        }
    }

    private void createCardView(Card card) {
        card.setSmallImage();
        card.setPowerText();
    }

    public void updateScore() {
        player1ScoreLabel.setText("Score: " + game.getPlayer1Points());
        player2ScoreLabel.setText("Score: " + game.getPlayer2Points());
        player1TotalScore.setText(Integer.toString(game.getPlayer1Points()));
        player2TotalScore.setText(Integer.toString(game.getPlayer2Points()));
        player1CloseCombatTotalScore.setText(Integer.toString(game.calculatePoints(row -> row == Row.PLAYER1_CLOSE_COMBAT)));
        player2CloseCombatTotalScore.setText(Integer.toString(game.calculatePoints(row -> row == Row.PLAYER2_CLOSE_COMBAT)));
        player1RangedTotalScore.setText(Integer.toString(game.calculatePoints(row -> row == Row.PLAYER1_RANGED)));
        player2RangedTotalScore.setText(Integer.toString(game.calculatePoints(row -> row == Row.PLAYER2_RANGED)));
        player1SiegeTotalScore.setText(Integer.toString(game.calculatePoints(row -> row == Row.PLAYER1_SIEGE)));
        player2SiegeTotalScore.setText(Integer.toString(game.calculatePoints(row -> row == Row.PLAYER2_SIEGE)));
    }

    @FXML
    private void handlePreviousCard() {
        if (currentIndex > 0) {
            currentIndex--;
            showCard();
        }
    }

    @FXML
    private void handleNextCard() {
        if (currentIndex < game.getCardChoices().size() - 1) {
            currentIndex++;
            showCard();
        }
    }

    private void showCard() {
        Card card = game.getCardChoices().get(currentIndex);
        chooseImageView.setImage(Tools.getImage(card.getImagePath()));
        chooseDescriptionText.setText(card.getDescription().getDescription());
    }

    public void showChooseOverlay(boolean canPass) {
        chooseDisplayVBox.setVisible(true);
        overlayPane.setVisible(true);
        passButton.setVisible(canPass);

        currentIndex = 0;
        showCard();
        chooseImageView.setOnMouseClicked(event -> {
            overlayPane.setVisible(false);
            chooseDisplayVBox.setVisible(false);
            sendTaskResult("chose " + currentIndex);
        });
    }

    @FXML
    private void passChoose() {
        // nextTurn(); // ridam dahane in khate code
        overlayPane.setVisible(false);
        chooseDisplayVBox.setVisible(false);
        sendTaskResult("chose " + "null");
    }

    public void nextTurn() {
        if (game.isPlayer1Turn()) {
            displayMessage("Turn of " + game.getPlayer1().getUsername());
        } else {
            displayMessage("Turn of " + game.getPlayer2().getUsername());
        }
    }

    public void updateScene() {
        updateScore();
        clearHighlights(); // Clear any row highlights, reset onMouseClick function of both cards and rows in game
        setupCardsInHand();
        setupCardsOnBoard();
        game.getAllCards().forEach(Card::setPowerText);
    }

    private void selectCard(Card card) {
        selectedCard = card;

        // Highlighting and setting mouse handler for possible rows
        clearHighlights();
        setupCardsInHand();
        setupCardsOnBoard();
        showBigImage(card.getImagePath(), card.getDescription().getDescription(), true, card, null);
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
            sendTaskResult("play " + CardsPlace.IN_HAND.getPlayerCards(game).indexOf(card) + " " +
                    row.toString());
        });
    }

    private void highlightCard(Card inGameCard, Card card) {
        inGameCard.setStroke();
        inGameCard.setOnMouseClicked(rowClickEvent -> {
            ((Decoy) card.getAbility()).setReturnCard(inGameCard);
            sendTaskResult("PlayCard " + CardsPlace.IN_HAND.getPlayerCards(game).indexOf(card) + " " +
                    inGameCard.getRow().toString() + " " + game.getInGameCards().indexOf(card));
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

    private void showLeaderCardOverlay(Leader leaderCard) {
        showBigImage(leaderCard.getImagePath(), leaderCard.getDescription(), false, null, leaderCard);
    }

    public void displayMessage(String message) {
        showOverlayMessage(message);
        // Hide message after a few seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> hideOverlayMessage());
        pause.play();
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

    public void showEndScreenOverlay() {
        // TODO
    }

    public void showChooseWhoStartsOverlay() { // Scoiatael faction has this ability I don't write it in Game class yet
        // TODO
    }

    @FXML
    public void handleQuit() {
        try {
            User player;
            if (game.isPlayer1Turn()) {
                player = game.getPlayer2();
            } else {
                player = game.getPlayer1();
            }

            game.setStatus(Game.GameStatus.COMPLETED);
            // game.setWinner(player);
            hideOverlayMessage();
            game.getPlayer1().setHighScore(game.getPlayer1Points() + game.getPlayer1().getHighScore());
            game.getPlayer2().setHighScore(game.getPlayer2Points() + game.getPlayer2().getHighScore());
            DatabaseConnection.updateGame(game);

            updateUserScore(game.getPlayer1());
            updateUserScore(game.getPlayer2());

            Game.setCurrentGame(null);
            App.loadScene(Menu.MAIN_MENU.getPath());

        } catch (SQLException e) {
            Tools.showAlert("Error", "Failed to end game", "An error occurred while ending the game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSaveGameAndExit() throws SQLException {
        game.setStatus(Game.GameStatus.PENDING);
        DatabaseConnection.updateGame(game);
        App.loadScene(Menu.MAIN_MENU.getPath());
        hideOverlayMessage();
        Game.setCurrentGame(null);
    }

    @FXML
    public void handleEndGameWithoutSaving() {
        try {
            if (game != null) {
                int gameId = game.getID();
                System.out.println(gameId);
                DatabaseConnection.deleteGame(gameId);
                Tools.showAlert("Game ended without saving.");
                App.loadScene(Menu.MAIN_MENU.getPath());
                hideOverlayMessage();

            }
        } catch (SQLException e) {
            Tools.showAlert("Error ending game: " + e.getMessage());
        }
    }

    @FXML
    public void handleToggleSound() {
        if (isMute) {
            isMute = false;
            mediaPlayer.play();
        } else {
            isMute = true;
            mediaPlayer.stop();
        }
    }

    @Override
    public void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.startsWith("online game move made ")) {;
                try {
                    game = DatabaseConnection.getGame(Integer.parseInt(input.split(" ")[4]));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Game.setCurrentGame(game);
                doTask();
            } else if (input.endsWith("Game ended by ")) {
                Game.setCurrentGame(null);
                Tools.showAlert(input + " You won!");
                App.loadScene(Menu.MAIN_MENU.getPath());
            }
        });
    }

    public void hideCardDisplay() {
        cardDisplayVBox.setVisible(false);
        cardNumText.setVisible(false);
    }

    private void showBigImage(String imagePath, String description, boolean isCard, Card card, Leader leader) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }
        cardImageView.setImage(Tools.getImage(imagePath));
        cardDescriptionText.setText(description);
        cardDisplayVBox.setVisible(true);
        cardDisplayVBox.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (!cardDisplayVBox.contains(event.getScreenX(), event.getScreenY())) {
                hideCardDisplay();
            }
        });
    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }

    public void doTask() {
        if (game.isOnline() && (game.getPlayer1().equals(User.getCurrentUser()) ^ game.isPlayer1Turn())){
            return;
        }
        if (game.getTask().equals("show end screen")) {
            showEndScreenOverlay();
        } else if (game.getTask().equals("play")) {
            nextTurn();
        } else if (game.getTask().equals("choose false")) {
            showChooseOverlay(false);
        } else if (game.getTask().equals("choose true")) {
            showChooseOverlay(true);
        } else {
            Tools.showAlert("invalid task: " + game.getTask());
        }
        updateScene();
    }

    public void sendTaskResult(String taskResult) {
        if (game.isOnline()) {
            App.getServerConnection().sendMessage("run task:" + taskResult + ":" + game.getID());
        } else {
            game.receiveTaskResult(taskResult, null);
        }
    }

    @FXML
    public void handleMakePublic(ActionEvent actionEvent) {
        if (Game.getCurrentGame().isPublic()) {
            makePublic.setText("Make game Online");
            App.getServerConnection().sendMessage("disconnect game:" + game.getID());
            Game.getCurrentGame().setPublic(false);
        } else {
            makePublic.setText("Make game Offline");
            Game.getCurrentGame().setPublic(true);
        }
        DatabaseConnection.updateGamePublicity(Game.getCurrentGame().isPublic(), Game.getCurrentGame().getID());
    }
}
