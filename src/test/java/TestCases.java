import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import enums.Row;
import enums.cards.SkelligeCards;
import enums.leaders.SkelligeLeaders;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;
import model.Deck;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestCases {
    private Game game;
    private User player1;
    private User player2;

    @Before
    public void setUp() {
        player1 = new User("Player1", "P1", "player1@example.com", "password");
        player2 = new User("Player2", "P2", "player2@example.com", "password");

        // Setup leaders and decks for both players (mocking simple decks for testing)
        Leader leader1 = SkelligeLeaders.CRACH_AN_CRAITE.getLeader();
        Leader leader2 = SkelligeLeaders.KING_BRAN.getLeader();
        player1.getDeck().setLeader(leader1);
        player2.getDeck().setLeader(leader2);

        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        // Add cards to the decks from SkelligeCards enum
        Card card1 = SkelligeCards.BERSERKER.getCard();
        Card card2 = SkelligeCards.VILDKAARL.getCard();

        deck1.addCard(card1);
        deck1.addCard(card2);

        deck2.addCard(card1);
        deck2.addCard(card2);

        player1.setDeck(deck1);
        player2.setDeck(deck2);

        game = new Game(player1, player2);
        game.startGameThread();
        game.setPlayer1Turn(true);
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
        Card card = game.getPlayer1InHandCards().getFirst();
        boolean result = game.player1PlayCard(card, Row.PLAYER1_CLOSE_COMBAT);
        assertTrue(result);
        assertTrue(game.getInGameCards().contains(card));
    }

    @Test
    public void testPlayer2PlayCard() {
        // Test if Player 2 can play a card
        Card card = game.getPlayer2InHandCards().getFirst();
        boolean result = game.player2PlayCard(card, Row.PLAYER2_CLOSE_COMBAT);
        assertTrue(result);
        assertTrue(game.getInGameCards().contains(card));
    }

    @Test
    public void testSwitchTurns() {
        // Test switching turns
        game.setPlayer1Turn(true);
        assertTrue(game.isPlayer1Turn());

        game.setPlayer1Turn(false);
        assertTrue(!game.isPlayer1Turn());
    }

    @Test
    public void testCalculatePoints() {
        // Test if points are calculated correctly
        Card card1 = game.getPlayer1InHandCards().getFirst();
        Card card2 = game.getPlayer2InHandCards().getFirst();

        game.player1PlayCard(card1, Row.PLAYER1_CLOSE_COMBAT);
        game.player2PlayCard(card2, Row.PLAYER2_CLOSE_COMBAT);

        try {
            Method method = game.getClass().getMethod("calculatePoints");
            method.setAccessible(true);
            method.invoke(game);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        int player1Points = game.getPlayer1Points();
        int player2Points = game.getPlayer2Points();

        assertTrue(player1Points > 0);
        assertTrue(player2Points > 0);
    }

}
