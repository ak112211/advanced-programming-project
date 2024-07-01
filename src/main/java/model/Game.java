package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.Row;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.InstantaneousAbility;
import model.abilities.openingabilities.OpeningAbility;
import model.abilities.persistentabilities.PersistentAbility;
import model.card.Card;
import model.card.Leader;
import util.CardSerializer;
import util.DeckDeserializer;
import util.LeaderSerializer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public class Game implements Serializable {
    private static final Random RANDOM = new Random();
    private static final int VETO_TIMES = 2;
    private static final int STARTING_HAND_SIZE = 10;
    private final User player1;
    private final User player2;
    private User currentPlayer;
    private final Date date;
    private boolean isOnline;
    private int player1Points, player2Points;
    private Leader player1LeaderCard;
    private Leader player2LeaderCard;
    private final ArrayList<Card> inGameCards = new ArrayList<>();
    private final ArrayList<Card> player1InHandCards = new ArrayList<>();
    private final ArrayList<Card> player2InHandCards = new ArrayList<>();
    private final ArrayList<Card> player1Deck;
    private final ArrayList<Card> player2Deck;
    private final ArrayList<Card> player1GraveyardCards = new ArrayList<>();
    private final ArrayList<Card> player2GraveyardCards = new ArrayList<>();
    private GameStatus status = GameStatus.PENDING;
    private User winner = null;
    private static Game currentGame;

    public Game(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        player1Deck = player1.getDeck().getCards();
        player2Deck = player2.getDeck().getCards();
        player1LeaderCard = player1.getDeck().getLeader();
        player2LeaderCard = player2.getDeck().getLeader();
        date = new Date(System.currentTimeMillis());
    }

    public void initializeGameObjects() {
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            player1GetRandomCard();
            player2GetRandomCard();
        }
        OpeningAbility.StartRound(this);
        currentPlayer = player1;
    }

    public void initializeGameObjectsFromSaved() {
        //TODO
    }

    public void nextTurn() {
        switchSides();
    }

    public void switchSides() {
        currentPlayer = (currentPlayer.equals(player1)) ? player2 : player1;
    }

    public void player1VetoCard() {
        for (int i = 0; i < VETO_TIMES; i++) {
            Optional<Card> chosenCard = chooseCardOrPass(player1InHandCards);
            if (chosenCard == null) {
                return;
            }
            player1GetRandomCard();
            moveCard(chosenCard.orElseThrow(), player1InHandCards, player1Deck);
        }
    }

    public void player2VetoCard() {
        for (int i = 0; i < VETO_TIMES; i++) {
            Optional<Card> chosenCard = chooseCardOrPass(player2InHandCards);
            if (chosenCard == null) {
                return;
            }
            player2GetRandomCard();
            moveCard(chosenCard.orElseThrow(), player2InHandCards, player2Deck);
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

    public boolean canPlay(Card card, Row row) {
        return card.getType() != Type.SPELL ||
                inGameCards.stream().noneMatch(inGameCard -> inGameCard.same(card) && inGameCard.getRow() == row);
    }

    // Functions that move a card:

    public void moveCard(Card card, ArrayList<Card> cards1, ArrayList<Card> cards2) {
        if (!cards1.remove(card)) {
            throw new IllegalArgumentException("Card doesn't exist there");
        }
        if (cards2.contains(card)) {
            throw new IllegalArgumentException("Card exists in destination");
        }
        cards2.add(card);
        if (cards2 == inGameCards && card.getAbility() instanceof InstantaneousAbility) {
            ((InstantaneousAbility) card.getAbility()).affect(this, card);
        }
    }

    public boolean player1PlayCard(Card card, Row row) {
        if (row == null) {
            row = card.getDefaultRow(true);
        }
        if (canPlay(card, row)) {
            card.setRow(row);
            moveCard(card, player1InHandCards, inGameCards);
            return true;
        }
        return false;
    }

    public boolean player2PlayCard(Card card, Row row) {
        if (row == null) {
            row = card.getDefaultRow(false);
        }
        if (canPlay(card, row)) {
            card.setRow(row);
            moveCard(card, player2InHandCards, inGameCards);
            return true;
        }
        return false;
    }

    public void player1GetRandomCard() {
        if (!player1Deck.isEmpty()) {
            moveCard(chooseRandomCard(player1Deck, false).orElseThrow(), player1Deck, player1InHandCards);
        }
    }

    public void player2GetRandomCard() {
        if (!player2Deck.isEmpty()) {
            moveCard(chooseRandomCard(player2Deck, false).orElseThrow(), player2Deck, player2InHandCards);
        }
    }

    public void moveCardToGraveyard(Card card) {
        moveCard(card, inGameCards, card.getRow().isPlayer1() ? player1GraveyardCards : player2GraveyardCards);
    }

    // Functions that choose a card:

    public Optional<Card> chooseCard(List<Card> cards, boolean onlyAffectables, boolean random) {
        if (random) {
            return chooseRandomCard(cards, onlyAffectables);
        } else {
            return chooseCard(cards, onlyAffectables);
        }
    }

    public static Optional<Card> chooseRandomCard(List<Card> cards, boolean onlyAffectables) {
        if (onlyAffectables) {
            cards = cards.stream().filter(Ability::canBeAffected).toList();
        }
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cards.get(RANDOM.nextInt(cards.size())));
    }

    public Optional<Card> chooseCard(List<Card> cards, boolean onlyAffectables) { // static?
        if (onlyAffectables) {
            cards = cards.stream().filter(Ability::canBeAffected).toList();
        }
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        return Optional.empty(); // TODO
    }

    public Optional<Card> chooseCardOrPass(List<Card> cards) {
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        return null; // TODO
    }
    
    // Saving functions:

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Card.class, new CardSerializer())
                .registerTypeAdapter(Leader.class, new LeaderSerializer())
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    public static Game fromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Card.class, new CardSerializer())
                .registerTypeAdapter(Leader.class, new LeaderSerializer())
                .registerTypeAdapter(Deck.class, new DeckDeserializer())
                .create();
        return gson.fromJson(json, Game.class);
    }
    
    // Getter and setter functions:

    public static void setCurrentGame(Game game) {
        currentGame = game;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentPlayer(User currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isPlayer1Turn() {
        return currentPlayer.equals(player1);
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public Date getDate() {
        return date;
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

    public Leader getPlayer1LeaderCard() {
        return player1LeaderCard;
    }

    public Leader getPlayer2LeaderCard() {
        return player2LeaderCard;
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setPlayer1LeaderCard(Leader PLAYER1_LEADER_CARD) {
        this.player1LeaderCard = PLAYER1_LEADER_CARD;
    }

    public void setPlayer2LeaderCard(Leader PLAYER2_LEADER_CARD) {
        this.player2LeaderCard = PLAYER2_LEADER_CARD;
    }

    public List<Card> getAllCards() {
        return Stream.of(player1InHandCards, player2InHandCards, player1Deck, player2Deck, inGameCards,
                player1GraveyardCards, player2GraveyardCards).flatMap(ArrayList::stream).toList();
    }

    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }
}
