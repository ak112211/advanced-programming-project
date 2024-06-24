import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import enums.Row;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import enums.cardsinformation.Description;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;
import model.Deck;
import model.abilities.Ability;

public class TestCases {
    private Game game;
    private User player1;
    private User player2;

    @Before
    public void setUp() {
        player1 = new User("Player1", "P1", "player1@example.com", "password");
        player2 = new User("Player2", "P2", "player2@example.com", "password");

        // Setup leaders and decks for both players (mocking simple decks for testing)
        Leader leader1 = new Leader("Leader1", Faction.NEUTRAL, "Leader1 description");
        Leader leader2 = new Leader("Leader2", Faction.NEUTRAL, "Leader2 description");
        player1.setLeader(leader1);
        player2.setLeader(leader2);

        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        // Mock adding cards to the decks
        Ability dummyAbility = new Ability() {
            @Override
            public void apply() {
                // Dummy ability implementation
            }
        };

        Card card1 = new Card();
        card1.setRow(Row.MELEE);
        Card card2 = new Card();
        card2.setRow(Row.RANGED);

        deck1.addCard(card1);
        deck1.addCard(card2);

        deck2.addCard(card1);
        deck2.addCard(card2);

        player1.setDeck(deck1);
        player2.setDeck(deck2);

        game = new Game(player1, player2, new Date());
        game.initializeGameObjects();
        game.setCurrentPlayer(player1);
    }

    @Test
    public void testGameInitialization() {
        // Test if game is initialized correctly
        assertEquals("Player1", game.getPlayer1().getUsername());
        assertEquals("Player2", game.getPlayer2().getUsername());
        assertEquals(Game.GameStatus.PENDING, game.getStatus());
    }

    @Test
    public void testPlayer1PlayCard() {
        // Test if Player 1 can play a card
        Card card = game.getPlayer1InHandCards().get(0);
        boolean result = game.player1PlayCard(card, null);
        assertTrue(result);
        assertTrue(game.getInGameCards().contains(card));
    }

    @Test
    public void testPlayer2PlayCard() {
        // Test if Player 2 can play a card
        Card card = game.getPlayer2InHandCards().get(0);
        boolean result = game.player2PlayCard(card, null);
        assertTrue(result);
        assertTrue(game.getInGameCards().contains(card));
    }

    @Test
    public void testSwitchTurns() {
        // Test switching turns
        game.setCurrentPlayer(player1);
        assertTrue(game.isPlayer1Turn());

        game.setCurrentPlayer(player2);
        assertTrue(!game.isPlayer1Turn());
    }

    @Test
    public void testCalculatePoints() {
        // Test if points are calculated correctly
        Card card1 = game.getPlayer1InHandCards().get(0);
        Card card2 = game.getPlayer2InHandCards().get(0);

        game.player1PlayCard(card1, null);
        game.player2PlayCard(card2, null);

        game.calculatePoints();

        int player1Points = game.getPlayer1Points();
        int player2Points = game.getPlayer2Points();

        assertTrue(player1Points > 0);
        assertTrue(player2Points > 0);
    }
}
