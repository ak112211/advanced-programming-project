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

public class ChooseDeckMenuController {

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
    public static boolean isMulti;
    private User currentUser;
    private Deck currentDeck;
    private Deck player2Deck;
    private Game game;
    private boolean settingFromSaved = false;

    public static String opponent;
    private List<Leader> leaders = new ArrayList<>();

    private boolean isPlayer2Turn = false;

    @FXML
    private void initialize() {
        currentUser = User.getCurrentUser();
        currentDeck = currentUser.getDeck();
        if (currentUser.getGames() != null) {
            for (Game game : currentUser.getGames()) {
                savedGamesListView.getItems().add(game.getDate().toString());
            }
        } else {
            savedGamesListView.setVisible(false);
        }
        savedGamesListView.setOnMouseClicked(event -> {
            String selectedDate = savedGamesListView.getSelectionModel().getSelectedItem();
            game = currentUser.getGames().stream()
                    .filter(c -> c.getDate().toString().equals(selectedDate))
                    .findFirst()
                    .orElse(null);
        });


        continueButton.setDisable(true);
        friendsMenu.setDisable(true);

        setup();

        factionComboBox.setOnAction(event -> {
            if (!settingFromSaved) {
                if (currentDeck.getFaction() != null && factionComboBox.getValue() != null) {
                    currentDeck.getCards().clear();
                    deckCardsListView.getItems().clear();
                    Faction selectedFaction = factionComboBox.getValue();
                    currentDeck.setFaction(selectedFaction);
                    loadLeaders(currentDeck.getFaction());
                    loadFactionCards(currentDeck.getFaction());
                }
            }
        });

        leaderComboBox.setOnAction(event -> {
            Leader leader = leaderComboBox.getValue();
            if (currentDeck.getLeader() != null && leaderComboBox.getValue() != null && player2Deck!= null) {
                if (!isPlayer2Turn) {
                    currentDeck.setLeader(leaderComboBox.getValue());
                    loadFactionCards(currentDeck.getFaction());
                    loadLeaders(currentDeck.getFaction());
                    leaderComboBox.setValue(leader);
                    showBigImage(currentDeck.getLeader().getImagePath(), currentDeck.getLeader().getDescription());
                } else {
                    player2Deck.setLeader(leaderComboBox.getValue());
                    loadFactionCards(player2Deck.getFaction());
                    loadLeaders(player2Deck.getFaction());
                    leaderComboBox.setValue(leader);
                    showBigImage(player2Deck.getLeader().getImagePath(), player2Deck.getLeader().getDescription());
                }

            }
        });

        factionCardsListView.setOnMouseClicked(event -> {
            Card selectedCard = factionCardsListView.getSelectionModel().getSelectedItem();
            if (selectedCard != null) {
                loadFactionCards(currentDeck.getFaction());
                addToDeck(selectedCard);
                showBigImage(selectedCard.getImagePath(), selectedCard.getDescription().getDescription());
            }
        });

        deckCardsListView.setOnMouseClicked(event -> {
            Card selectedCard = deckCardsListView.getSelectionModel().getSelectedItem();
            if (selectedCard != null) {
                loadFactionCards(currentDeck.getFaction());
                removeFromDeck(selectedCard);
            }
        });

        updateCardCounts();
    }

    private void setup() {

        factionComboBox.getItems().setAll(Faction.values());
        factionComboBox.getItems().remove(Faction.NEUTRAL);
        if (!isPlayer2Turn) {
            currentDeck = new Deck();
            currentUser.setDeck(currentDeck);
            currentDeck.setFaction(Faction.REALMS_NORTHERN);
            loadLeaders(currentDeck.getFaction());
            factionComboBox.setValue(currentDeck.getFaction());
            loadFactionCards(factionComboBox.getValue());
            loadLeaders(factionComboBox.getValue());
            deckCardsListView.getItems().addAll(currentDeck.getCards());
        } else {
            player2Deck = new Deck();
            player2Deck.setFaction(Faction.EMPIRE_NILFGAARDIAM);
            loadLeaders(player2Deck.getFaction());
            factionComboBox.setValue(player2Deck.getFaction());
            loadFactionCards(factionComboBox.getValue());
            loadLeaders(factionComboBox.getValue());
            deckCardsListView.getItems().addAll(player2Deck.getCards());
        }
    }

    @FXML
    private void changeTurn(ActionEvent event) {
        isPlayer2Turn = !isPlayer2Turn;
        deckCardsListView.getItems().clear();
        factionCardsListView.getItems().clear();
        setup();
    }

    public void hideCardDisplay() {
        cardDisplayVBox.setVisible(false);
    }

    private void loadFactionCards(Faction faction) {
        factionCardsListView.getItems().clear();
        List<Card> factionCards = new ArrayList<>();

        switch (faction) {
            case EMPIRE_NILFGAARDIAM:
                for (EmpireNilfgaardianCards cardEnum : EmpireNilfgaardianCards.values()) {
                    factionCards.add(cardEnum.getCard());
                }
                break;
            case MONSTER:
                for (MonstersCards cardEnum : MonstersCards.values()) {
                    factionCards.add(cardEnum.getCard());
                }
                break;
            case SCOIA_TAEL:
                for (ScoiaTaelCards cardEnum : ScoiaTaelCards.values()) {
                    factionCards.add(cardEnum.getCard());
                }
                break;
            case REALMS_NORTHERN:
                for (RealmsNorthernCards cardEnum : RealmsNorthernCards.values()) {
                    factionCards.add(cardEnum.getCard());
                }
                break;
            case SKELLIGE:
                for (SkelligeCards cardEnum : SkelligeCards.values()) {
                    factionCards.add(cardEnum.getCard());
                }
                break;
        }

        for (NeutralCards cardEnum : NeutralCards.values()) {
            factionCards.add(cardEnum.getCard());
        }

        factionCardsListView.getItems().addAll(factionCards);
    }

    private void loadLeaders(Faction faction) {
        leaderComboBox.getItems().clear();
        leaders.clear();
        setUpLeaders(faction);
        leaderComboBox.getItems().addAll(leaders);
        Leader randomLeader = leaders.get(new Random().nextInt(leaders.size()));
        leaderComboBox.setValue(randomLeader);
        currentDeck.setLeader(randomLeader);

    }

    private Leader getRandomLeader() {
        return leaders.get(new Random().nextInt(leaders.size()));
    }

    private void setUpLeaders(Faction faction) {
        switch (faction) {
            case EMPIRE_NILFGAARDIAM:
                for (EmpireNilfgaardianLeaders leaderEnum : EmpireNilfgaardianLeaders.values()) {
                    leaders.add(leaderEnum.getLeader());
                }
                break;
            case MONSTER:
                for (MonstersLeaders leaderEnum : MonstersLeaders.values()) {
                    leaders.add(leaderEnum.getLeader());
                }
                break;
            case REALMS_NORTHERN:
                for (RealmsNorthernLeaders leaderEnum : RealmsNorthernLeaders.values()) {
                    leaders.add(leaderEnum.getLeader());
                }
                break;
            case SCOIA_TAEL:
                for (ScoiaTaelLeaders leaderEnum : ScoiaTaelLeaders.values()) {
                    leaders.add(leaderEnum.getLeader());
                }
                break;
            case SKELLIGE:
                for (SkelligeLeaders leaderEnum : SkelligeLeaders.values()) {
                    leaders.add(leaderEnum.getLeader());
                }
                break;
        }
    }

    private void addToDeck(Card card) {
        long countInDeck = deckCardsListView.getItems().stream().filter(c -> c.getName().equals(card.getName())).count();
        if (countInDeck < card.getNoOfCardsInGame()) {
            deckCardsListView.getItems().add(card);
            if (!isPlayer2Turn) {
                currentDeck.getCards().add(card);
            } else {
                player2Deck.getCards().add(card);
            }
            updateCardCounts();
        }
    }

    private void removeFromDeck(Card card) {
        deckCardsListView.getItems().remove(card);
        if (!isPlayer2Turn) {
            currentDeck.getCards().remove(card);
        } else {
            player2Deck.getCards().remove(card);
        }
        updateCardCounts();
    }

    private void updateCardCounts() {
        long unitCardsCount = deckCardsListView.getItems().stream().filter(c -> c.getType().isUnit()).count();
        long specialCardsCount = deckCardsListView.getItems().stream().filter(c -> c.getType().isSpecial()).count();
        unitCardsLabel.setText("Unit Cards: " + unitCardsCount);
        specialCardsLabel.setText("Special Cards: " + specialCardsCount);

        boolean validDeck = unitCardsCount >= 22 && specialCardsCount <= 10;
        choosePlayer2DeckButton.setDisable(!validDeck);
        startGameButton.setDisable(player2Deck == null || !validDeck);
    }

    @FXML
    private void goBack() {
        isMulti = false;
        opponent = null;
        App.loadScene(Menu.MAIN_MENU.getPath());
    }

    @FXML
    private void sendPlayRequest() {
        // Implement send play request
    }

    @FXML
    private void uploadDeck() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Deck File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try (FileReader reader = new FileReader(selectedFile)) {
                StringBuilder jsonBuilder = new StringBuilder();
                int i;
                while ((i = reader.read()) != -1) {
                    jsonBuilder.append((char) i);
                }
                settingFromSaved = true;
                currentDeck = Deck.fromJson(jsonBuilder.toString());
                factionComboBox.setValue(currentDeck.getFaction());
                leaderComboBox.setValue(currentDeck.getLeader());
                deckCardsListView.getItems().clear();
                deckCardsListView.getItems().addAll(currentDeck.getCards());
                updateCardCounts();
                Tools.showAlert("Deck loaded successfully.");
                settingFromSaved = false;
            } catch (IOException e) {
                Tools.showAlert("Failed to load deck: " + e.getMessage());
            }
        }
    }

    @FXML
    private void downloadDeck() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Deck File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(currentDeck.toJson());
                Tools.showAlert("Deck saved successfully.");
            } catch (IOException e) {
                Tools.showAlert("Failed to save deck: " + e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

    private void showBigImage(String imagePath, String description) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        // Load the image and set it in the ImageView
        Image image = new Image(
                Objects.requireNonNull(getClass().getResource(imagePath))
                        .toExternalForm());
        cardImageView.setImage(image);

        // Set the description text
        cardDescriptionText.setText(description);

        // Ensure the VBox containing the image and description is visible
        cardDisplayVBox.setVisible(true);

        cardDisplayVBox.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (!cardDisplayVBox.contains(event.getScreenX(), event.getScreenY())) {
                hideCardDisplay();
            }
        });
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

}
