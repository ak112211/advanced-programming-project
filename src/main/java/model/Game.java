package model;

import enums.Row;
import enums.cardsinformation.Type;
import javafx.scene.layout.Pane;
import model.abilities.Ability;
import model.abilities.persistentabilities.PersistentAbility;
import model.card.Card;
import model.card.Leader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Game implements Serializable {
    private static final Random RANDOM = new Random();
    private final User PLAYER1;
    private final User PLAYER2;
    private User currentPlayer;
    private final Date DATE;
    public transient Pane gamePane;
    private int player1Points, player2Points;
    private Leader player1LeaderCard;
    private Leader player2LeaderCard;
    private ArrayList<Card> inGameCards = new ArrayList<>();
    private ArrayList<Card> player1InHandCards = new ArrayList<>();
    private ArrayList<Card> player2InHandCards = new ArrayList<>();
    private ArrayList<Card> player1Deck = new ArrayList<>();
    private ArrayList<Card> player2Deck = new ArrayList<>();
    private ArrayList<Card> player1GraveyardCards = new ArrayList<>();
    private ArrayList<Card> player2GraveyardCards = new ArrayList<>();
    private GameStatus status = GameStatus.PENDING;
    private User winner = null;
    private static Game currentGame;

    public Game(User player1, User player2, Date date) {
        PLAYER1 = player1;
        PLAYER2 = player2;
        player1Deck = player1.getDeck().getCards();
        player2Deck = player2.getDeck().getCards();
        player1LeaderCard = player1.getLeader();
        player2LeaderCard = player2.getLeader();
        DATE = date;
    }

    public static void setCurrentGame(Game game) {
        currentGame = game;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public User getPlayer1() {
        return PLAYER1;
    }

    public User getPlayer2() {
        return PLAYER2;
    }

    public Date getDate() {
        return DATE;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public int getPlayer2Points() {
        return player2Points;
    }

    public void moveCard(Card card, ArrayList<Card> cards1, ArrayList<Card> cards2) {
        if (!cards1.remove(card)) {
            throw new RuntimeException("Card doesn't exist there");
        }
        cards2.add(card);
    }

    public void moveCardToGraveyard(Card card) {
        moveCard(card, inGameCards, card.getRow().isPlayer1() ? player1GraveyardCards : player2GraveyardCards);
    }

    public boolean canPlay(Card card) {
        return card.getType() != Type.SPELL ||
                inGameCards.stream().noneMatch(inGameCard -> card.same(inGameCard) && card.sameRow(inGameCard));
    }

    public boolean player1PlayCard(Card card, Row row) {
        if (row == null) {
            card.setDefaultRow(true);
        } else {
            card.setRow(row);
        }
        if (canPlay(card)) {
            moveCard(card, player1InHandCards, inGameCards);
            return true;
        }
        return false;
    }

    public boolean player2PlayCard(Card card, Row row) {
        if (row == null) {
            card.setDefaultRow(false);
        } else {
            card.setRow(row);
        }
        if (canPlay(card)) {
            moveCard(card, player2InHandCards, inGameCards);
            return true;
        }
        return false;
    }

    public void player1GetRandomCard() {
        if (!player1Deck.isEmpty()) {
            moveCard(chooseRandomCard(player1Deck,false), player1Deck, player1InHandCards);
        }
    }

    public void player2GetRandomCard() {
        if (!player2Deck.isEmpty()) {
            moveCard(chooseRandomCard(player2Deck,false), player2Deck, player2InHandCards);
        }
    }

    public void calculatePoints() {
        PersistentAbility.calculatePowers(inGameCards);
        player1Points = inGameCards.stream()
                .filter(card -> card.getRow().isPlayer1())
                .mapToInt(Card::getPower)
                .sum();
        player2Points = inGameCards.stream()
                .filter(card -> !card.getRow().isPlayer1())
                .mapToInt(Card::getPower)
                .sum();
    }

    public void nextTurn() {
        switchSides();
    }

    public void switchSides() {
        currentPlayer = (currentPlayer.equals(PLAYER1)) ? PLAYER2 : PLAYER1;
    }

    public void initializeGameObjects() {
    }

    public void initializeGameObjectsFromSaved() {
    }

    public Card chooseCard(List<Card> cards, boolean onlyAffectables) { // static?
        if (onlyAffectables) {
            cards = cards.stream().filter(Ability::canBeAffected).toList();
        }
        if (cards.isEmpty()) {
            return null;
        }
        return null; // TODO
    }

    public static Card chooseRandomCard(List<Card> cards, boolean onlyAffectables) {
        if (onlyAffectables) {
            cards = cards.stream().filter(Ability::canBeAffected).toList();
        }
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(RANDOM.nextInt(cards.size()));
    }

    public Card chooseCard(List<Card> cards, boolean onlyAffectables, boolean random) {
        if (random) {
            return chooseRandomCard(cards, onlyAffectables);
        } else {
            return chooseCard(cards, onlyAffectables);
        }
    }

    public Leader getPlayer1LeaderCard() {
        return player1LeaderCard;
    }

    public Leader getPlayer2LeaderCard() {
        return player2LeaderCard;
    }

    public void setCurrentPlayer(User currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isPlayer1Turn() {
        return currentPlayer.equals(PLAYER1);
    }

    public void player1VetoCard() {
        Card chosenCard = chooseCard(player1InHandCards, false);
        player1GetRandomCard();
        moveCard(chosenCard, player1InHandCards, player1Deck);
    }

    public void player2VetoCard() {
        Card chosenCard = chooseCard(player2InHandCards, false);
        player2GetRandomCard();
        moveCard(chosenCard, player2InHandCards, player2Deck);
    }

    public void passVeto() {
        nextTurn();
    }

    public ArrayList<Card> getInGameCards() {
        return inGameCards;
    }

    public ArrayList<Card> getPlayer1InHandCards() {
        return player1InHandCards;
    }

    public ArrayList<Card> getPlayer2InHandCards() {
        return player2InHandCards;
    }

    public ArrayList<Card> getPlayer1Deck() {
        return player1Deck;
    }

    public ArrayList<Card> getPlayer2Deck() {
        return player2Deck;
    }

    public ArrayList<Card> getPlayer1GraveyardCards() {
        return player1GraveyardCards;
    }

    public ArrayList<Card> getPlayer2GraveyardCards() {
        return player2GraveyardCards;
    }

    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }
}
