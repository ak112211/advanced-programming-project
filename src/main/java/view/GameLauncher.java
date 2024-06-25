package view;

import controller.GameController;
import enums.Row;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static controller.AppController.loadScene;

public class GameLauncher extends Application {
    private Stage primaryStage;
    private Pane gamePane;
    private Pane overlayPane;
    private VBox exitMenu;
    private Text messageDisplay;
    private HBox player1Hand;
    private HBox player2Hand;
    private VBox player1CloseCombat;
    private VBox player2CloseCombat;
    private VBox player1Ranged;
    private VBox player2Ranged;
    private VBox player1Siege;
    private VBox player2Siege;
    private VBox player1Score;
    private VBox player2Score;
    private VBox player1Graveyard;
    private VBox player2Graveyard;
    private VBox player1Leader;
    private VBox player2Leader;
    private Card selectedCard;
    private boolean loadFromSaved = false;
    private Game game;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.game = Game.getCurrentGame();
        setupGamePane();
        setupPauseMenu();
        setupMessageDisplay();
        setupGame();
        setupScene();
        startTurn(); // Start with Player 1's turn
    }

    public void showPause() {
        exitMenu.setVisible(true);
    }

    public void hidePause() {
        exitMenu.setVisible(false);
    }

    public void setupPauseMenu() {
        exitMenu = new VBox(10);
        exitMenu.setAlignment(Pos.CENTER);
        exitMenu.setVisible(false);
        exitMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20;");

        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(event -> hidePause());

        Button saveButton = new Button("Save Game");
        saveButton.setOnAction(event -> GameController.saveGameState(game));

        Button exitButton = new Button("Exit to Main Menu");
        exitButton.setOnAction(event -> loadScene("mainMenu.fxml"));

        exitMenu.getChildren().addAll(resumeButton, saveButton, exitButton);
        gamePane.getChildren().add(exitMenu);
    }

    public void setupMessageDisplay() {
        messageDisplay = new Text();
        messageDisplay.setStyle("-fx-font-size: 18; -fx-fill: white;");
        messageDisplay.setVisible(false);

        StackPane messagePane = new StackPane(messageDisplay);
        messagePane.setAlignment(Pos.TOP_CENTER);
        messagePane.setPrefSize(1200, 50);
        messagePane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        gamePane.getChildren().add(messagePane);
    }

    private void setupGamePane() {
        gamePane = new Pane();
        gamePane.setPrefSize(1200, 800);
        gamePane.setBackground(new Background(createBackgroundImage()));
        if (game != null) {
            game.gamePane = gamePane;
        }

        overlayPane = new Pane();
        overlayPane.setPrefSize(1200, 800);
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        overlayPane.setVisible(false);
        gamePane.getChildren().add(overlayPane);

        // Initialize player hand and board areas
        player1Hand = new HBox(5);
        player1Hand.setAlignment(Pos.BOTTOM_CENTER);
        player1Hand.setPrefHeight(150);
        player1Hand.setLayoutY(650);

        player2Hand = new HBox(5);
        player2Hand.setAlignment(Pos.TOP_CENTER);
        player2Hand.setPrefHeight(150);
        player2Hand.setLayoutY(0);

        player1CloseCombat = new VBox(5);
        player1CloseCombat.setAlignment(Pos.BOTTOM_CENTER);
        player1CloseCombat.setPrefHeight(150);
        player1CloseCombat.setLayoutY(500);

        player2CloseCombat = new VBox(5);
        player2CloseCombat.setAlignment(Pos.TOP_CENTER);
        player2CloseCombat.setPrefHeight(150);
        player2CloseCombat.setLayoutY(150);

        player1Ranged = new VBox(5);
        player1Ranged.setAlignment(Pos.BOTTOM_CENTER);
        player1Ranged.setPrefHeight(150);
        player1Ranged.setLayoutY(450);

        player2Ranged = new VBox(5);
        player2Ranged.setAlignment(Pos.TOP_CENTER);
        player2Ranged.setPrefHeight(150);
        player2Ranged.setLayoutY(200);

        player1Siege = new VBox(5);
        player1Siege.setAlignment(Pos.BOTTOM_CENTER);
        player1Siege.setPrefHeight(150);
        player1Siege.setLayoutY(400);

        player2Siege = new VBox(5);
        player2Siege.setAlignment(Pos.TOP_CENTER);
        player2Siege.setPrefHeight(150);
        player2Siege.setLayoutY(250);

        player1Score = new VBox(5);
        player1Score.setAlignment(Pos.CENTER_LEFT);
        player1Score.setLayoutX(10);
        player1Score.setLayoutY(200);

        player2Score = new VBox(5);
        player2Score.setAlignment(Pos.CENTER_RIGHT);
        player2Score.setLayoutX(1150);
        player2Score.setLayoutY(200);

        player1Graveyard = new VBox(5);
        player1Graveyard.setAlignment(Pos.CENTER_LEFT);
        player1Graveyard.setLayoutX(10);
        player1Graveyard.setLayoutY(300);

        player2Graveyard = new VBox(5);
        player2Graveyard.setAlignment(Pos.CENTER_RIGHT);
        player2Graveyard.setLayoutX(1150);
        player2Graveyard.setLayoutY(300);

        player1Leader = new VBox(5);
        player1Leader.setAlignment(Pos.CENTER_LEFT);
        player1Leader.setLayoutX(10);
        player1Leader.setLayoutY(400);

        player2Leader = new VBox(5);
        player2Leader.setAlignment(Pos.CENTER_RIGHT);
        player2Leader.setLayoutX(1150);
        player2Leader.setLayoutY(400);

        gamePane.getChildren().addAll(player1Hand, player2Hand, player1CloseCombat, player2CloseCombat, player1Ranged, player2Ranged, player1Siege, player2Siege, player1Score, player2Score, player1Graveyard, player2Graveyard, player1Leader, player2Leader);

        setupRowDragAndDrop(player1CloseCombat, Row.PLAYER1_CLOSE_COMBAT);
        setupRowDragAndDrop(player2CloseCombat, Row.PLAYER2_CLOSE_COMBAT);
        setupRowDragAndDrop(player1Ranged, Row.PLAYER1_RANGED);
        setupRowDragAndDrop(player2Ranged, Row.PLAYER2_RANGED);
        setupRowDragAndDrop(player1Siege, Row.PLAYER1_SIEGE);
        setupRowDragAndDrop(player2Siege, Row.PLAYER2_SIEGE);
    }

    private void setupRowDragAndDrop(VBox rowBox, Row row) {
        rowBox.setOnDragOver(event -> {
            if (event.getGestureSource() != rowBox && event.getDragboard().hasContent(Card.DATA_FORMAT)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        rowBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(Card.DATA_FORMAT)) {
                Card card = (Card) db.getContent(Card.DATA_FORMAT);
                if (card != null && selectedCard != null && selectedCard.equals(card)) {
                    playCard(card, row);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupGame() {
        if (!loadFromSaved) {
            game = new Game(User.getCurrentUser(), new User("Opponent", "opponent", "opponent@example.com", "password"), new Date());
            Game.setCurrentGame(game);
            GameController.game = game;
            game.initializeGameObjects(); // Setup initial game objects
        } else {
            game.initializeGameObjectsFromSaved(); // Setup initial game objects from saved state
        }

        // Add cards to player hand and board
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

    private void setupScene() {
        Scene scene = new Scene(gamePane, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gwent Game");
        primaryStage.setResizable(false);
        primaryStage.show();

        scene.setOnMouseClicked(event -> {
            if (!event.getTarget().equals(overlayPane)) {
                overlayPane.setVisible(false);
                overlayPane.getChildren().clear();
            }
        });
    }

    public void updateScore() {
        // Implement score updating logic
    }

    public BackgroundImage createBackgroundImage() {
        Image image = new Image("/mnt/data/board.jpg", 1200, 800, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        return backgroundImage;
    }

    public Pane getGamePane() {
        return this.gamePane;
    }

    public void setGame(Game loadedGame) {
        this.game = loadedGame;
    }

    public void displayMessage(String message) {
        messageDisplay.setText(message);
        messageDisplay.setVisible(true);
        // Hide message after a few seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> messageDisplay.setVisible(false));
        pause.play();
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
        nextTurn();
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

    public void switchSides() {
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

    public void nextTurn() {
        switchSides();
    }

    public void startTurn() {
        game.setCurrentPlayer(game.getPlayer1());
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
}
