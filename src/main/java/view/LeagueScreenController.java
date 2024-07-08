package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.App;
import model.League;
import util.DatabaseConnection;

import java.sql.SQLException;

public class LeagueScreenController {

    @FXML
    public VBox results;
    @FXML
    private TextField leagueNameField;
    @FXML
    private ListView<String> playersListView;
    @FXML
    private Label quarter1GameLabel;
    @FXML
    private Label quarter2GameLabel;
    @FXML
    private Label quarter3GameLabel;
    @FXML
    private Label quarter4GameLabel;
    @FXML
    private Label semi1GameLabel;
    @FXML
    private Label semi2GameLabel;
    @FXML
    private Label finalGameLabel;
    @FXML
    private Label winnerLabel;

    public static League league = new League("Default League");

    @FXML
    public void initialize() {
        leagueNameField.setText(league.getName());
        updatePlayersListView();
        results.setVisible(league.getPlayers().size() >= 8);
    }

    private void updateGameLabels() {
        quarter1GameLabel.setText(league.getQuarter1Game());
        quarter2GameLabel.setText(league.getQuarter2Game());
        quarter3GameLabel.setText(league.getQuarter3Game());
        quarter4GameLabel.setText(league.getQuarter4Game());
        semi1GameLabel.setText(league.getSemi1Game());
        semi2GameLabel.setText(league.getSemi2Game());
        finalGameLabel.setText(league.getFinalPlay());
        winnerLabel.setText(league.getWinner());
    }


    @FXML
    public void handleStartQuarterFinals() {
        try {
            league.startQuarterFinals();
            updateGameLabels();
        } catch (IllegalStateException e) {
            showAlert("Error", "Cannot start quarter finals", e.getMessage());
        }
    }

    @FXML
    private void handleStartSemiFinals() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Semi Finals");
        dialog.setHeaderText("Enter winners of quarter finals (comma-separated):");
        dialog.setContentText("Winners:");
        dialog.showAndWait().ifPresent(winners -> {
            String[] winnerArray = winners.split(",");
            if (winnerArray.length == 4) {
                league.startSemiFinals(winnerArray[0].trim(), winnerArray[1].trim(), winnerArray[2].trim(), winnerArray[3].trim());
                updateGameLabels();
            } else {
                showAlert("Error", "Invalid Input", "Please enter exactly 4 winners.");
            }
        });
    }

    @FXML
    private void handleStartFinals() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Finals");
        dialog.setHeaderText("Enter winners of semi finals (comma-separated):");
        dialog.setContentText("Winners:");
        dialog.showAndWait().ifPresent(winners -> {
            String[] winnerArray = winners.split(",");
            if (winnerArray.length == 2) {
                league.startFinals(winnerArray[0].trim(), winnerArray[1].trim());
                updateGameLabels();
            } else {
                showAlert("Error", "Invalid Input", "Please enter exactly 2 winners.");
            }
        });
    }

    @FXML
    public void handleDeclareWinner(String player1) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Declare Winner");
        dialog.setHeaderText("Enter winner of the finals:");
        dialog.setContentText("Winner:");
        dialog.showAndWait().ifPresent(winner -> {
            league.declareWinner(winner.trim());
            updateGameLabels();
        });
    }

    public void updatePlayersListView() {
        try {
            playersListView.getItems().setAll(DatabaseConnection.getPlayersList(league.getID()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        App.loadScene(Menu.CHAT_MENU.getPath());
    }

    public void handleStartSemiFinals(String player1, String player3, String player5, String player7) {
    }

    public void handleStartFinals(String player1, String player5) {
    }
}
