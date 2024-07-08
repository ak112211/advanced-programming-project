package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.League;

import java.util.ArrayList;
import java.util.List;

public class LeagueScreenController {

    @FXML
    private TextField leagueNameField;
    @FXML
    private ListView<String> playersListView;
    @FXML
    private Label winnerLabel;

    private League league;

    @FXML
    private void initialize() {
        league = new League("Default League");
        leagueNameField.setText(league.getName());
        updatePlayersListView();
    }

    @FXML
    private void handleAddPlayer() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Player");
        dialog.setHeaderText("Add a new player to the league");
        dialog.setContentText("Player name:");
        dialog.showAndWait().ifPresent(name -> {
            try {
                league.addPlayer(name);
                updatePlayersListView();
            } catch (IllegalStateException e) {
                showAlert("Error", "Cannot add player", e.getMessage());
            }
        });
    }

    @FXML
    private void handleStartQuarterFinals() {
        try {
            league.startQuarterFinals();
            showAlert("Quarter Finals", "Quarter Finals Started",
                    "Matches: " + league.getQuarter1Game() + ", " + league.getQuarter2Game() + ", "
                            + league.getQuarter3Game() + ", " + league.getQuarter4Game());
        } catch (IllegalStateException e) {
            showAlert("Error", "Cannot start quarter finals", e.getMessage());
        }
    }

    @FXML
    private void handleStartSemiFinals() {
        // Assuming winners are manually input for simplicity
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Semi Finals");
        dialog.setHeaderText("Enter winners of quarter finals (comma-separated):");
        dialog.setContentText("Winners:");
        dialog.showAndWait().ifPresent(winners -> {
            String[] winnerArray = winners.split(",");
            if (winnerArray.length == 4) {
                league.startSemiFinals(winnerArray[0].trim(), winnerArray[1].trim(), winnerArray[2].trim(), winnerArray[3].trim());
                showAlert("Semi Finals", "Semi Finals Started", "Matches: " + league.getSemi1Game() + ", " + league.getSemi2Game());
            } else {
                showAlert("Error", "Invalid Input", "Please enter exactly 4 winners.");
            }
        });
    }

    @FXML
    private void handleStartFinals() {
        // Assuming winners are manually input for simplicity
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Finals");
        dialog.setHeaderText("Enter winners of semi finals (comma-separated):");
        dialog.setContentText("Winners:");
        dialog.showAndWait().ifPresent(winners -> {
            String[] winnerArray = winners.split(",");
            if (winnerArray.length == 2) {
                league.startFinals(winnerArray[0].trim(), winnerArray[1].trim());
                showAlert("Finals", "Finals Started", "Match: " + league.getFinalPlay());
            } else {
                showAlert("Error", "Invalid Input", "Please enter exactly 2 winners.");
            }
        });
    }

    @FXML
    private void handleDeclareWinner() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Declare Winner");
        dialog.setHeaderText("Enter winner of the finals:");
        dialog.setContentText("Winner:");
        dialog.showAndWait().ifPresent(winner -> {
            league.declareWinner(winner.trim());
            winnerLabel.setText(league.getWinner());
        });
    }

    private void updatePlayersListView() {
        playersListView.getItems().setAll(league.getPlayers());
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
