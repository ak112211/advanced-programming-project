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
    public boolean moveCardToGraveyard(Card card) {
        boolean res = inGameCards.remove(card);
        if (res) {
            if (Row.isPlayer1(card.getRow())) {
                player1GraveyardCards.add(card);
            } else {
                player2GraveyardCards.add(card);
            }
        }
        return res;
    }
    public boolean moveCardBackToHand(Card card) {
        boolean res = inGameCards.remove(card);
        if (res) {
            if (Row.isPlayer1(card.getRow())) {
                player1InHandCards.add(card);
            } else {
                player2InHandCards.add(card);
            }
        }
        return res;
    }
    public boolean player1ResurrectCard(Card card, Row row) {
        boolean res = player1GraveyardCards.remove(card);
        if (res) {
            card.setRow(row);
            inGameCards.add(card);
        }
        return res;
    }
    public boolean player2ResurrectCard(Card card, Row row) {
        boolean res = player2GraveyardCards.remove(card);
        if (res) {
            card.setRow(row);
            inGameCards.add(card);
        }
        return res;
    }
    public void resetGraveyardToDeck(){
        player1Deck.addAll(player1GraveyardCards);
        player1GraveyardCards.clear();
        player2Deck.addAll(player2GraveyardCards);
        player2GraveyardCards.clear();
    }
    public boolean player1PlayCard(Card card, Row row) {
        boolean res = player1InHandCards.remove(card);
        if (res) {
            card.setRow(row);
            inGameCards.add(card);
        }
        return res;
    }
    public boolean player2PlayCard(Card card, Row row) {
        boolean res = player2InHandCards.remove(card);
        if (res) {
            card.setRow(row);
            inGameCards.add(card);
        }
        return res;
    }
    public boolean changeCard(Card card1, Card card2) {
        int i = inGameCards.indexOf(card1);
        if (i == -1) {
            return false;
        }
        inGameCards.set(i, card2);
        return true;
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

