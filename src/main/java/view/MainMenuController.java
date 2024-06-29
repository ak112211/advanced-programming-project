package view;

import enums.Menu;
import enums.cardsinformation.Faction;
import enums.cards.*;
import enums.leaders.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.App;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;
import model.Deck;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainMenuController {

    @FXML
    public HBox friendsMenu;
    @FXML
    public VBox cardDisplayVBox;
    @FXML
    public ImageView cardImageView;
    @FXML
    public Text cardDescriptionText;
    @FXML
    public ListView<String> savedGamesListView;
    @FXML
    public Button continueButton;
    @FXML
    private TextField friendUsernameField;
    @FXML
    private ListView<String> friendsListView;
    @FXML
    private ComboBox<Faction> factionComboBox;
    @FXML
    private ListView<Card> factionCardsListView;
    @FXML
    private ListView<Card> deckCardsListView;
    @FXML
    private ComboBox<Leader> leaderComboBox;
    @FXML
    private Label unitCardsLabel;
    @FXML
    private Label specialCardsLabel;
    @FXML
    private Button startGameButton;
    @FXML
    private Button choosePlayer2DeckButton;
    private boolean isMulti;
    private User currentUser;
    private Deck currentDeck;
    private Deck player2Deck;
    private Game game;
    private boolean settingFromSaved = false;
    private List<Leader> leaders = new ArrayList<>();

    private boolean isPlayer2Turn = false;


    @FXML
    private void goToProfile() {
        App.loadScene("/fxml/ProfileMenu.fxml");
    }

    @FXML
    private void goToAddFriends() {
        App.loadScene("/fxml/ChatMenu.fxml");
    }

    @FXML
    private void sendPlayRequest() {
        // Implement send play request
    }


    @FXML
    public void startGame(ActionEvent actionEvent) {
        if (currentDeck == null || player2Deck == null || currentDeck.getCards().size() < 22 || player2Deck.getCards().size() < 22) {
            Tools.showAlert("Error", "Deck Error", "Both players must have at least 22 unit cards to start the game.");
            return;
        }

        User player2 = new User("Player2", null, null, null);
        player2.setDeck(player2Deck);
        if (game == null) {
            game = new Game(currentUser, new User("Player2", null, null, null)); // Assuming "Player2" is a placeholder user
            game.setOnline(isMulti);
        }
        Game.setCurrentGame(game);
        new GameLauncher().start(App.getStage());
    }

    public void showScoreboard(ActionEvent actionEvent) {
        App.loadScene("/fxml/Scoreboard.fxml");
    }

    public void goToDeckMenu(ActionEvent actionEvent) {
        ChooseDeckMenuController.isMulti = false;
        App.loadScene(Menu.DECK_MENU.getPath());

    }
}
