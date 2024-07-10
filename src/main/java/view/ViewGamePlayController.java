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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
import static view.Tools.showAlert;

// TODO use leader ability (taskResult="leader")
// TODO pass round (taskResult="pass")
// TODO show players name, avatar, faction, etc.
public class ViewGamePlayController implements Initializable, ServerConnection.ServerEventListener {

    private static final Image GEM_ON = Tools.getImage("/gwentImages/img/icons/icon_gem_on.png");
    private static final Image GEM_OFF = Tools.getImage("/gwentImages/img/icons/icon_gem_off.png");
    private static final String CHEAT_TEXT = "ang";
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
    public static Game game;
    private Card selectedCard;
    private int currentIndex;
    @FXML
    private VBox cheatMenu;
    private StringBuilder writtenText = new StringBuilder();
    @FXML
    private Label overlayMessage;

    public static VBox createEndScreenVBox(int roundNumber, int firstNumber, int secondNumber) {
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

        player1NameLabel.setText(game.getPlayer1().getUsername());
        player2NameLabel.setText(game.getPlayer2().getUsername());
        player1FactionLabel.setText(game.getPlayer1Faction().getName());
        player2FactionLabel.setText(game.getPlayer2Faction().getName());
        player1Faction.setImage(game.getPlayer1Faction().getIcon());
        player2Faction.setImage(game.getPlayer2Faction().getIcon());

        initializeCards();
        setupLeaderCards();
        updateScene();

        App.getServerConnection().addMessageListener(this);

        if (game.isPlayer1Turn()) {
            displayMessage("Turn of Player 1");
        } else {
            displayMessage("Turn of Player 2");
        }
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
        updateScore();
        setupCardsInHand();
        setupCardsOnBoard();
        setupGraveyardCards();
        setupGemsAndCardNumber();
        game.getAllCards().forEach(this::createCardView);
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

    private void showLeaderCardOverlay(boolean player1Leader) {
        Leader leaderCard = player1Leader ? game.getPlayer1LeaderCard() : game.getPlayer2LeaderCard();
        showBigImage(leaderCard.getImagePath(), leaderCard.getDescription(), false, null, leaderCard);
    }

    @Override
    public void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.startsWith("online game move made ")) {
                System.out.println(Integer.parseInt(input.split(" ")[4]));
                if (Integer.parseInt(input.split(" ")[4]) == game.getID()) {
                    try {
                        game = DatabaseConnection.getGame(game.getID());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    updateScene();
                }
            } else if (input.startsWith("game disconnected")) {
                if (Integer.parseInt(input.split(" ")[2]) == game.getID()) {
                    game = null;
                    showAlert("game ended or made private");
                    Tools.loadScene(Menu.ONGOING_GAMES_MENU);
                }
            }
        });
    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }

    @FXML
    public void handleQuit() {
        game = null;
        Tools.loadScene(Menu.ONGOING_GAMES_MENU);
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

        // Add the event filter to the scene
        cardDisplayVBox.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            // Get the coordinates of the mouse event relative to the cardDisplayVBox
            double x = event.getScreenX();
            double y = event.getScreenY();

            // Check if the click is outside the bounds of cardDisplayVBox
            if (!cardDisplayVBox.localToScreen(cardDisplayVBox.getBoundsInLocal()).contains(x, y)) {
                hideCardDisplay();
            }
        });
    }

    @FXML
    private void endScreenExit() {
        Tools.loadScene(Menu.MAIN_MENU);
    }
}
