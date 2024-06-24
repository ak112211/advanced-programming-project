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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;

import java.util.Date;

import static controller.AppController.loadScene;

public class GameLauncher extends Application {
    private Stage primaryStage;
    private Pane gamePane;
    public Game game;
    private VBox exitMenu;
    private Text messageDisplay;
    private HBox player1Hand;
    private HBox player2Hand;
    private VBox player1Board;
    private VBox player2Board;
    private VBox player1Score;
    private VBox player2Score;
    private VBox player1Graveyard;
    private VBox player2Graveyard;
    private VBox player1Leader;
    private VBox player2Leader;

    private boolean loadFromSaved = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

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

        // Initialize player hand and board areas
        player1Hand = new HBox(5);
        player1Hand.setAlignment(Pos.BOTTOM_CENTER);
        player1Hand.setPrefHeight(150);
        player1Hand.setLayoutY(650);

        player2Hand = new HBox(5);
        player2Hand.setAlignment(Pos.TOP_CENTER);
        player2Hand.setPrefHeight(150);
        player2Hand.setLayoutY(0);

        player1Board = new VBox(5);
        player1Board.setAlignment(Pos.BOTTOM_CENTER);
        player1Board.setPrefHeight(150);
        player1Board.setLayoutY(500);

        player2Board = new VBox(5);
        player2Board.setAlignment(Pos.TOP_CENTER);
        player2Board.setPrefHeight(150);
        player2Board.setLayoutY(150);

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

        gamePane.getChildren().addAll(player1Hand, player2Hand, player1Board, player2Board, player1Score, player2Score, player1Graveyard, player2Graveyard, player1Leader, player2Leader);

        setupDragAndDrop(player1Board, Row.PLAYER1_CLOSE_COMBAT, Row.PLAYER1_RANGED, Row.PLAYER1_SIEGE);
        setupDragAndDrop(player2Board, Row.PLAYER2_CLOSE_COMBAT, Row.PLAYER2_RANGED, Row.PLAYER2_SIEGE);
    }

    private void setupDragAndDrop(VBox board, Row... rows) {
        board.setOnDragOver(event -> {
            if (event.getGestureSource() != board && event.getDragboard().hasContent(Card.DATA_FORMAT)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        board.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(Card.DATA_FORMAT)) {
                Card card = (Card) db.getContent(Card.DATA_FORMAT);
                if (card != null) {
                    playCard(card, card.getRow().isPlayer1());
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupGame() {
        if (!loadFromSaved) {
            GameController.game = game;
            game.initializeGameObjects(); // Setup initial game objects
        } else {
            game.initializeGameObjectsFromSaved(); // Setup initial game objects from saved state
        }

        // Add cards to player hand and board
        setupCardsInHand();
        setupCardsOnBoard();
        setupLeaderCards();
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
        card.setOnDragDetected(event -> {
            Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(Card.DATA_FORMAT, card);
            db.setContent(content);
            event.consume();
        });

        card.setOnMouseClicked(event -> playCard(card, isPlayer1));
        return card;
    }

    private void setupCardsOnBoard() {
        player1Board.getChildren().clear();
        player2Board.getChildren().clear();

        for (Card card : game.getPlayer1InHandCards()) {
            player1Board.getChildren().add(card);
        }

        for (Card card : game.getPlayer2InHandCards()) {
            player2Board.getChildren().add(card);
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

    private void playCard(Card card, boolean isPlayer1) {
        if (isPlayer1) {
            game.player1PlayCard(card, null);
        } else {
            game.player2PlayCard(card, null);
        }
        game.calculatePoints();
        setupCardsOnBoard();
        setupCardsInHand();
        nextTurn();
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
}
