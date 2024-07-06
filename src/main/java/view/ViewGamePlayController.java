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
import javafx.geometry.Pos;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

import static util.DatabaseConnection.updateUserScore;

public class ViewGamePlayController implements Initializable, ServerConnection.ServerEventListener {
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

    private boolean fromSaved = false;

    public static Game game;
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

        player1NameLabel.setText(game.getPlayer1().getUsername());
        player2NameLabel.setText(game.getPlayer2().getUsername());
        initializeCards();
        setupLeaderCards();
        updateScene();

        App.getServerConnection().addMessageListener(this);

        App.getStage().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePauseMenu();
            }
        });

        if (game.isPlayer1Turn()) {
            displayMessage("Turn of Player 1");
        } else {
            displayMessage("Turn of Player 2");
        }

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

        Leader player2LeaderCard = game.getPlayer2LeaderCard();
        player2Leader.getChildren().add(player2LeaderCard);
    }

    private void setupCardsInHand() {
        player1Hand.getChildren().clear();
        player2Hand.getChildren().clear();

        player1Hand.getChildren().addAll(game.getPlayer1InHandCards());
        player2Hand.getChildren().addAll(game.getPlayer2InHandCards());
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

    public void nextTurn() {
        if (game.isPlayer1Turn()) {
            displayMessage("Turn of " + game.getPlayer1().getUsername());
        } else {
            displayMessage("Turn of " + game.getPlayer2().getUsername());
        }
    }

    public void updateScene() {
        updateScore();
        setupCardsInHand();
        setupCardsOnBoard();
        game.getAllCards().forEach(Card::setPowerText);
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

    public void showOverlayMessage(String message) {
        overlayMessage.setText(message);
        overlayMessage.setVisible(true);
    }

    public void hideOverlayMessage() {
        overlayMessage.setVisible(false);
    }

    @Override
    public void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.startsWith("online game move made")) {
                if (Integer.parseInt(input.split(" ")[4]) == game.getID()) {
                    updateScene();
                }
            } else if (input.startsWith("game disconnected")) {
                if (Integer.parseInt(input.split(" ")[2]) == game.getID()) {
                    game = null;
                    App.loadScene(Menu.ONGOING_GAMES_MENU.getPath());
                }
            }
        });
    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }

    @FXML
    public void handleQuit(ActionEvent actionEvent) {
        game = null;
        App.loadScene(Menu.ONGOING_GAMES_MENU.getPath());
    }
}
