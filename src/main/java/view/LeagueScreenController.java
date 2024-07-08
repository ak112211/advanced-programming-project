package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.League;


public class LeagueScreenController {

    @FXML
    private Label playerFinal1;
    @FXML
    private Label playerFinal2;
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label player3;
    @FXML
    private Label player4;
    @FXML
    private Label player5;
    @FXML
    private Label player6;
    @FXML
    private Label player7;
    @FXML
    private Label player8;
    @FXML
    private Label playerSemiFinal1;
    @FXML
    private Label playerSemiFinal2;
    @FXML
    private Label playerSemiFinal3;
    @FXML
    private Label playerSemiFinal4;
    @FXML
    private Label p1q1;
    @FXML
    private Label p2q1;
    @FXML
    private Label p1q2;
    @FXML
    private Label p2q2;
    @FXML
    private Label p1q3;
    @FXML
    private Label p2q3;
    @FXML
    private Label p1q4;
    @FXML
    private Label p2q4;
    @FXML
    private Label p1s1;
    @FXML
    private Label p2s1;
    @FXML
    private Label p1s2;
    @FXML
    private Label p2s2;
    @FXML
    private Label p1f;
    @FXML
    private Label p2f;
    @FXML
    private Label Winner;
    @FXML
    private Button startQ1;
    @FXML
    private Button startQ2;
    @FXML
    private Button startQ3;
    @FXML
    private Button startQ4;
    @FXML
    private Button startSemiFinal1;
    @FXML
    private Button startSemiFinal2;
    @FXML
    private Button startFinal;

    public static League league;

    @FXML
    public void initialize() {
        updateUI();
    }

    @FXML
    public void handleStartQ1() {
        league.setQuarter1Game("Q1 Game ID");
        p1q1.setText(league.getPlayers().get(0));
        p2q1.setText(league.getPlayers().get(1));
        // Additional logic for starting Quarter Final 1
    }

    @FXML
    public void handleStartQ2() {
        league.setQuarter2Game("Q2 Game ID");
        p1q2.setText(league.getPlayers().get(2));
        p2q2.setText(league.getPlayers().get(3));
        // Additional logic for starting Quarter Final 2
    }

    @FXML
    public void handleStartQ3() {
        league.setQuarter3Game("Q3 Game ID");
        p1q3.setText(league.getPlayers().get(4));
        p2q3.setText(league.getPlayers().get(5));
        // Additional logic for starting Quarter Final 3
    }

    @FXML
    public void handleStartQ4() {
        league.setQuarter4Game("Q4 Game ID");
        p1q4.setText(league.getPlayers().get(6));
        p2q4.setText(league.getPlayers().get(7));
        // Additional logic for starting Quarter Final 4
    }

    @FXML
    public void handleStartSemiFinal1() {
        league.setSemi1Game("S1 Game ID");
        // Logic to determine and set semi-final players
        // Example:
        playerSemiFinal1.setText(p1q1.getText());  // Replace with logic to get the winner
        playerSemiFinal2.setText(p2q2.getText());  // Replace with logic to get the winner
        // Additional logic for starting Semi Final 1
    }

    @FXML
    public void handleStartSemiFinal2() {
        league.setSemi2Game("S2 Game ID");
        // Logic to determine and set semi-final players
        // Example:
        playerSemiFinal3.setText(p1q3.getText());  // Replace with logic to get the winner
        playerSemiFinal4.setText(p2q4.getText());  // Replace with logic to get the winner
        // Additional logic for starting Semi Final 2
    }

    @FXML
    public void handleStartFinal() {
        league.setFinalPlay("Final Game ID");
        // Logic to determine and set final players
        // Example:
        p1f.setText(playerSemiFinal1.getText());  // Replace with logic to get the winner
        p2f.setText(playerSemiFinal3.getText());  // Replace with logic to get the winner
        // Additional logic for starting the Final
    }

    private void updateUI() {
        // Set initial text for labels based on league state
        player1.setText(league.getPlayers().get(0));
        player2.setText(league.getPlayers().get(1));
        player3.setText(league.getPlayers().get(2));
        player4.setText(league.getPlayers().get(3));
        player5.setText(league.getPlayers().get(4));
        player6.setText(league.getPlayers().get(5));
        player7.setText(league.getPlayers().get(6));
        player8.setText(league.getPlayers().get(7));
    }

}
