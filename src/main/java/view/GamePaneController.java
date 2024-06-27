package view;

import enums.Row;
import enums.cardsinformation.Faction;
import enums.leaders.MonstersLeaders;
import enums.leaders.SkelligeLeaders;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Deck;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GamePaneController implements Initializable {
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
    private VBox player1CloseCombat;
    @FXML
    private VBox player2CloseCombat;
    @FXML
    private VBox player1Ranged;
    @FXML
    private VBox player2Ranged;
    @FXML
    private VBox player1Siege;
    @FXML
    private VBox player2Siege;
    @FXML
    private VBox player1Score;
    @FXML
    private VBox player2Score;
    @FXML
    private VBox player1Graveyard;
    @FXML
    private VBox player2Graveyard;
    @FXML
    private VBox player1Leader;
    @FXML
    private VBox player2Leader;
    private Text messageDisplay = new Text();
    private Game game;
    private Card selectedCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.fitHeightProperty().bind(gamePane.heightProperty());
        background.fitWidthProperty().bind(gamePane.widthProperty());
        game = Game.getCurrentGame();
        if (game == null) {
            Deck deck1 = new Deck();
            Deck deck2 = new Deck();
            deck1.setFaction(Faction.SKELLIGE);
            deck2.setFaction(Faction.MONSTER);
            deck1.addCards(deck1.getFaction().getAllCards());
            deck2.addCards(deck2.getFaction().getAllCards());
            deck1.setLeader(SkelligeLeaders.CRACH_AN_CRAITE.getLeader());
            deck2.setLeader(MonstersLeaders.BRINGER_OF_DEATH.getLeader());

            User user1 = new User("username1", "nickname1", "email1", "password1");
            User user2 = new User("username2", "nickname2", "email2", "password2");
            user1.setDeck(deck1);
            user2.setDeck(deck2);
            game = new Game(user1, user2);
        }
        game.initializeGameObjects();
        setupGame();
        startTurn(); // Start with Player 1's turn
    }

    private void setupGame() {
        player1NameLabel.setText(game.getPlayer1().getUsername());
        player2NameLabel.setText(game.getPlayer2().getUsername());
        updateScore();
        setupCardsInHand();
        setupCardsOnBoard();
        setupLeaderCards();
        showVetoOverlay(); // Show the veto overlay at the beginning of the game
    }

    private void setupCardsInHand() {
        player1Hand.getChildren().clear();
        player2Hand.getChildren().clear();

        for (Card card : game.getPlayer1InHandCards()) {
            player1Hand.getChildren().add(createCardView(card, true));
        }

        for (Card card : game.getPlayer2InHandCards()) {
            player2Hand.getChildren().add(createCardView(card, false));
        }
    }

    private Card createCardView(Card card, boolean isPlayer1) {
        card.setOnMouseClicked(event -> {
            if (card.equals(game.getPlayer1LeaderCard()) || card.equals(game.getPlayer2LeaderCard())) {
                showLeaderCardOverlay(card);
            } else if (game.isPlayer1Turn() == isPlayer1) {
                selectCard(card, isPlayer1);
                showHandCardOverlay(card);
                // Listen for clicks on row areas to place the card
                player1CloseCombat.setOnMouseClicked(rowClickEvent -> placeCard(card, Row.PLAYER1_CLOSE_COMBAT));
                player1Ranged.setOnMouseClicked(rowClickEvent -> placeCard(card, Row.PLAYER1_RANGED));
                player1Siege.setOnMouseClicked(rowClickEvent -> placeCard(card, Row.PLAYER1_SIEGE));
                player2CloseCombat.setOnMouseClicked(rowClickEvent -> placeCard(card, Row.PLAYER2_CLOSE_COMBAT));
                player2Ranged.setOnMouseClicked(rowClickEvent -> placeCard(card, Row.PLAYER2_RANGED));
                player2Siege.setOnMouseClicked(rowClickEvent -> placeCard(card, Row.PLAYER2_SIEGE));
            }
        });
        return card;
    }

    private void placeCard(Card card, Row row) {
        playCard(card, row);
        clearRowHighlights(); // Clear any row highlights after placing the card
    }

    private void setupCardsOnBoard() {
        player1CloseCombat.getChildren().clear();
        player2CloseCombat.getChildren().clear();
        player1Ranged.getChildren().clear();
        player2Ranged.getChildren().clear();
        player1Siege.getChildren().clear();
        player2Siege.getChildren().clear();

        for (Card card : game.getInGameCards()) {
            VBox targetBox = getRowBox(card.getRow());
            if (targetBox != null) {
                targetBox.getChildren().add(card);
            }
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

    public void updateScore() {
        player1ScoreLabel.setText("Score: " + game.getPlayer1Points());
        player2ScoreLabel.setText("Score: " + game.getPlayer2Points());
    }

    private VBox getRowBox(Row row) {
        switch (row) {
            case PLAYER1_CLOSE_COMBAT:
                return player1CloseCombat;
            case PLAYER2_CLOSE_COMBAT:
                return player2CloseCombat;
            case PLAYER1_RANGED:
                return player1Ranged;
            case PLAYER2_RANGED:
                return player2Ranged;
            case PLAYER1_SIEGE:
                return player1Siege;
            case PLAYER2_SIEGE:
                return player2Siege;
            default:
                return null;
        }
    }

    private void selectCard(Card card, boolean isPlayer1) {
        if (game.isPlayer1Turn() == isPlayer1) {
            selectedCard = card;
            highlightPossibleRows(card);
        }
    }

    private void highlightPossibleRows(Card card) {
        clearRowHighlights();
        Row cardRow = card.getRow();
        if (cardRow.isCloseCombat()) {
            highlightRow(player1CloseCombat, player2CloseCombat);
        }
        if (cardRow.isRanged()) {
            highlightRow(player1Ranged, player2Ranged);
        }
        if (cardRow.isSiege()) {
            highlightRow(player1Siege, player2Siege);
        }
    }

    private void highlightRow(VBox player1Row, VBox player2Row) {
        if (game.isPlayer1Turn()) {
            player1Row.setStyle("-fx-background-color: rgba(0, 255, 0, 0.3);");
        } else {
            player2Row.setStyle("-fx-background-color: rgba(0, 255, 0, 0.3);");
        }
    }

    private void clearRowHighlights() {
        player1CloseCombat.setStyle("");
        player2CloseCombat.setStyle("");
        player1Ranged.setStyle("");
        player2Ranged.setStyle("");
        player1Siege.setStyle("");
        player2Siege.setStyle("");
    }

    private void playCard(Card card, Row row) {
        clearRowHighlights(); // Clear any row highlights before placing the card
        if (row.isPlayer1()) {
            game.player1PlayCard(card, row);
        } else {
            game.player2PlayCard(card, row);
        }
        game.calculatePoints();
        setupCardsOnBoard();
        setupCardsInHand();
        updateScore(); // Update scores after playing a card
        nextTurn();
    }

    private void nextTurn() {
        switchSides();
    }

    private void switchSides() {
        boolean isPlayer1Turn = game.isPlayer1Turn();
        if (isPlayer1Turn) {
            // Enable Player 2's hand and disable Player 1's hand
            for (Card card : player2Hand.getChildren().filtered(node -> node instanceof Card).toArray(Card[]::new)) {
                card.setDisable(false);
            }
            for (Card card : player1Hand.getChildren().filtered(node -> node instanceof Card).toArray(Card[]::new)) {
                card.setDisable(true);
            }
            displayMessage("Turn of Player 2");
        } else {
            // Enable Player 1's hand and disable Player 2's hand
            for (Card card : player1Hand.getChildren().filtered(node -> node instanceof Card).toArray(Card[]::new)) {
                card.setDisable(false);
            }
            for (Card card : player2Hand.getChildren().filtered(node -> node instanceof Card).toArray(Card[]::new)) {
                card.setDisable(true);
            }
            displayMessage("Turn of Player 1");
        }
        game.setCurrentPlayer(isPlayer1Turn ? game.getPlayer2() : game.getPlayer1());
    }

    private void showLeaderCardOverlay(Card leaderCard) {
        VBox overlay = new VBox(10);
        overlay.setAlignment(Pos.TOP_LEFT);
        overlay.setStyle("-fx-background-color: white; -fx-padding: 10;");
        overlay.setPrefSize(200, 400);

        Text name = new Text(leaderCard.getName());
        Text description = new Text(leaderCard.getDescription().toString());

        overlay.getChildren().addAll(name, description);
        overlayPane.getChildren().add(overlay);
        overlayPane.setVisible(true);
    }

    private void showHandCardOverlay(Card handCard) {
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
        nextTurn();
        overlayPane.setVisible(false);
        overlayPane.getChildren().clear();
    }

    public void displayMessage(String message) {
        messageDisplay.setText(message);
        messageDisplay.setVisible(true);
        // Hide message after a few seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> messageDisplay.setVisible(false));
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

    public void startTurn() {
        game.setCurrentPlayer(game.getPlayer1());
    }
}
