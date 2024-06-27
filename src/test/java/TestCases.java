import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import enums.Row;
import enums.cards.SkelligeCards;
import enums.leaders.SkelligeLeaders;
import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;
import model.Deck;
import model.abilities.Ability;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

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
        boolean result = game.player1PlayCard(card, Row.PLAYER1_CLOSE_COMBAT);
        assertTrue(result);
        assertTrue(game.getInGameCards().contains(card));
    }

    @Test
    public void testPlayer2PlayCard() {
        // Test if Player 2 can play a card
        Card card = game.getPlayer2InHandCards().get(0);
        boolean result = game.player2PlayCard(card, Row.PLAYER2_CLOSE_COMBAT);
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

        game.player1PlayCard(card1, Row.PLAYER1_CLOSE_COMBAT);
        game.player2PlayCard(card2, Row.PLAYER2_CLOSE_COMBAT);

        game.calculatePoints();

        int player1Points = game.getPlayer1Points();
        int player2Points = game.getPlayer2Points();

        assertTrue(player1Points > 0);
        assertTrue(player2Points > 0);
    }

    @Test
    public void testDragAndDropCard() {
        // Test drag and drop functionality
        Card card = game.getPlayer1InHandCards().get(0);
        Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.put(Card.DATA_FORMAT, card);
        db.setContent(content);

        VBox board = new VBox();
        setupDragAndDrop(board, Row.PLAYER1_CLOSE_COMBAT);


        board.getOnDragDropped().handle(new javafx.scene.input.DragEvent(null, null, null, db, 0.0, 0.0, 0.0, 0.0, null, null, null, null));

        assertTrue(game.getInGameCards().contains(card));
        assertTrue(board.getChildren().contains(card));
    }

    private void setupDragAndDrop(VBox board, Row... rows) {
        board.setOnDragOver(event -> {
            if (event.getGestureSource() != board && event.getDragboard().hasContent(Card.DATA_FORMAT)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        board.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(Card.DATA_FORMAT)) {
                Card card = (Card) db.getContent(Card.DATA_FORMAT);
                if (card != null) {
                    game.player1PlayCard(card, rows[0]);
                    board.getChildren().add(card);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}
