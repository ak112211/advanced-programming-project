package view;

import enums.cardsinformation.Faction;
import enums.cards.*;
import enums.leaders.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.App;
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
    private List<Leader> leaders = new ArrayList<>();

    private boolean isPlayer2Turn;

    @FXML
    private void initialize() {
        currentUser = User.getCurrentUser();
        currentDeck = currentUser.getDeck();
        friendsMenu.setDisable(true);

        if (currentDeck == null) {
            currentDeck = new Deck();
            currentUser.setDeck(currentDeck);
            currentDeck.setFaction(Faction.REALMS_NORTHERN);
            Leader randomLeader = getRandomLeader();
            currentDeck.setLeader(randomLeader);
        }

        if (isMulti) {
            friendsListView.getItems().addAll(currentUser.getFriends());
        }
        factionComboBox.getItems().setAll(Faction.values());
        factionComboBox.getItems().remove(Faction.NEUTRAL);

        loadFactionCards(factionComboBox.getValue());
        loadLeaders(factionComboBox.getValue());

        if (!isPlayer2Turn) {
            leaderComboBox.setValue(currentDeck.getLeader() != null ? currentDeck.getLeader() : leaderComboBox.getItems().get(0));
            factionComboBox.setValue(currentDeck.getFaction() != null ? currentDeck.getFaction() : Faction.REALMS_NORTHERN);

            deckCardsListView.getItems().addAll(currentDeck.getCards());
        } else {
            leaderComboBox.setValue(player2Deck.getLeader() != null ? player2Deck.getLeader() : leaderComboBox.getItems().get(0));
            factionComboBox.setValue(player2Deck.getFaction() != null ? player2Deck.getFaction() : Faction.REALMS_NORTHERN);
            deckCardsListView.getItems().addAll(player2Deck.getCards());
        }


        factionComboBox.setOnAction(event -> {
            Faction selectedFaction = factionComboBox.getValue();
            deckCardsListView.getItems().clear();
            loadFactionCards(selectedFaction);
            loadLeaders(selectedFaction);
            Leader randomLeader = getRandomLeader();
            if (!isPlayer2Turn) {
                currentDeck.setFaction(selectedFaction);
                currentDeck.setLeader(randomLeader);
            } else {
                player2Deck.setFaction(selectedFaction);
                player2Deck.setLeader(randomLeader);
            }
            updateCardCounts();
        });

        leaderComboBox.setOnAction(event -> {
            if (!isPlayer2Turn) {
                currentDeck.setLeader(leaderComboBox.getValue());
                showBigImage(currentDeck.getLeader().getImagePath(), currentDeck.getLeader().getDescription());

            } else {
                player2Deck.setLeader(leaderComboBox.getValue());
                showBigImage(player2Deck.getLeader().getImagePath(), player2Deck.getLeader().getDescription());

            }
        });

        factionCardsListView.setOnMouseClicked(event -> {
            Card selectedCard = factionCardsListView.getSelectionModel().getSelectedItem();
            if (selectedCard != null) {
                addToDeck(selectedCard);
                showBigImage(selectedCard.getImagePath(), selectedCard.getDescription().getDescription());
            }
        });

        deckCardsListView.setOnMouseClicked(event -> {
            Card selectedCard = deckCardsListView.getSelectionModel().getSelectedItem();
            if (selectedCard != null) {
                removeFromDeck(selectedCard);
            }
        });

        updateCardCounts();
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

        if (currentDeck.getLeader() == null && !leaders.isEmpty()) {
            Leader randomLeader = leaders.get(new Random().nextInt(leaders.size() - 1));
            currentDeck.setLeader(randomLeader);
        }
    }

    private Leader getRandomLeader() {
        return leaders.get(new Random().nextInt(leaders.size() - 1));
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
        long countInDeck = deckCardsListView.getItems().stream().filter(c -> c.equals(card)).count();
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
    private void changeTurn(ActionEvent event) {
        isPlayer2Turn = !isPlayer2Turn;
        leaders.clear();
        player2Deck = new Deck();
        player2Deck.setFaction(Faction.REALMS_NORTHERN);
        Leader randomLeader = getRandomLeader();
        player2Deck.setLeader(randomLeader);
        initialize();
    }

    @FXML
    private void goToProfile() {
        App.loadScene("/fxml/ProfileMenu.fxml");
    }

    @FXML
    private void goToUserProfile() {
        App.loadScene("/fxml/ProfileMenu.fxml");
    }

    @FXML
    private void goToAddFriends() {
        App.loadScene("/fxml/AddFriendsMenu.fxml");
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
                currentDeck = Deck.fromJson(jsonBuilder.toString());
                factionComboBox.setValue(currentDeck.getFaction());
                leaderComboBox.setValue(currentDeck.getLeader());
                deckCardsListView.getItems().clear();
                deckCardsListView.getItems().addAll(currentDeck.getCards());
                removeFullyAddedCards();
                updateCardCounts();
                Tools.showAlert("Deck loaded successfully.");
            } catch (IOException e) {
                Tools.showAlert("Failed to load deck: " + e.getMessage());
            }
        }
    }

    private void removeFullyAddedCards() {
        List<Card> factionCards = factionCardsListView.getItems();
        List<Card> cardsToRemove = new ArrayList<>();
        for (Card factionCard : factionCards) {
            long countInDeck = currentDeck.getCards().stream().filter(c -> c.equals(factionCard)).count();
            if (countInDeck >= factionCard.getNoOfCardsInGame()) {
                cardsToRemove.add(factionCard);
            }
        }
        factionCardsListView.getItems().removeAll(cardsToRemove);
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

    @FXML
    private void handleSaveDeck() {
        Faction selectedFaction = factionComboBox.getValue();
        Leader selectedLeader = leaderComboBox.getValue();
        List<Card> selectedDeck = new ArrayList<>(deckCardsListView.getItems());

        if (selectedDeck.size() < 22) {
            Tools.showAlert("Deck must contain at least 22 unit cards.");
            return;
        }

        currentDeck.setFaction(selectedFaction);
        currentDeck.setLeader(selectedLeader);
        currentDeck.getCards().clear();
        currentDeck.getCards().addAll(selectedDeck);

        try (FileWriter writer = new FileWriter(currentDeck.getFaction() + ".json")) {
            writer.write(currentDeck.toJson());
            Tools.showAlert("Deck saved successfully.");
        } catch (IOException e) {
            Tools.showAlert("Failed to save deck: " + e.getMessage());
        }
    }

    public void toggleMultiplayer(ActionEvent actionEvent) {
        isMulti = !isMulti;
        if (isMulti)
            friendsMenu.setDisable(false);
    }

    private void showBigImage(String imagePath, String description) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        Stage stage = new Stage();
        StackPane root = new StackPane();
        VBox vbox = new VBox(10); // 10 is the spacing between the image and the text
        vbox.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource(imagePath))
                        .toExternalForm()));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        imageView.setFitHeight(400); // adjust the size as needed

        Text descriptionText = new Text(description);
        descriptionText.setWrappingWidth(200); // wrapping width to fit the text within the given width
        descriptionText.setTextAlignment(TextAlignment.CENTER);

        vbox.getChildren().addAll(imageView, descriptionText);
        root.getChildren().add(vbox);

        Scene scene = new Scene(root, 450, 450); // adjust the size as needed
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

}
