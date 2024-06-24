package model;

import enums.Row;
import javafx.scene.layout.Pane;
import model.abilities.persistentabilities.PersistentAbility;
import model.card.Card;
import model.card.Leader;

import java.util.ArrayList;
import java.util.Date;

public class Game {

    private final User PLAYER1;
    private final User PLAYER2;
    private User currentPlayer;
    private final Date DATE;
    public Pane gamePane;
    private int player1Points, player2Points;
    private Leader player1LeaderCard;
    private Leader player2LeaderCard;
    private ArrayList<Card> inGameCards = new ArrayList<>();
    private ArrayList<Card> player1InHandCards = new ArrayList<>();
    private ArrayList<Card> player2InHandCards = new ArrayList<>();
    private ArrayList<Card>  player1Deck = new ArrayList<>();
    private ArrayList<Card>  player2Deck = new ArrayList<>();
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

    public boolean moveCard(Card card, ArrayList<Card> cards1, ArrayList<Card> cards2) {
        boolean res = cards1.remove(card);
        if (res) {
            cards2.add(card);
        }
        return res;
    }

    public boolean moveCardToGraveyard(Card card) {
        return moveCard(card, inGameCards,
                card.getRow().isPlayer1() ? player1GraveyardCards : player2GraveyardCards);
    }

    public boolean player1PlayCard(Card card, Row row) {
        if (row == null) {
            row = card.getType().getRow(true);
        }
        return moveCard(card, player1InHandCards, inGameCards);
    }

    public boolean player2PlayCard(Card card, Row row) {
        if (row == null) {
            row = card.getType().getRow(false);
        }
        return moveCard(card, player1InHandCards, inGameCards);
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

    public void initializeGameObjects() {
    }

    public void initializeGameObjectsFromSaved() {

    }

    public Card chooseCard(ArrayList<Card> cards) {
        return null; // TODO
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

    public boolean isPlayer1sTurn() {
        return currentPlayer.equals(PLAYER1);
    }

    public boolean isPlayer1Turn() {
        return currentPlayer.equals(PLAYER1);
    }

    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }
}

