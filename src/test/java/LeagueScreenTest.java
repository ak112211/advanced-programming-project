import javafx.scene.control.*;
import javafx.stage.Stage;
import model.League;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import view.LeagueScreenController;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class LeagueScreenTest extends ApplicationTest {

    private LeagueScreenController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FxToolkit.setupStage((Consumer<Stage>) stage);
        controller.league = new League("Test League");
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize a new League before each test
        controller.league = new League("Test League");
        controller.initialize();
    }

    @Test
    public void testAddPlayer() {
        // Add a player and check the players list
        controller.league.addPlayer("Player1");
        controller.updatePlayersListView();
        ListView<String> playersListView = (ListView<String>) controller.league.getPlayers();
        assertEquals(1, playersListView.getItems().size());
        assertEquals("Player1", playersListView.getItems().get(0));
    }

    @Test
    public void testStartQuarterFinals() {
        // Add 8 players and start quarter finals
        for (int i = 1; i <= 8; i++) {
            controller.league.addPlayer("Player" + i);
        }
        controller.handleStartQuarterFinals();
        assertEquals("Player1 vs Player2", controller.league.getQuarter1Game());
        assertEquals("Player3 vs Player4", controller.league.getQuarter2Game());
        assertEquals("Player5 vs Player6", controller.league.getQuarter3Game());
        assertEquals("Player7 vs Player8", controller.league.getQuarter4Game());
    }

    @Test
    public void testStartSemiFinals() {
        // Set up quarter finals and start semi finals
        for (int i = 1; i <= 8; i++) {
            controller.league.addPlayer("Player" + i);
        }
        controller.handleStartQuarterFinals();
        assertEquals("Player1 vs Player3", controller.league.getSemi1Game());
        assertEquals("Player5 vs Player7", controller.league.getSemi2Game());
    }

    @Test
    public void testStartFinals() {
        // Set up semi finals and start finals
        for (int i = 1; i <= 8; i++) {
            controller.league.addPlayer("Player" + i);
        }
        controller.handleStartQuarterFinals();
        controller.handleStartSemiFinals("Player1", "Player3", "Player5", "Player7");
        controller.handleStartFinals("Player1", "Player5");
        assertEquals("Player1 vs Player5", controller.league.getFinalPlay());
    }

    @Test
    public void testDeclareWinner() {
        // Set up finals and declare winner
        for (int i = 1; i <= 8; i++) {
            controller.league.addPlayer("Player" + i);
        }
        controller.handleStartQuarterFinals();
        controller.handleStartSemiFinals("Player1", "Player3", "Player5", "Player7");
        controller.handleStartFinals("Player1", "Player5");
        controller.handleDeclareWinner("Player1");
        assertEquals("Player1", controller.league.getWinner());
    }
}
