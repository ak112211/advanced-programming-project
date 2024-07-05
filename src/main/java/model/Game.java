package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.Row;
import enums.cardsinformation.Type;
import javafx.application.Platform;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.InstantaneousAbility;
import model.abilities.openingabilities.OpeningAbility;
import model.abilities.persistentabilities.PersistentAbility;
import model.card.Card;
import model.card.Leader;
import util.CardSerializer;
import util.DeckDeserializer;
import util.LeaderSerializer;
import view.GamePaneController;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Game implements Serializable {
    private static final Random RANDOM = new Random();
    private static final int VETO_TIMES = 2;
    private static final int STARTING_HAND_SIZE = 10;
    private static Game currentGame;

    private int ID;
    private final User player1;
    private final User player2;
    private final Date date;
    private User currentPlayer;
    private boolean isOnline;
    private int player1Points, player2Points;
    private final Leader player1LeaderCard;
    private final Leader player2LeaderCard;
    private final ArrayList<Card> inGameCards;
    private final ArrayList<Card> player1InHandCards;
    private final ArrayList<Card> player2InHandCards;
    private final ArrayList<Card> player1Deck;
    private final ArrayList<Card> player2Deck;
    private final ArrayList<Card> player1GraveyardCards;
    private final ArrayList<Card> player2GraveyardCards;
    private GameStatus status;
    private User winner;
    private GamePaneController gamePaneController;
    private CountDownLatch latch = new CountDownLatch(1);
    private Integer chosenCardIndex = null;
    private boolean vetoForPLayer1Shown = false;
    private boolean vetoForPLayer2Shown = false;
    private boolean hasSwitchedTurn;
    private int threadCount;

    // New score fields
    private int player1Round1;
    private int player1Round2;
    private int player1Round3;
    private int player2Round1;
    private int player2Round2;
    private int player2Round3;
    private int player1FinalScore;
    private int player2FinalScore;

    public Game(User player1, User player2) {
        this(player1, player2, new Date(System.currentTimeMillis()),
                player1.getDeck().getCards(), player2.getDeck().getCards(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                player1.getDeck().getLeader(), player2.getDeck().getLeader(), GameStatus.ACTIVE, null,
                RANDOM.nextBoolean() ? player1 : player2);
    }

    public Game(User player1, User player2, Date date, ArrayList<Card> player1Deck, ArrayList<Card> player2Deck, ArrayList<Card> player1InHandCards, ArrayList<Card> player2InHandCards, ArrayList<Card> player1GraveyardCards, ArrayList<Card> player2GraveyardCards, ArrayList<Card> inGameCards, Leader player1LeaderCard, Leader player2LeaderCard, GameStatus status, User winner, User currentPlayer) {
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;
        this.player1Deck = player1Deck;
        this.player2Deck = player2Deck;
        this.player1InHandCards = player1InHandCards;
        this.player2InHandCards = player2InHandCards;
        this.player1GraveyardCards = player1GraveyardCards;
        this.player2GraveyardCards = player2GraveyardCards;
        this.inGameCards = inGameCards;
        this.player1LeaderCard = player1LeaderCard;
        this.player2LeaderCard = player2LeaderCard;
        this.winner = winner;
        this.currentPlayer = currentPlayer;
        this.status = status;
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
    }

    public void nextTurn() {
        switchSides();
    }

    public void switchSides() {
        currentPlayer = (currentPlayer.equals(player1)) ? player2 : player1;
    }


    public void veto() {
        if (!vetoForPLayer1Shown && isPlayer1Turn()) {
            vetoForPLayer1Shown = true;
            Thread thread = new Thread(this::player1VetoCard);
            thread.start();
            gamePaneController.startTurn();
        } else if (!vetoForPLayer2Shown && !isPlayer1Turn()) {
            vetoForPLayer2Shown = true;
            Thread thread = new Thread(this::player2VetoCard);
            thread.start();
            gamePaneController.startTurn();
        }
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
        Platform.runLater(gamePaneController::startTurn);
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
        Platform.runLater(gamePaneController::startTurn);
    }

    public int calculatePoints(Predicate<Row> predicate) {
        return inGameCards.stream()
               .filter(card -> predicate.test(card.getRow()))
               .mapToInt(Card::getPower)
               .sum();
    }

    public void calculatePoints() {
        for (Card card : getAllCards()) {
            card.setPower(card.getFirstPower());
        }
        PersistentAbility.calculatePowers(inGameCards);
        getAllCards().forEach(Card::setPowerText);
        player1Points = calculatePoints(Row::isPlayer1);
        player2Points = calculatePoints(row -> !row.isPlayer1());
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
            threadCount++;
            Thread thread = new Thread(() -> {
                ((InstantaneousAbility) card.getAbility()).affect(this, card);
                Platform.runLater(gamePaneController::startTurn);
                threadCount--;
                if (threadCount == 0) {
                    hasSwitchedTurn = true;
                    switchSides();
                    Platform.runLater(() -> {
                        calculatePoints();
                        gamePaneController.nextTurn();
                    });
                }
            });
            thread.start();
        }
    }

    public void playCard(Card card, Row row) throws SQLException, IOException {
        hasSwitchedTurn = false;
        if (isPlayer1Turn()) {
            if (!player1PlayCard(card, row)) {
                throw new IllegalArgumentException("card cannot be played");
            }
        } else {
            if (!player2PlayCard(card, row)) {
                throw new IllegalArgumentException("card cannot be played");
            }
        }
        if (!hasSwitchedTurn && threadCount == 0) {
            switchSides();
            Platform.runLater(() -> {
                calculatePoints();
                gamePaneController.nextTurn();
            });
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
        List<Card> finalCards = cards;
        Platform.runLater(() -> {
            gamePaneController.startTurn();
            gamePaneController.showChooseOverlay(false, finalCards);
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(cards.get(chosenCardIndex));
    }

    public Optional<Card> chooseCardOrPass(List<Card> cards) {
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        Platform.runLater(() -> {
            gamePaneController.startTurn();
            gamePaneController.showChooseOverlay(true, cards);
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return chosenCardIndex == null ? null : Optional.of(cards.get(chosenCardIndex));
    }

    public void finishedChoosing(Integer index) {
        chosenCardIndex = index;
        latch.countDown();
        latch = new CountDownLatch(1);
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

    public List<Card> getAllCards() {
        return Stream.of(player1InHandCards, player2InHandCards, player1Deck, player2Deck, inGameCards,
                player1GraveyardCards, player2GraveyardCards).flatMap(ArrayList::stream).toList();
    }

    public void setGamePaneController(GamePaneController gamePaneController) {
        this.gamePaneController = gamePaneController;
    }

    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPlayer1Round1() {
        return player1Round1;
    }

    public void setPlayer1Round1(int player1Round1) {
        this.player1Round1 = player1Round1;
    }

    public int getPlayer1Round2() {
        return player1Round2;
    }

    public void setPlayer1Round2(int player1Round2) {
        this.player1Round2 = player1Round2;
    }

    public int getPlayer1Round3() {
        return player1Round3;
    }

    public void setPlayer1Round3(int player1Round3) {
        this.player1Round3 = player1Round3;
    }

    public int getPlayer2Round1() {
        return player2Round1;
    }

    public void setPlayer2Round1(int player2Round1) {
        this.player2Round1 = player2Round1;
    }

    public int getPlayer2Round2() {
        return player2Round2;
    }

    public void setPlayer2Round2(int player2Round2) {
        this.player2Round2 = player2Round2;
    }

    public int getPlayer2Round3() {
        return player2Round3;
    }

    public void setPlayer2Round3(int player2Round3) {
        this.player2Round3 = player2Round3;
    }

    public int getPlayer1FinalScore() {
        return player1FinalScore;
    }

    public void setPlayer1FinalScore(int player1FinalScore) {
        this.player1FinalScore = player1FinalScore;
    }

    public int getPlayer2FinalScore() {
        return player2FinalScore;
    }

    public void setPlayer2FinalScore(int player2FinalScore) {
        this.player2FinalScore = player2FinalScore;
    }

}
