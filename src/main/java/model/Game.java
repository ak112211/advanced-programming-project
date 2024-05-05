package model;

import enums.Row;
import model.card.Card;

import java.util.ArrayList;
import java.util.Date;

public class Game {

    private final User PLAYER1;
    private final User PLAYER2;
    private final Date DATE;
    private final ArrayList<Card> inGameCards = new ArrayList<>();
    private final ArrayList<Card> player1InHandCards = new ArrayList<>();
    private final ArrayList<Card> player2InHandCards = new ArrayList<>();
    private final ArrayList<Card> player1Deck = new ArrayList<>();
    private final ArrayList<Card> player2Deck = new ArrayList<>();
    private final ArrayList<Card> player1GraveyardCards = new ArrayList<>();
    private final ArrayList<Card> player2GraveyardCards = new ArrayList<>();
    private GameStatus status;
    private User winner;

    public Game(User player1, User player2, Date date) {
        this.PLAYER1 = player1;
        this.PLAYER2 = player2;
        this.DATE = date;
        this.status = GameStatus.PENDING;
        this.winner = null;
    }

    public User getPLAYER1() {
        return PLAYER1;
    }

    public User getPLAYER2() {
        return PLAYER2;
    }

    public Date getDATE() {
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
            row = card.getTYPE().getRow(true);
        }
        return moveCard(card, player1InHandCards, inGameCards);
    }
    public boolean player2PlayCard(Card card, Row row) {
        if (row == null) {
            row = card.getTYPE().getRow(false);
        }
        return moveCard(card, player1InHandCards, inGameCards);
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

