package model;

import enums.Row;
import enums.cardsinformation.Type;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.InstantaneousAbility;
import model.abilities.openingabilities.OpeningAbility;
import model.abilities.persistentabilities.PersistentAbility;
import model.card.Card;
import model.card.Leader;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public class Game implements Serializable {
    private static final Random RANDOM = new Random();
    private static final int VETO_TIMES = 2;
    private static final int STARTING_HAND_SIZE = 10;
    private final User PLAYER1;
    private final User PLAYER2;
    private User currentPlayer;
    private final Date DATE;
    private int player1Points, player2Points;
    private final Leader PLAYER1_LEADER_CARD;
    private final Leader PLAYER2_LEADER_CARD;
    private final ArrayList<Card> IN_GAME_CARDS = new ArrayList<>();
    private final ArrayList<Card> PLAYER1_IN_HAND_CARDS = new ArrayList<>();
    private final ArrayList<Card> PLAYER2_IN_HAND_CARDS = new ArrayList<>();
    private ArrayList<Card> player1Deck;
    private ArrayList<Card> player2Deck;
    private final ArrayList<Card> PLAYER1_GRAVEYARD_CARDS = new ArrayList<>();
    private final ArrayList<Card> PLAYER2_GRAVEYARD_CARDS = new ArrayList<>();
    private GameStatus status = GameStatus.PENDING;
    private User winner = null;
    private static Game currentGame;

    public Game(User player1, User player2) {
        PLAYER1 = player1;
        PLAYER2 = player2;
        player1Deck = player1.getDeck().getCards();
        player2Deck = player2.getDeck().getCards();
        PLAYER1_LEADER_CARD = player1.getDeck().getLeader();
        PLAYER2_LEADER_CARD = player2.getDeck().getLeader();
        DATE = new Date(System.currentTimeMillis());
    }

    public void initializeGameObjects() {
        player1Deck = PLAYER1.getDeck().getCards();
        player2Deck = PLAYER2.getDeck().getCards();
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            player1GetRandomCard();
            player2GetRandomCard();
        }
        OpeningAbility.StartRound(this);
        currentPlayer = PLAYER1;
    }

    public void initializeGameObjectsFromSaved() {
        //TODO
    }

    public void nextTurn() {
        switchSides();
    }

    public void switchSides() {
        currentPlayer = (currentPlayer.equals(PLAYER1)) ? PLAYER2 : PLAYER1;
    }

    public void player1VetoCard() {
        for (int i = 0; i < VETO_TIMES; i++) {
            Optional<Card> chosenCard = chooseCardOrPass(PLAYER1_IN_HAND_CARDS);
            if (chosenCard == null) {
                return;
            }
            player1GetRandomCard();
            moveCard(chosenCard.orElseThrow(), PLAYER1_IN_HAND_CARDS, player1Deck);
        }
    }

    public void player2VetoCard() {
        for (int i = 0; i < VETO_TIMES; i++) {
            Optional<Card> chosenCard = chooseCardOrPass(PLAYER2_IN_HAND_CARDS);
            if (chosenCard == null) {
                return;
            }
            player2GetRandomCard();
            moveCard(chosenCard.orElseThrow(), PLAYER2_IN_HAND_CARDS, player2Deck);
        }
    }

    public void calculatePoints() {
        PersistentAbility.calculatePowers(IN_GAME_CARDS);
        player1Points = IN_GAME_CARDS.stream()
                .filter(card -> card.getRow().isPlayer1())
                .mapToInt(Card::getPower)
                .sum();
        player2Points = IN_GAME_CARDS.stream()
                .filter(card -> !card.getRow().isPlayer1())
                .mapToInt(Card::getPower)
                .sum();
    }

    public boolean canPlay(Card card, Row row) {
        return card.getType() != Type.SPELL ||
                IN_GAME_CARDS.stream().noneMatch(inGameCard -> inGameCard.same(card) && inGameCard.getRow() == row);
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
        if (cards2 == IN_GAME_CARDS && card.getAbility() instanceof InstantaneousAbility) {
            ((InstantaneousAbility) card.getAbility()).affect(this, card);
        }
    }

    public boolean player1PlayCard(Card card, Row row) {
        if (row == null) {
            row = card.getDefaultRow(true);
        }
        if (canPlay(card, row)) {
            card.setRow(row);
            moveCard(card, PLAYER1_IN_HAND_CARDS, IN_GAME_CARDS);
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
            moveCard(card, PLAYER2_IN_HAND_CARDS, IN_GAME_CARDS);
            return true;
        }
        return false;
    }

    public void player1GetRandomCard() {
        if (!player1Deck.isEmpty()) {
            moveCard(chooseRandomCard(player1Deck, false).orElseThrow(), player1Deck, PLAYER1_IN_HAND_CARDS);
        }
    }

    public void player2GetRandomCard() {
        if (!player2Deck.isEmpty()) {
            moveCard(chooseRandomCard(player2Deck, false).orElseThrow(), player2Deck, PLAYER2_IN_HAND_CARDS);
        }
    }

    public void moveCardToGraveyard(Card card) {
        moveCard(card, IN_GAME_CARDS, card.getRow().isPlayer1() ? PLAYER1_GRAVEYARD_CARDS : PLAYER2_GRAVEYARD_CARDS);
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
        return currentPlayer.equals(PLAYER1);
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

    public Leader getPlayer1LeaderCard() {
        return PLAYER1_LEADER_CARD;
    }

    public Leader getPlayer2LeaderCard() {
        return PLAYER2_LEADER_CARD;
    }

    public ArrayList<Card> getInGameCards() {
        return IN_GAME_CARDS;
    }

    public ArrayList<Card> getPlayer1InHandCards() {
        return PLAYER1_IN_HAND_CARDS;
    }

    public ArrayList<Card> getPlayer2InHandCards() {
        return PLAYER2_IN_HAND_CARDS;
    }

    public ArrayList<Card> getPlayer1Deck() {
        return player1Deck;
    }

    public ArrayList<Card> getPlayer2Deck() {
        return player2Deck;
    }

    public ArrayList<Card> getPlayer1GraveyardCards() {
        return PLAYER1_GRAVEYARD_CARDS;
    }

    public ArrayList<Card> getPlayer2GraveyardCards() {
        return PLAYER2_GRAVEYARD_CARDS;
    }

    public List<Card> getAllCards() {
        return Stream.of(PLAYER1_IN_HAND_CARDS, PLAYER2_IN_HAND_CARDS, player1Deck, player2Deck, IN_GAME_CARDS,
                PLAYER1_GRAVEYARD_CARDS, PLAYER2_GRAVEYARD_CARDS).flatMap(ArrayList::stream).toList();
    }

    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }
}
