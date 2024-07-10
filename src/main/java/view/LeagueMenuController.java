package view;

import controller.AppController;
import enums.Menu;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import model.App;
import model.League;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;
import java.util.Objects;


public class LeagueMenuController {

    @FXML
    public Pane results;
    @FXML
    public ListView<String> playersListView;
    @FXML
    public Label leagueName;
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
        leagueName.setText(league.getName());
        Platform.runLater(() -> playersListView.getItems().addAll(league.getPlayers()));
        updateUI();
    }

    @FXML
    public void handleStartQ1() {
        if (player1.getText().equals(User.getCurrentUser().getUsername())) {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(player2.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(player1.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ChooseDeckMenuController.leagueGameStep = "q1";
        ChooseDeckMenuController.league = league;
        assert ChooseDeckMenuController.player2 != null;
        App.getServerConnection().sendMessage(ChooseDeckMenuController.player2.getUsername() + ":send message:" + User.getCurrentUser().getUsername() + " sent request for league game");
        AppController.loadScene(Menu.DECK_MENU);
    }

    @FXML
    public void handleStartQ2() {
        if (player3.getText().equals(User.getCurrentUser().getUsername())) {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(player4.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(player3.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ChooseDeckMenuController.leagueGameStep = "q2";
        ChooseDeckMenuController.league = league;
        assert ChooseDeckMenuController.player2 != null;
        App.getServerConnection().sendMessage(ChooseDeckMenuController.player2.getUsername() + ":send message:" + User.getCurrentUser().getUsername() + " sent request for league game");
        AppController.loadScene(Menu.DECK_MENU);
    }

    @FXML
    public void handleStartQ3() {
        if (player5.getText().equals(User.getCurrentUser().getUsername())) {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(player6.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(player5.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ChooseDeckMenuController.leagueGameStep = "q3";
        ChooseDeckMenuController.league = league;
        assert ChooseDeckMenuController.player2 != null;
        App.getServerConnection().sendMessage(ChooseDeckMenuController.player2.getUsername() + ":send message:" + User.getCurrentUser().getUsername() + " sent request for league game");
        AppController.loadScene(Menu.DECK_MENU);
    }

    @FXML
    public void handleStartQ4() {
        if (player7.getText().equals(User.getCurrentUser().getUsername())) {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(player8.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(player7.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ChooseDeckMenuController.leagueGameStep = "q4";
        ChooseDeckMenuController.league = league;
        assert ChooseDeckMenuController.player2 != null;
        App.getServerConnection().sendMessage(ChooseDeckMenuController.player2.getUsername() + ":send message:" + User.getCurrentUser().getUsername() + " sent request for league game");
        AppController.loadScene(Menu.DECK_MENU);
    }

    @FXML
    public void handleStartSemiFinal1() {
        if (playerSemiFinal1.getText().equals(User.getCurrentUser().getUsername())) {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(playerSemiFinal2.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(playerSemiFinal1.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ChooseDeckMenuController.leagueGameStep = "s1";
        ChooseDeckMenuController.league = league;
        assert ChooseDeckMenuController.player2 != null;
        App.getServerConnection().sendMessage(ChooseDeckMenuController.player2.getUsername() + ":send message:" + User.getCurrentUser().getUsername() + " sent request for league game");
        AppController.loadScene(Menu.DECK_MENU);
    }

    @FXML
    public void handleStartSemiFinal2() {
        if (playerSemiFinal3.getText().equals(User.getCurrentUser().getUsername())) {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(playerSemiFinal4.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(playerSemiFinal3.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ChooseDeckMenuController.leagueGameStep = "s2";
        ChooseDeckMenuController.league = league;
        assert ChooseDeckMenuController.player2 != null;
        App.getServerConnection().sendMessage(ChooseDeckMenuController.player2.getUsername() + ":send message:" + User.getCurrentUser().getUsername() + " sent request for league game");
        AppController.loadScene(Menu.DECK_MENU);
    }

    @FXML
    public void handleStartFinal() {
        if (playerFinal1.getText().equals(User.getCurrentUser().getUsername())) {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(playerFinal2.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ChooseDeckMenuController.player2 = DatabaseConnection.getUser(playerFinal1.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ChooseDeckMenuController.leagueGameStep = "f";
        ChooseDeckMenuController.league = league;
        assert ChooseDeckMenuController.player2 != null;
        App.getServerConnection().sendMessage(ChooseDeckMenuController.player2.getUsername() + ":send message:" + User.getCurrentUser().getUsername() + " sent request for league game");
        AppController.loadScene(Menu.DECK_MENU);
    }

    private void updateUI() {
        if (league.getPlayers().size() < 8) {
            results.setVisible(false);
        } else {
            results.setVisible(true);
            // Set initial text for labels based on league state
            player1.setText(league.getPlayers().get(0));
            player2.setText(league.getPlayers().get(1));
            player3.setText(league.getPlayers().get(2));
            player4.setText(league.getPlayers().get(3));
            player5.setText(league.getPlayers().get(4));
            player6.setText(league.getPlayers().get(5));
            player7.setText(league.getPlayers().get(6));
            player8.setText(league.getPlayers().get(7));

            if (league.getQuarter1Game() != null) {
                startQ1.setVisible(false);
                try {
                    p1q1.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter1Game()))).getPlayer1Points()));
                    p2q1.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter1Game()))).getPlayer2Points()));
                    playerSemiFinal1.setText(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter1Game()))).getWinnerUser().getUsername());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                startQ1.setVisible(player1.getText().equals(User.getCurrentUser().getUsername()) || player2.getText().equals(User.getCurrentUser().getUsername()));
                p1q1.setVisible(false);
                p2q1.setVisible(false);
                playerSemiFinal1.setText("?");
            }

            if (league.getQuarter2Game() != null) {
                startQ2.setVisible(false);
                try {
                    p1q2.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter2Game()))).getPlayer1Points()));
                    p2q2.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter2Game()))).getPlayer2Points()));
                    playerSemiFinal2.setText(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter2Game()))).getWinnerUser().getUsername());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                startQ2.setVisible(player3.getText().equals(User.getCurrentUser().getUsername()) || player4.getText().equals(User.getCurrentUser().getUsername()));
                p1q2.setVisible(false);
                p2q2.setVisible(false);
                playerSemiFinal2.setText("?");
            }

            startSemiFinal1.setVisible(league.getSemi1Game() == null && league.getQuarter1Game() != null && league.getQuarter2Game() != null &&
                    (playerSemiFinal1.getText().equals(User.getCurrentUser().getUsername()) || playerSemiFinal2.getText().equals(User.getCurrentUser().getUsername())));

            if (league.getQuarter3Game() != null) {
                startQ3.setVisible(false);
                try {
                    p1q3.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter3Game()))).getPlayer1Points()));
                    p2q3.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter3Game()))).getPlayer2Points()));
                    playerSemiFinal3.setText(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter3Game()))).getWinnerUser().getUsername());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                startQ3.setVisible(player5.getText().equals(User.getCurrentUser().getUsername()) || player6.getText().equals(User.getCurrentUser().getUsername()));
                p1q3.setVisible(false);
                p2q3.setVisible(false);
                playerSemiFinal3.setText("?");
            }

            if (league.getQuarter4Game() != null) {
                startQ4.setVisible(false);
                try {
                    p1q4.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter4Game()))).getPlayer1Points()));
                    p2q4.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter4Game()))).getPlayer2Points()));
                    playerSemiFinal4.setText(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getQuarter4Game()))).getWinnerUser().getUsername());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                startQ4.setVisible(player7.getText().equals(User.getCurrentUser().getUsername()) || player8.getText().equals(User.getCurrentUser().getUsername()));
                p1q4.setVisible(false);
                p2q4.setVisible(false);
                playerSemiFinal4.setText("?");
            }

            startSemiFinal2.setVisible(league.getSemi2Game() == null && league.getQuarter3Game() != null && league.getQuarter4Game() != null &&
                    (playerSemiFinal3.getText().equals(User.getCurrentUser().getUsername()) || playerSemiFinal4.getText().equals(User.getCurrentUser().getUsername())));

            if (league.getSemi1Game() != null) {
                try {
                    p1s1.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getSemi1Game()))).getPlayer1Points()));
                    p2s1.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getSemi1Game()))).getPlayer2Points()));
                    playerFinal1.setText(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getSemi1Game()))).getWinnerUser().getUsername());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                p1s1.setVisible(false);
                p2s1.setVisible(false);
                playerFinal1.setText("?");
            }

            if (league.getSemi2Game() != null) {
                try {
                    p1s2.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getSemi2Game()))).getPlayer1Points()));
                    p2s2.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getSemi2Game()))).getPlayer2Points()));
                    playerFinal2.setText(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getSemi2Game()))).getWinnerUser().getUsername());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                p1s2.setVisible(false);
                p2s2.setVisible(false);
                playerFinal2.setText("?");
            }

            startFinal.setVisible(league.getFinalPlay() == null && league.getSemi1Game() != null && league.getSemi2Game() != null &&
                    (playerFinal1.getText().equals(User.getCurrentUser().getUsername()) || playerFinal2.getText().equals(User.getCurrentUser().getUsername())));

            if (league.getFinalPlay() != null) {
                try {
                    p1f.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getFinalPlay()))).getPlayer1Points()));
                    p2f.setText(String.valueOf(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getFinalPlay()))).getPlayer2Points()));
                    Winner.setText(Objects.requireNonNull(DatabaseConnection.getGame(Integer.parseInt(league.getFinalPlay()))).getWinnerUser().getUsername());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                p1f.setVisible(false);
                p2f.setVisible(false);
                Winner.setText("?");
            }
        }
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        AppController.loadScene(Menu.CHAT_MENU);
    }
}
