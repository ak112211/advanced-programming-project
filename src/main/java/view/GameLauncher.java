package view;

import controller.GameController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Game;
import model.User;
import util.GameSerializer;

import static controller.AppController.loadScene;
import static controller.GameController.*;

public class GameLauncher extends Application {
    private Stage primaryStage;
    private VBox pauseMenu;
    private int currentTrack;
    public MediaPlayer mediaPlayer; // Assuming this handles your background music
    public boolean loadFromSaved = false;
    private Pane gamePane;
    public Text messageDisplay;  // Add this
    private ProgressBar freezeBar;
    public Game game;
    private Text scoreBoard;
    private Text waveNumber;
    private double freezePoints = 0; // Total points accumulated for freezing
    private final double maxFreezePoints = 200;
    private Text clusterBombCount;
    private Text atomicBombCount;
    public final double WIDTH = 800;
    public final double HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        setupGamePane();
        setupGame();
        setupScene();
        setupScoreAndBombDisplay();
        setupMessageDisplay();
        setupPauseMenu();
        setupBackgroundMusic();

        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-1);
        if (User.getCurrentUser().isBlack()) {
            primaryStage.getScene().getRoot().setEffect(grayscale);
        }
    }

    private void setupBackgroundMusic() {
        Media media = new Media(getClass().getResource("/media/wrong-place-129242.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
        if (!User.getCurrentUser().isMute()) {
            mediaPlayer.play(); // Start playing the music
        }
    }

    public void showPause() {
        pauseMenu.setVisible(true);
    }

    public void hidePause() {
        pauseMenu.setVisible(false);
    }

    public void setupPauseMenu() {
        pauseMenu = new VBox(10);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); -fx-padding: 20; -fx-border-color: white; -fx-border-width: 2;");
        pauseMenu.setVisible(false);
        pauseMenu.setLayoutX((WIDTH - 200) / 2);
        pauseMenu.setLayoutY((HEIGHT - 300) / 2);

        Text pauseTitle = new Text("Game Paused");
        Button resumeButton = new Button("Resume Game");
        if (User.getCurrentUser().getPassword() != null) {
            Button saveExitButton = new Button("Save & Exit");
            saveExitButton.setOnAction(e -> {
                stopAnimations();
                mediaPlayer.pause();
                saveGameState(game);
                loadScene("/fxml/MainMenu.fxml");
            });

            pauseMenu.getChildren().add(saveExitButton);
        }
        Button exitButton = new Button("Exit Without Saving");
        Button stopMusicButton = new Button("Toggle Sound");
        Button showControlsButton = new Button("Show Controls");
        Button changeMusicButton = new Button("Change Music");

        resumeButton.setOnAction(e -> togglePause(game));
        exitButton.setOnAction(e -> {
            stopAnimations();
            mediaPlayer.pause();
            if (User.getCurrentUser().getPassword() == null) {
                loadScene("/fxml/LoginScreen.fxml");
            } else {
                loadScene("/fxml/MainMenu.fxml");
            }
        });
        stopMusicButton.setOnAction(e -> {
            if (!User.getCurrentUser().isMute()) {
                User.getCurrentUser().muteAll();
                mediaPlayer.pause();
            } else {
                User.getCurrentUser().unmuteAll();
                mediaPlayer.play();
            }
        });
        showControlsButton.setOnAction(e -> showControls());
        changeMusicButton.setOnAction(e -> changeMusic());

        pauseMenu.getChildren().addAll(pauseTitle, resumeButton, exitButton, stopMusicButton, showControlsButton, changeMusicButton);
        gamePane.getChildren().add(pauseMenu);
    }

    private void showControls() {
        // Display a new window or dialog with control instructions
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Controls");
        alert.setHeaderText("Key Bindings for Game");
        alert.setContentText("A / left - Move Left\nD / right - Move Right\nW / up - Rotate Counter-Clockwise\nS / down - Rotate Clockwise\nSPACE - Shoot\nC - Deploy Cluster Bomb\nR - Deploy Atomic Bomb\nESC - Pause Game");
        alert.showAndWait();
    }

    private void changeMusic() {
        // Change the background music
        String[] musicFiles = {"/media/wrong-place-129242.mp3", "/media/Ramin-Djawadi-Finale-128.mp3", "/media/piano.mp3"};

        if (currentTrack == 1) {
            currentTrack = 2;
        } else if (currentTrack == 2) {
            currentTrack = 3;
        } else {
            currentTrack = 1;
        }
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(new Media(getClass().getResource(musicFiles[currentTrack]).toString()));
        mediaPlayer.play();
    }

    public void setupMessageDisplay() {
        VBox infoDisplay = new VBox(10);
        messageDisplay = new Text();
        messageDisplay.setVisible(false);  // Initially hide the message
        infoDisplay.getChildren().add(messageDisplay);
        infoDisplay.setLayoutX(100); // Positioned at the top left of the pane
        infoDisplay.setLayoutY(300);
        gamePane.getChildren().add(infoDisplay);
    }

    private void setupGamePane() {
        gamePane = new Pane();
        gamePane.setPrefSize(WIDTH, HEIGHT);
        gamePane.setBackground(new Background(createBackgroundImage()));
        if (game != null) {
            game.gamePane = gamePane;
        }
    }

    private void setupGame() {
        if (!loadFromSaved) {
            game = new Game(User.getCurrentUser().getUsername(), this);
            Game.setCurrentGame(game);
            GameController.game = game;
            game.initializeGameObjects(); // Setup initial game objects based on the current wave
            game.initializeWaveCounts();
        } else {
            game.initializeGameObjectsFromSaved(); // Setup initial game objects based on the current wave
        }
    }

    private void setupScene() {
        Scene scene = new Scene(gamePane, WIDTH, HEIGHT);
        scene.setOnKeyPressed(event -> GameController.handleKeyPress(event, game.getBomber()));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Launcher");
        primaryStage.setResizable(false);
        primaryStage.show();
        game.setupGameLoop();
    }

    public void setupScoreAndBombDisplay() {
        freezeBar = new ProgressBar(0);
        freezeBar.setPrefWidth(200);  // Set the preferred width of the freeze bar
        freezeBar.setLayoutX(300);
        freezeBar.setLayoutY(50);
        VBox infoDisplay = new VBox(10);
        scoreBoard = new Text("Kills: 0");
        clusterBombCount = new Text("Cluster Bombs: 0");
        atomicBombCount = new Text("Atomic Bombs: 0");
        waveNumber = new Text("Wave: 1");

        infoDisplay.getChildren().addAll(scoreBoard, clusterBombCount, atomicBombCount, waveNumber);
        infoDisplay.setLayoutX(10); // Positioned at the top left of the pane
        infoDisplay.setLayoutY(10);

        gamePane.getChildren().add(infoDisplay);
        gamePane.getChildren().add(freezeBar);
    }

    public void updateScore() {
        scoreBoard.setText("Kills: " + game.getKills());
    }

    public void updateWaveNumber() {
        waveNumber.setText("Wave: " + game.getCurrentWave());
    }

    public void updateClusterBombs() {
        clusterBombCount.setText("Cluster Bombs: " + game.getNumOfClusterBombs());
    }

    public void updateAtomicBombs() {
        atomicBombCount.setText("Atomic Bombs: " + game.getNumOfAtomicBombs());
    }

    public void incrementFreezeBar(int score) {
        freezePoints += score;
        if (freezePoints >= maxFreezePoints) {
            freezePoints = maxFreezePoints;
        }
        freezeBar.setProgress(freezePoints / maxFreezePoints); // Normalize between 0 and 1
    }

    public BackgroundImage createBackgroundImage() {
        Image image = new Image(Game.class.getResource("/images/needed/sky.png").toExternalForm(), WIDTH ,HEIGHT, false, false);
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
}
