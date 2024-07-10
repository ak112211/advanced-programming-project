package view;

import enums.Menu;
import enums.Row;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Type;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.App;
import model.Game;
import model.RoundsInfo;
import model.RoundsInfo.Winner;
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

    private static final Image GEM_ON = Tools.getImage("/gwentImages/img/icons/icon_gem_on.png");
    private static final Image GEM_OFF = Tools.getImage("/gwentImages/img/icons/icon_gem_off.png");
    private static final String CHEAT_TEXT = "alo pedarsag";
    @FXML
    private ImageView endScreenStatusImage;
    @FXML
    private Label endScreenPlayer1NameLabel, endScreenPlayer2NameLabel, endScreenWinStatus;
    @FXML
    private VBox endScreenOverlay;
    @FXML
    private HBox endScreenVBoxes;

    @FXML
    private Label player1CardNumber, player2CardNumber;
    @FXML
    private ImageView player1Gem1, player1Gem2, player2Gem1, player2Gem2;
    @FXML
    private ImageView player1Faction, player2Faction;
    @FXML
    private Button makePublic;
    @FXML
    private Text player1TotalScore, player2TotalScore, player1CloseCombatTotalScore, player1RangedTotalScore,
            player1SiegeTotalScore, player2SiegeTotalScore, player2RangedTotalScore, player2CloseCombatTotalScore;
    @FXML
    private Label player1FactionLabel, player2FactionLabel;
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
    private Label player1NameLabel, player2NameLabel;
    @FXML
    private HBox player1Graveyard, player2Graveyard;
    @FXML
    private HBox player1Hand, player2Hand;
    @FXML
    private HBox player1CloseCombat, player2CloseCombat, player1Ranged, player2Ranged, player1Siege, player2Siege;
    @FXML
    private HBox player1CloseCombatSpell, player2CloseCombatSpell, player1RangedSpell, player2RangedSpell,
            player1SiegeSpell, player2SiegeSpell, weather;
    @FXML
    private VBox player1Score, player2Score;
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
    private VBox cheatMenu;
    private StringBuilder writtenText = new StringBuilder();
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
        player1FactionLabel.setText(game.getPlayer1Faction().getName());
        player2FactionLabel.setText(game.getPlayer2Faction().getName());
        player1Faction.setImage(game.getPlayer1Faction().getIcon());
        player2Faction.setImage(game.getPlayer2Faction().getIcon());
        background.setOnMouseClicked(event -> hideCardDisplay());

        initializeCards();
        setupLeaderCards();
        updateScene();

        game.startGameThread(); // it handles starting from saved

        App.getStage().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (cheatMenu.isVisible()) {
                    cheatMenu.setVisible(false);
                    overlayPane.setVisible(false);
                } else {
                    togglePauseMenu();
                }
            } else if (event.getCode().isWhitespaceKey() || event.getCode().isLetterKey()) {
                writtenText.append(event.getCode().getChar().toLowerCase());
                if (writtenText.toString().endsWith(CHEAT_TEXT) && (!game.isOnline() || game.isMyTurn())) {
                    cheatMenu.setVisible(true);
                    overlayPane.setVisible(true);
                }
            }
        });

        if (game.isOnline()) {
            exitSave.setVisible(false);
            exit.setVisible(false);
            quit.setVisible(true);
            makePublic.setVisible(true);
            App.getServerConnection().addMessageListener(this);
            if (game.isPublic()) {
                makePublic.setText("Make game Offline");
            }
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

    private void createCardView(Card card) {
        card.setSmallImage();
        card.setPowerText();
    }

    private void setupLeaderCards() {
        player1Leader.getChildren().clear();
        player2Leader.getChildren().clear();

        Leader player1LeaderCard = game.getPlayer1LeaderCard();
        player1Leader.getChildren().add(player1LeaderCard);
        player1LeaderCard.setOnMouseClicked(event -> showLeaderCardOverlay(true));

        Leader player2LeaderCard = game.getPlayer2LeaderCard();
        player2Leader.getChildren().add(player2LeaderCard);
        player2LeaderCard.setOnMouseClicked(event -> showLeaderCardOverlay(false));
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

    private void setupCardsInHand() {
        player1Hand.getChildren().clear();
        player2Hand.getChildren().clear();

        player1Hand.getChildren().addAll(game.getPlayer1InHandCards());
        player2Hand.getChildren().addAll(game.getPlayer2InHandCards());

        for (Card card : CardsPlace.IN_HAND.getPlayerCards(game)) {
            card.setOnMouseClicked(event -> selectCard(card));
        }
    }

    private void setupGraveyardCards() {
        player1Graveyard.getChildren().clear();
        player2Graveyard.getChildren().clear();

        player1Graveyard.getChildren().addAll(game.getPlayer1GraveyardCards());
        player2Graveyard.getChildren().addAll(game.getPlayer2GraveyardCards());

        game.getAllCards().forEach(inGameCard -> inGameCard.setRotate(0));
        game.getPlayer1GraveyardCards().forEach(card -> card.setRotate(-60));
        game.getPlayer2GraveyardCards().forEach(card -> card.setRotate(-60));
    }

    private void setupGemsAndCardNumber() {
        player1CardNumber.setText(Integer.toString(game.getPlayer1InHandCards().size()));
        if (game.getRoundsInfo().getPlayer1Hearts() >= 1) {
            player1Gem1.setImage(GEM_ON);
        } else {
            player1Gem1.setImage(GEM_OFF);
        }
        if (game.getRoundsInfo().getPlayer1Hearts() >= 2) {
            player1Gem2.setImage(GEM_ON);
        } else {
            player1Gem2.setImage(GEM_OFF);
        }

        player2CardNumber.setText(Integer.toString(game.getPlayer2InHandCards().size()));
        if (game.getRoundsInfo().getPlayer2Hearts() >= 1) {
            player2Gem1.setImage(GEM_ON);
        } else {
            player2Gem1.setImage(GEM_OFF);
        }
        if (game.getRoundsInfo().getPlayer2Hearts() >= 2) {
            player2Gem2.setImage(GEM_ON);
        } else {
            player2Gem2.setImage(GEM_OFF);
        }
    }

    public void updateScene() {
        clearHighlights(); // Clear any row highlights, reset onMouseClick function of both cards and rows in game
        hideCardDisplay();
        updateScore();
        setupCardsInHand();
        setupCardsOnBoard();
        setupGraveyardCards();
        setupGemsAndCardNumber();
        game.getAllCards().forEach(Card::setPowerText);
        if (game.isOnline()) {
            App.getServerConnection().sendMessage("update viewers:" + game.getID());
        }
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

    public void updateScore() {
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

    @FXML
    private void player1PassRound() {
        if (game.isPlayer1Turn()) {
            sendTaskResult("pass");
        }
    }

    @FXML
    private void player2PassRound() {
        if (!game.isPlayer1Turn()) {
            sendTaskResult("pass");
        }
    }

    public void nextTurn() {
        if (game.isPlayer1Turn()) {
            displayMessage("Turn of " + game.getPlayer1().getUsername());
        } else {
            displayMessage("Turn of " + game.getPlayer2().getUsername());
        }
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

    private void showLeaderCardOverlay(boolean player1Leader) {
        Leader leaderCard = player1Leader ? game.getPlayer1LeaderCard() : game.getPlayer2LeaderCard();
        showBigImage(leaderCard.getImagePath(), leaderCard.getDescription(), false, null, leaderCard);

        if (player1Leader == game.isPlayer1Turn() &&
                (player1Leader ? game.player1CanUseLeaderAbility() : game.player2CanUseLeaderAbility())) {
            cardImageView.setOnMouseClicked((event) -> sendTaskResult("leader"));
        }
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

    public void showChooseWhoStartsOverlay() { // Scoiatael faction has this ability I don't write it in Game class yet
        // TODO
    }

    @FXML
    public void handleQuit() {
        try {
            if (game.userIsPlayer1()) {
                game.getRoundsInfo().setWinner(RoundsInfo.Winner.PLAYER2);
            } else {
                game.getRoundsInfo().setWinner(RoundsInfo.Winner.PLAYER1);
            }

            game.setStatus(Game.GameStatus.COMPLETED);
            hideOverlayMessage();
            game.getPlayer1().setHighScore(game.getPlayer1Points() + game.getPlayer1().getHighScore());
            game.getPlayer2().setHighScore(game.getPlayer2Points() + game.getPlayer2().getHighScore());
            DatabaseConnection.updateGame(game);

            updateUserScore(game.getPlayer1());
            updateUserScore(game.getPlayer2());
            App.getServerConnection().sendMessage(game.getWinnerUser().getUsername() + ":ended game");
            App.getServerConnection().sendMessage("disconnect game:" + game.getID());
            Game.setCurrentGame(null);
            Tools.loadScene(Menu.MAIN_MENU);
        } catch (SQLException e) {
            Tools.showAlert("Error", "Failed to end game", "An error occurred while ending the game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSaveGameAndExit() throws SQLException {
        game.setStatus(Game.GameStatus.PENDING);
        DatabaseConnection.updateGame(game);
        Tools.loadScene(Menu.MAIN_MENU);
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
                Tools.loadScene(Menu.MAIN_MENU);
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
        if (input.startsWith("Move from ")) {
            Game newGame;
            try {
                newGame = DatabaseConnection.getGame(game.getID());
                assert newGame != null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (!game.hasFinishedVeto()) { // still in veto state
                System.out.println();
                assert game.getTask().equals("veto");
                game.setFinishedVeto(true);
                if (game.userIsPlayer1()) {
                    game.getPlayer2InHandCards().clear();
                    game.getPlayer2InHandCards().addAll(newGame.getPlayer2InHandCards());
                    game.getPlayer2Deck().clear();
                    game.getPlayer2Deck().addAll(newGame.getPlayer2Deck());
                } else {
                    game.getPlayer1InHandCards().clear();
                    game.getPlayer1InHandCards().addAll(newGame.getPlayer1InHandCards());
                    game.getPlayer1Deck().clear();
                    game.getPlayer1Deck().addAll(newGame.getPlayer1Deck());
                }
            } else {
                Game.setCurrentGame(newGame);
                newGame.setOnline(true);
                newGame.setSkipCode(true);
                newGame.setGamePaneController(this);
                newGame.startGameThread();
                game = newGame;
            }
            Platform.runLater(this::updateScene);
        } else if (input.startsWith("Game ended by ")) {
            Game.setCurrentGame(null);
            Platform.runLater(() -> {
                Tools.showAlert(input + " You won!");
                Tools.loadScene(Menu.MAIN_MENU);
            });
        }

    }

    public void hideCardDisplay() {
        cardDisplayVBox.setVisible(false);
        cardNumText.setVisible(false);
    }

    private void showBigImage(String imagePath, String description, boolean isCard, Card card, Leader leader) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }
        cardImageView.setOnMouseClicked(null);
        cardImageView.setImage(Tools.getImage(imagePath));
        cardDescriptionText.setText(description);
        cardDisplayVBox.setVisible(true);

    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }

    public void doTask() {
        switch (game.getTask()) {
            case "update" -> {
            }
            case "show end screen" -> showEndScreenOverlay();
            case "play" -> nextTurn();
            case "choose" -> showChooseOverlay(false);
            case "veto" -> showChooseOverlay(true);
            default -> Tools.showAlert("invalid task: " + game.getTask());
        }
        updateScene();
    }

    public void sendTaskResult(String taskResult) {
        game.receiveTaskResult(taskResult, User.getCurrentUser());
    }

    // cheats:

    @FXML
    public void handleMakePublic() {
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

    @FXML
    private void cheatGetRandomCard() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatGetRandomCard();
        updateScene();
    }

    @FXML
    private void cheatResetHearts() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatResetHearts();
        updateScene();
    }

    @FXML
    private void cheatClearWeather() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatClearWeather();
        updateScene();
    }

    @FXML
    private void cheatPlayScorch() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatPlayScorch();
        updateScene();
    }

    @FXML
    private void cheatResetGraveyardToDeck() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatResetGraveyardToDeck();
        updateScene();
    }

    @FXML
    private void cheatDoubleCards() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatDoubleCards();
        updateScene();
    }

    @FXML
    private void cheatUseLeaderAbility() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatUseLeaderAbility();
        updateScene();
    }

    @FXML
    private void cheatRemoveMyCards() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatRemoveMyCards();
        updateScene();
    }

    // End Game Overlay

    @FXML
    private void cheatRemoveEnemyCards() {
        if (game.isOnline() && !game.isMyTurn()) {
            return;
        }
        game.cheatRemoveEnemyCards();
        updateScene();
    }

    public void showEndScreenOverlay() {
        endScreenOverlay.setVisible(true);
        RoundsInfo roundsInfo = game.getRoundsInfo();
        endScreenVBoxes.getChildren().remove(1, endScreenVBoxes.getChildren().size());
        for (int i = 1; i < roundsInfo.getCurrentRound(); i++) {
            VBox vbox = createEndScreenVBox(i, roundsInfo.getPlayer1Score(i), roundsInfo.getPlayer2Score(i));
            endScreenVBoxes.getChildren().add(i, vbox);
        }
        endScreenPlayer1NameLabel.setText(game.getPlayer1().getUsername());
        endScreenPlayer2NameLabel.setText(game.getPlayer2().getUsername());
        Label adjustingLabel = new Label();
        adjustingLabel.setPrefWidth(((VBox) endScreenVBoxes.getChildren().getFirst()).getWidth());
        endScreenVBoxes.getChildren().add(adjustingLabel);

        assert roundsInfo.getWinner() != null;
        if (game.isOnline()) {
            if (roundsInfo.getWinner() == Winner.DRAW) {
                endScreenWinStatus.setText("Draw");
                endScreenStatusImage.setImage(Tools.getImage("/gwentImages/img/icons/end_draw.png"));
            } else if ((roundsInfo.getWinner() == Winner.PLAYER1) == game.userIsPlayer1()) {
                endScreenWinStatus.setText("Victory");
                endScreenStatusImage.setImage(Tools.getImage("/gwentImages/img/icons/end_win.png"));
            } else {
                endScreenWinStatus.setText("Defeat");
                endScreenStatusImage.setImage(Tools.getImage("/gwentImages/img/icons/end_lose.png"));
            }
        } else {
            if (roundsInfo.getWinner() == Winner.DRAW) {
                endScreenWinStatus.setText("Draw");
                endScreenStatusImage.setImage(Tools.getImage("/gwentImages/img/icons/end_draw.png"));
            } else if (roundsInfo.getWinner() == Winner.PLAYER1) {
                endScreenWinStatus.setText(game.getPlayer1().getUsername() + " Won");
                endScreenStatusImage.setImage(Tools.getImage("/gwentImages/img/icons/end_win.png"));
            } else {
                endScreenWinStatus.setText(game.getPlayer2().getUsername() + " Won");
                endScreenStatusImage.setImage(Tools.getImage("/gwentImages/img/icons/end_lose.png"));
            }
        }
        App.getServerConnection().sendMessage("disconnect game:" + game.getID());
    }

    public VBox createEndScreenVBox(int roundNumber, int firstNumber, int secondNumber) {
        // Create the VBox with spacing set to 40.0
        VBox vbox = new VBox(40);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);

        // Create the round label
        Label roundLabel = new Label("Round " + roundNumber);
        roundLabel.setTextFill(Color.WHITE);
        roundLabel.setFont(new Font("Arial", 18));

        // Create the labels for the numbers
        Label firstNumberLabel = new Label(String.valueOf(firstNumber));
        Label secondNumberLabel = new Label(String.valueOf(secondNumber));
        firstNumberLabel.setFont(new Font("Arial", 18));
        secondNumberLabel.setFont(new Font("Arial", 18));

        // Determine the colors based on the values of the numbers
        if (firstNumber > secondNumber) {
            firstNumberLabel.setTextFill(Color.YELLOW);
            secondNumberLabel.setTextFill(Color.WHITE);
        } else if (firstNumber < secondNumber) {
            firstNumberLabel.setTextFill(Color.WHITE);
            secondNumberLabel.setTextFill(Color.YELLOW);
        } else {
            firstNumberLabel.setTextFill(Color.WHITE);
            secondNumberLabel.setTextFill(Color.WHITE);
        }

        // Add the labels to the VBox
        vbox.getChildren().addAll(roundLabel, firstNumberLabel, secondNumberLabel);

        return vbox;
    }


    @FXML
    private void endScreenExit() {
        Tools.loadScene(Menu.MAIN_MENU);
    }
}
