package view;

import enums.cards.*;
import enums.cardsinformation.Faction;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.card.Card;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static controller.AppController.loadScene;

public class MainController {
    @FXML private CheckBox multiplayerCheckBox;
    @FXML private VBox friendsMenu;
    @FXML private ListView<String> friendsListView;
    @FXML private ListView<String> factionCardsListView;
    @FXML private ListView<String> deckCardsListView;
    @FXML private Label unitCardsLabel;
    @FXML private Button startGameButton;

    private int unitCardsCount = 0;
    private List<Card> factionCards = new ArrayList<>();
    private List<Card> deckCards = new ArrayList<>();
    private Faction selectedFaction;
    private String selectedLeader;

    @FXML
    private void initialize() {
        // Load friends list (dummy data for now)
        friendsListView.getItems().addAll("Friend1", "Friend2", "Friend3");

        // Initialize faction cards (using dummy data for now)
        loadFactionCards(Faction.EMPIRE_NILFGAARDIAM); // Default faction for demonstration
    }

    @FXML
    private void goToProfile() {
        loadScene("/fxml/ProfileMenu.fxml");
    }

    @FXML
    private void goToUserProfile() {
        loadScene("/fxml/ProfileMenu.fxml");
    }

    @FXML
    private void goToAddFriends() {
        loadScene("/fxml/AddFriends.fxml");
    }

    @FXML
    private void toggleMultiplayer() {
        friendsMenu.setVisible(multiplayerCheckBox.isSelected());
    }

    @FXML
    private void sendPlayRequest() {
        String selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            // Send play request to selected friend
        }
    }

    @FXML
    private void uploadDeck() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject deckJson = new JSONObject(content);
                // Load deck from JSON
                deckCards.clear();
                deckCardsListView.getItems().clear();

                selectedFaction = Faction.valueOf(deckJson.getString("faction"));
                selectedLeader = deckJson.getString("leader");

                JSONArray deckArray = deckJson.getJSONArray("deckCards");
                for (int i = 0; i < deckArray.length(); i++) {
                    String cardName = deckArray.getString(i);
                    Card card = getCardByNameAndFaction(cardName, selectedFaction);
                    if (card != null) {
                        deckCards.add(card);
                        deckCardsListView.getItems().add(card.getName());
                    }
                }
                unitCardsCount = (int) deckCards.stream().filter(card -> card.getType().equals("Unit")).count();
                updateUnitCardsLabel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void downloadDeck() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                JSONObject deckJson = new JSONObject();
                deckJson.put("faction", selectedFaction.name());
                deckJson.put("leader", selectedLeader);

                JSONArray deckArray = new JSONArray();
                for (Card card : deckCards) {
                    deckArray.put(card.getName());
                }
                deckJson.put("deckCards", deckArray);

                Files.write(file.toPath(), deckJson.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void chooseFaction() {
        List<String> factionOptions = new ArrayList<>();
        for (Faction faction : Faction.values()) {
            factionOptions.add(faction.name());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(factionOptions.get(0), factionOptions);
        dialog.setTitle("Choose Faction");
        dialog.setHeaderText("Select a faction for your deck");
        dialog.setContentText("Faction:");

        dialog.showAndWait().ifPresent(faction -> {
            selectedFaction = Faction.valueOf(faction);
            loadFactionCards(selectedFaction);
            // Clear current deck as the faction has changed
            deckCards.clear();
            deckCardsListView.getItems().clear();
            unitCardsCount = 0;
            updateUnitCardsLabel();
        });
    }

    private void loadFactionCards(Faction faction) {
        factionCards.clear();
        factionCardsListView.getItems().clear();
        switch (faction) {
            case EMPIRE_NILFGAARDIAM:
                for (EmpireNilfgaardianCards card : EmpireNilfgaardianCards.values()) {
                    factionCards.add(card.getCard());
                    factionCardsListView.getItems().add(card.getCard().getName());
                }
                break;
            case MONSTER:
                for (MonstersCards card : MonstersCards.values()) {
                    factionCards.add(card.getCard());
                    factionCardsListView.getItems().add(card.getCard().getName());
                }
                break;
            case NEUTRAL:
                for (NeutralCards card : NeutralCards.values()) {
                    factionCards.add(card.getCard());
                    factionCardsListView.getItems().add(card.getCard().getName());
                }
                break;
            case SCOIA_TAEL:
                for (ScoiaTaelCards card : ScoiaTaelCards.values()) {
                    factionCards.add(card.getCard());
                    factionCardsListView.getItems().add(card.getCard().getName());
                }
                break;
            case REALMS_NORTHERN:
                for (RealmsNorthernCards card : RealmsNorthernCards.values()) {
                    factionCards.add(card.getCard());
                    factionCardsListView.getItems().add(card.getCard().getName());
                }
                break;
        }
    }

    @FXML
    private void chooseLeader() {
        List<String> leaderOptions = new ArrayList<>();
        for (Card leader : getLeadersForFaction(selectedFaction)) {
            leaderOptions.add(leader.getName());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(leaderOptions.get(0), leaderOptions);
        dialog.setTitle("Choose Leader");
        dialog.setHeaderText("Select a leader for your deck");
        dialog.setContentText("Leader:");

        dialog.showAndWait().ifPresent(leader -> {
            selectedLeader = leader;
            // Set selected leader (implementation depends on how you handle leaders in your model)
        });
    }

    private List<Card> getLeadersForFaction(Faction faction) {
        List<Card> leaders = new ArrayList<>();
        // Assuming a method getLeaders() that returns a list of leaders for each faction
        switch (faction) {
            case EMPIRE_NILFGAARDIAM:
                // Add Nilfgaardian leaders
                break;
            case MONSTER:
                // Add Monster leaders
                break;
            case NEUTRAL:
                // Add Neutral leaders
                break;
            case SCOIA_TAEL:
                // Add Scoia'Tael leaders
                break;
            case REALMS_NORTHERN:
                // Add Northern Realms leaders
                break;
        }
        return leaders;
    }

    @FXML
    private void addToDeck() {
        String selectedCardName = factionCardsListView.getSelectionModel().getSelectedItem();
        if (selectedCardName != null) {
            for (Card card : factionCards) {
                if (card.getName().equals(selectedCardName)) {
                    deckCards.add(card);
                    deckCardsListView.getItems().add(card.getName());
                    if (card.getType().equals("Unit")) {
                        unitCardsCount++;
                    }
                    updateUnitCardsLabel();
                    break;
                }
            }
        }
    }

    private void updateUnitCardsLabel() {
        unitCardsLabel.setText("Unit Cards: " + unitCardsCount);
        startGameButton.setDisable(unitCardsCount < 22);
    }

    private Card getCardByNameAndFaction(String cardName, Faction faction) {
        switch (faction) {
            case EMPIRE_NILFGAARDIAM:
                for (EmpireNilfgaardianCards cardEnum : EmpireNilfgaardianCards.values()) {
                    if (cardEnum.getCard().getName().equals(cardName)) {
                        return cardEnum.getCard();
                    }
                }
                break;
            case MONSTER:
                for (MonstersCards cardEnum : MonstersCards.values()) {
                    if (cardEnum.getCard().getName().equals(cardName)) {
                        return cardEnum.getCard();
                    }
                }
                break;
            case NEUTRAL:
                for (NeutralCards cardEnum : NeutralCards.values()) {
                    if (cardEnum.getCard().getName().equals(cardName)) {
                        return cardEnum.getCard();
                    }
                }
                break;
            case SCOIA_TAEL:
                for (ScoiaTaelCards cardEnum : ScoiaTaelCards.values()) {
                    if (cardEnum.getCard().getName().equals(cardName)) {
                        return cardEnum.getCard();
                    }
                }
                break;
            case REALMS_NORTHERN:
                for (RealmsNorthernCards cardEnum : RealmsNorthernCards.values()) {
                    if (cardEnum.getCard().getName().equals(cardName)) {
                        return cardEnum.getCard();
                    }
                }
                break;
        }
        return null;
    }
}
