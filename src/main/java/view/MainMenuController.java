package view;

import enums.cardsinformation.Faction;
import enums.cards.*;
import enums.leaders.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
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
import java.util.Random;

public class MainMenuController {

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

    private User currentUser;
    private Deck currentDeck;
    List<Leader> leaders = new ArrayList<>();

    @FXML
    private void initialize() {
        currentUser = User.getCurrentUser();
        currentDeck = currentUser.getDeck();

        // Create a new deck if the current deck is null
        if (currentDeck == null) {
            currentDeck = new Deck();
            currentUser.setDeck(currentDeck);
        }

        friendsListView.getItems().addAll(currentUser.getFriends());

        factionComboBox.getItems().setAll(Faction.values());
        factionComboBox.getItems().remove(Faction.NEUTRAL); // Remove neutral faction
        factionComboBox.setValue(currentDeck.getFaction() != null ? currentDeck.getFaction() : Faction.REALMS_NORTHERN);

        currentDeck.setFaction(Faction.REALMS_NORTHERN);
        loadFactionCards(factionComboBox.getValue());
        loadLeaders(factionComboBox.getValue());

        // Randomly select a leader if none is set
        if (currentDeck.getLeader() == null) {
            Leader randomLeader = getRandomLeader();
            currentDeck.setLeader(randomLeader);
        }

        leaderComboBox.setValue(currentDeck.getLeader() != null ? currentDeck.getLeader() : leaderComboBox.getItems().get(0));

        deckCardsListView.getItems().addAll(currentDeck.getCards());

        factionComboBox.setOnAction(event -> {
            Faction selectedFaction = factionComboBox.getValue();
            deckCardsListView.getItems().clear();
            loadFactionCards(selectedFaction);
            loadLeaders(selectedFaction);
            Leader randomLeader = getRandomLeader();
            currentDeck.setFaction(selectedFaction);
            currentDeck.setLeader(randomLeader);
            updateCardCounts();
        });

        leaderComboBox.setOnAction(event -> {
            currentDeck.setLeader(leaderComboBox.getValue());
        });

        factionCardsListView.setOnMouseClicked(event -> {
            Card selectedCard = factionCardsListView.getSelectionModel().getSelectedItem();
            if (selectedCard != null) {
                addToDeck(selectedCard);
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

        // Add faction-specific cards
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

        // Randomly select a leader if none is set
        if (currentDeck.getLeader() == null && !leaders.isEmpty()) {
            Leader randomLeader = leaders.get(new Random().nextInt(leaders.size()-1));
            currentDeck.setLeader(randomLeader);
        }
    }

    private Leader getRandomLeader() {
        return leaders.get(new Random().nextInt(leaders.size()-1));
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
            currentDeck.getCards().add(card);

            if (countInDeck + 1 == card.getNoOfCardsInGame()) {
                factionCardsListView.getItems().remove(card);
            }
            updateCardCounts();
        }
    }

    private void removeFromDeck(Card card) {
        deckCardsListView.getItems().remove(card);
        currentDeck.getCards().remove(card);
        if (!factionCardsListView.getItems().contains(card)) {
            factionCardsListView.getItems().add(card);
        }
        updateCardCounts();
    }

    private void updateCardCounts() {
        long unitCardsCount = deckCardsListView.getItems().stream().filter(c -> c.getType().isUnit()).count();
        long specialCardsCount = deckCardsListView.getItems().stream().filter(c -> c.getType().isSpecial()).count();
        unitCardsLabel.setText("Unit Cards: " + unitCardsCount);
        specialCardsLabel.setText("Special Cards: " + specialCardsCount);
        startGameButton.setDisable(unitCardsCount < 22 || specialCardsCount > 10);
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
        // Implement add friends navigation
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
                System.out.println(currentDeck.getCards().size());
                factionComboBox.setValue(currentDeck.getFaction());
                leaderComboBox.setValue(currentDeck.getLeader());
                deckCardsListView.getItems().clear();
                deckCardsListView.getItems().addAll(currentDeck.getCards());
                updateCardCounts();
                Tools.showAlert("Deck loaded successfully.");
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
    }
}
