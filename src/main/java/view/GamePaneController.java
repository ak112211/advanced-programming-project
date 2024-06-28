package view;

import enums.Row;
import enums.cards.RealmsNorthernCards;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import enums.leaders.MonstersLeaders;
import enums.leaders.RealmsNorthernLeaders;
import enums.leaders.SkelligeLeaders;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Deck;
import model.Game;
import model.User;
import model.abilities.instantaneousabilities.Decoy;
import model.abilities.instantaneousabilities.Spy;
import model.card.Card;
import model.card.Leader;

import java.net.URL;

import java.util.*;

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

    private HashMap<HBox, Row> GET_ROW;

    private HashMap<Row, HBox> GET_ROW_BOX;

    private Text messageDisplay = new Text();
    private Game game;
    private Card selectedCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GET_ROW = new HashMap<>() {{
            put(player1CloseCombat, Row.PLAYER1_CLOSE_COMBAT);
            put(player1Ranged, Row.PLAYER1_RANGED);
            put(player1Siege, Row.PLAYER1_SIEGE);
            put(player1CloseCombatSpell, Row.PLAYER1_CLOSE_COMBAT);
            put(player1RangedSpell, Row.PLAYER1_RANGED);
            put(player1SiegeSpell, Row.PLAYER1_SIEGE);
            put(player2CloseCombat, Row.PLAYER2_CLOSE_COMBAT);
            put(player2Ranged, Row.PLAYER2_RANGED);
            put(player2Siege, Row.PLAYER2_SIEGE);
            put(player2CloseCombatSpell, Row.PLAYER2_CLOSE_COMBAT);
            put(player2RangedSpell, Row.PLAYER2_RANGED);
            put(player2SiegeSpell, Row.PLAYER2_SIEGE);
        }};

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

        for (Card card : game.getInGameCards()) {
            HBox targetBox = GET_ROW_BOX.get(card.getRow());
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

    private void setupCardsInHand() {
        player1Hand.getChildren().clear();
        player2Hand.getChildren().clear();

        for (Card card : game.getPlayer1InHandCards()) {
            player1Hand.getChildren().add(card);
        }

        for (Card card : game.getPlayer2InHandCards()) {
            player2Hand.getChildren().add(card);
        }
    }

    private Card createCardView(Card card) {
        card.setSmallImage();
        card.setOnMouseClicked(event -> { // this will disable when it's not his turn
            System.out.println("clicked");
            if (card.equals(game.getPlayer1LeaderCard()) || card.equals(game.getPlayer2LeaderCard())) {
                showLeaderCardOverlay(card);
            } else {
                selectCard(card);
                // showHandCardOverlay(card);
            }
        });
        return card;
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
        // nextTurn(); // ridam dahane in khat
        overlayPane.setVisible(false);
        overlayPane.getChildren().clear();
    }

    private void nextTurn() {
        game.switchSides();
        startTurn();
    }

    private void startTurn() {
        updateScore();
        setupCardsInHand();
        setupCardsOnBoard();
        if (game.isPlayer1Turn()) {
            // Enable Player 2's hand and disable Player 1's hand
            for (Node card : player1Hand.getChildren()) {
                card.setDisable(false);
            }
            for (Node card : player2Hand.getChildren()) {
                card.setDisable(true);
            }
            displayMessage("Turn of Player 2");

        } else {
            // Enable Player 1's hand and disable Player 2's hand
            for (Node card : player2Hand.getChildren()) {
                card.setDisable(false);
            }
            for (Node card : player1Hand.getChildren()) {
                card.setDisable(true);
            }
            displayMessage("Turn of Player 1");
        }
    }

    private void selectCard(Card card) {
        selectedCard = card;

        // Highlighting and setting mouse handler for possible rows
        boolean isPlayer1 = game.isPlayer1Turn() ^ card.getAbility() instanceof Spy;
        clearHighlights();
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
                    highlightRow(player1CloseCombatSpell, Row.PLAYER1_CLOSE_COMBAT, card);
                    highlightRow(player1RangedSpell, Row.PLAYER1_RANGED, card);
                    highlightRow(player1SiegeSpell, Row.PLAYER1_SIEGE, card);
                } else {
                    highlightRow(player2CloseCombatSpell, Row.PLAYER2_CLOSE_COMBAT, card);
                    highlightRow(player2RangedSpell, Row.PLAYER2_RANGED, card);
                    highlightRow(player2SiegeSpell, Row.PLAYER2_SIEGE, card);
                }
            } else if (card.getType() == Type.DECOY) {
                if (isPlayer1) {
                    player1CloseCombat.getChildren().forEach(inGameCard -> highlightCard((Card) inGameCard, card));
                    player1Ranged.getChildren().forEach(inGameCard -> highlightCard((Card) inGameCard, card));
                    player1Siege.getChildren().forEach(inGameCard -> highlightCard((Card) inGameCard, card));
                } else {
                    player2CloseCombat.getChildren().forEach(inGameCard -> highlightCard((Card) inGameCard, card));
                    player2Ranged.getChildren().forEach(inGameCard -> highlightCard((Card) inGameCard, card));
                    player2Siege.getChildren().forEach(inGameCard -> highlightCard((Card) inGameCard, card));
                }
            }
        }
    }

    private void highlightRow(HBox rowBox, Row row, Card card) {
        rowBox.setStyle("-fx-background-color: rgba(255, 255, 150, 0.2);");
        rowBox.setOnMouseClicked(rowClickEvent -> placeCard(card, row));
    }

    private void highlightCard(Card inGameCard, Card card) {
        inGameCard.setStroke(Paint.valueOf("FFFFA0B0"));
        inGameCard.setStrokeWidth(2);
        inGameCard.setOnMouseClicked(rowClickEvent -> {
            placeCard(card, inGameCard.getRow());
            ((Decoy) card.getAbility()).setReturnCard(inGameCard);
        });
    }

    private void clearHighlights() {
        player1CloseCombat.setStyle("");
        player2CloseCombat.setStyle("");
        player1Ranged.setStyle("");
        player2Ranged.setStyle("");
        player1Siege.setStyle("");
        player2Siege.setStyle("");
        weather.setStyle("");
        player1CloseCombatSpell.setStyle("");
        player2CloseCombatSpell.setStyle("");
        player1RangedSpell.setStyle("");
        player2RangedSpell.setStyle("");
        player1SiegeSpell.setStyle("");
        player2SiegeSpell.setStyle("");
        player1CloseCombat.getChildren().forEach(inGameCard -> ((Card) inGameCard).setStrokeWidth(0));
        player1Ranged.getChildren().forEach(inGameCard -> ((Card) inGameCard).setStrokeWidth(0));
        player1Siege.getChildren().forEach(inGameCard -> ((Card) inGameCard).setStrokeWidth(0));
        player2CloseCombat.getChildren().forEach(inGameCard -> ((Card) inGameCard).setStrokeWidth(0));
        player2Ranged.getChildren().forEach(inGameCard -> ((Card) inGameCard).setStrokeWidth(0));
        player2Siege.getChildren().forEach(inGameCard -> ((Card) inGameCard).setStrokeWidth(0));

        player1CloseCombat.setOnMouseClicked(null);
        player1Ranged.setOnMouseClicked(null);
        player1Siege.setOnMouseClicked(null);
        player2CloseCombat.setOnMouseClicked(null);
        player2Ranged.setOnMouseClicked(null);
        player2Siege.setOnMouseClicked(null);
        weather.setOnMouseClicked(null);
        player1CloseCombatSpell.setOnMouseClicked(null);
        player1RangedSpell.setOnMouseClicked(null);
        player1SiegeSpell.setOnMouseClicked(null);
        player2CloseCombatSpell.setOnMouseClicked(null);
        player2RangedSpell.setOnMouseClicked(null);
        player2SiegeSpell.setOnMouseClicked(null);
        player1CloseCombat.getChildren().forEach(inGameCard -> inGameCard.setOnMouseClicked(null));
        player1Ranged.getChildren().forEach(inGameCard -> inGameCard.setOnMouseClicked(null));
        player1Siege.getChildren().forEach(inGameCard -> inGameCard.setOnMouseClicked(null));
        player2CloseCombat.getChildren().forEach(inGameCard -> inGameCard.setOnMouseClicked(null));
        player2Ranged.getChildren().forEach(inGameCard -> inGameCard.setOnMouseClicked(null));
        player2Siege.getChildren().forEach(inGameCard -> inGameCard.setOnMouseClicked(null));
    }

    private void placeCard(Card card, Row row) {
        playCard(card, row);
        clearHighlights(); // Clear any row highlights after placing the card
    }

    private void playCard(Card card, Row row) {
        clearHighlights(); // Clear any row highlights before placing the card
        if (row.isPlayer1() ^ card.getAbility() instanceof Spy) {
            game.player1PlayCard(card, row);
        } else {
            game.player2PlayCard(card, row);
        }
        game.calculatePoints();
        nextTurn();
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
}
