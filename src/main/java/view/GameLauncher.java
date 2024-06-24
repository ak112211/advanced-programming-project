package view;

import controller.GameController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.User;
import model.card.Card;

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
    private HBox player1Board;
    private HBox player2Board;
    private VBox leaderCards;
    private boolean loadFromSaved = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        setupGamePane();
        setupPauseMenu();
        setupMessageDisplay();
        setupGame();
        setupScene();
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

        player1Board = new HBox(5);
        player1Board.setAlignment(Pos.BOTTOM_CENTER);
        player1Board.setPrefHeight(150);
        player1Board.setLayoutY(500);

        player2Board = new HBox(5);
        player2Board.setAlignment(Pos.TOP_CENTER);
        player2Board.setPrefHeight(150);
        player2Board.setLayoutY(150);

        leaderCards = new VBox(5);
        leaderCards.setAlignment(Pos.CENTER_LEFT);
        leaderCards.setLayoutX(50);
        leaderCards.setLayoutY(300);

        gamePane.getChildren().addAll(player1Hand, player2Hand, player1Board, player2Board, leaderCards);
    }

    private void setupGame() {
        if (!loadFromSaved) {
            game = new Game(User.getCurrentUser(), new User("Computer"), new Date());
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
    }

    private void setupCardsInHand() {
        player1Hand.getChildren().clear();
        player2Hand.getChildren().clear();

        for (Card card : game.getPlayer1InHandCards()) {
            ImageView cardView = new ImageView(new Image(card.getImagePath()));
            cardView.setFitHeight(100);
            cardView.setFitWidth(70);
            cardView.setOnMouseClicked(event -> playCard(card, true));
            player1Hand.getChildren().add(cardView);
        }

        for (Card card : game.getPlayer2InHandCards()) {
            ImageView cardView = new ImageView(new Image(card.getImagePath()));
            cardView.setFitHeight(100);
            cardView.setFitWidth(70);
            cardView.setOnMouseClicked(event -> playCard(card, false));
            player2Hand.getChildren().add(cardView);
        }
    }

    private void setupCardsOnBoard() {
        player1Board.getChildren().clear();
        player2Board.getChildren().clear();

        for (Card card : game.getPlayer1BoardCards()) {
            ImageView cardView = new ImageView(new Image(card.getImagePath()));
            cardView.setFitHeight(100);
            cardView.setFitWidth(70);
            player1Board.getChildren().add(cardView);
        }

        for (Card card : game.getPlayer2BoardCards()) {
            ImageView cardView = new ImageView(new Image(card.getImagePath()));
            cardView.setFitHeight(100);
            cardView.setFitWidth(70);
            player2Board.getChildren().add(cardView);
        }
    }

    private void setupLeaderCards() {
        ImageView player1LeaderCard = new ImageView(new Image(game.getPlayer1LeaderCard().getImagePath()));
        player1LeaderCard.setFitHeight(100);
        player1LeaderCard.setFitWidth(70);

        ImageView player2LeaderCard = new ImageView(new Image(game.getPlayer2LeaderCard().getImagePath()));
        player2LeaderCard.setFitHeight(100);
        player2LeaderCard.setFitWidth(70);

        leaderCards.getChildren().addAll(player1LeaderCard, player2LeaderCard);
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
        gamePane.setRotate(gamePane.getRotate() + 180);
    }

    public void nextTurn() {
        switchSides();

    }
}
