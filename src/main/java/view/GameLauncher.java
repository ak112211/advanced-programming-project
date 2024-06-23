package view;

import controller.GameController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.User;
import model.card.Card;
import util.GameSerializer;

import java.util.Date;

import static controller.AppController.loadScene;
import static controller.GameController.*;

public class GameLauncher extends Application {
    private Stage primaryStage;
    private Pane gamePane;
    public Game game;
    private VBox exitMenu;
    private Text messageDisplay;
    private HBox playerHand;
    private HBox playerDeck;
    private VBox leaderCards;

    private boolean loadFromSaved = false;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupGamePane();
        setupPauseMenu();
        setupMessageDisplay();
        setupScoreAndBombDisplay();
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

        // Initialize player hand and deck areas
        playerHand = new HBox(5);
        playerHand.setAlignment(Pos.BOTTOM_CENTER);
        playerHand.setPrefHeight(150);
        playerHand.setLayoutY(650);

        playerDeck = new HBox(5);
        playerDeck.setAlignment(Pos.BOTTOM_CENTER);
        playerDeck.setPrefHeight(150);
        playerDeck.setLayoutY(500);

        leaderCards = new VBox(5);
        leaderCards.setAlignment(Pos.CENTER_LEFT);
        leaderCards.setLayoutX(50);
        leaderCards.setLayoutY(300);

        gamePane.getChildren().addAll(playerHand, playerDeck, leaderCards);
    }

    private void setupGame() {
        if (!loadFromSaved) {
            game = new Game(User.getCurrentUser(), new User("Player2"), new Date());
            Game.setCurrentGame(game);
            GameController.game = game;
            game.initializeGameObjects(); // Setup initial game objects
        } else {
            game.initializeGameObjectsFromSaved(); // Setup initial game objects from saved state
        }

        // Add cards to player hand and deck
        setupCardsInHand();
        setupCardsInDeck();
        setupLeaderCards();
    }

    private void setupCardsInHand() {
        for (Card card : game.getPlayer1InHandCards()) {
            ImageView cardView = new ImageView(new Image(card.getImagePath()));
            cardView.setFitHeight(100);
            cardView.setFitWidth(70);
            playerHand.getChildren().add(cardView);
        }
    }

    private void setupCardsInDeck() {
        for (Card card : game.getPlayer1Deck()) {
            ImageView cardView = new ImageView(new Image(card.getImagePath()));
            cardView.setFitHeight(100);
            cardView.setFitWidth(70);
            playerDeck.getChildren().add(cardView);
        }
    }

    private void setupLeaderCards() {
        // Assuming you have methods getPlayer1LeaderCard and getPlayer2LeaderCard
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

    public void setupScoreAndBombDisplay() {
        // Implement score and bomb display setup
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
}
