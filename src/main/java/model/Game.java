package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.Row;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import javafx.application.Platform;
import model.abilities.Ability;
import model.abilities.ejectabilities.EjectAbility;
import model.abilities.instantaneousabilities.Decoy;
import model.abilities.instantaneousabilities.InstantaneousAbility;
import model.abilities.openingabilities.OpeningAbility;
import model.abilities.passiveabilities.CancelLeaderAbility;
import model.abilities.persistentabilities.PersistentAbility;
import model.card.Card;
import model.card.Leader;
import server.DatabaseConnection;
import server.GwentServer;
import util.CardSerializer;
import util.DeckDeserializer;
import util.LeaderSerializer;
import view.GamePaneController;
import model.RoundsInfo.Winner;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static server.GwentServer.sendOutput;

public class Game implements Serializable {
    private static final Random RANDOM = new Random();
    private static final int VETO_TIMES = 2;
    private static final int STARTING_HAND_SIZE = 10;
    private static Game currentGame;

    private transient GamePaneController gamePaneController;
    private transient CountDownLatch latch = new CountDownLatch(1);
    private int player1Points, player2Points;
    private transient boolean isOnline;
    private transient Card chosenCard;
    private transient Thread thread;
    private transient String taskResult;

    // these variables must be saved:
    private List<Card> cardChoices; // gharare anidsash az card haye dige bashe yani gharare 2 ta reference be ye chiz eshare konan. vali age sakhtete mohem nist age ye carde dige besazi va reference ha 2 ta chize motafeto neshoon bedan
    private String task;
    private int ID;
    private boolean isPublic;
    private final User player1;
    private final User player2;
    private final Date date;
    private boolean isPlayer1Turn;
    private boolean vetoForPLayer1Shown;
    private boolean vetoForPLayer2Shown;
    private boolean player1HasPassed;
    private boolean player2HasPassed;
    private boolean player1UsedLeaderAbility;
    private boolean player2UsedLeaderAbility;
    private final Faction player1Faction;
    private final Faction player2Faction;
    private final Leader player1LeaderCard;
    private final Leader player2LeaderCard;
    private final ArrayList<Card> inGameCards;
    private final ArrayList<Card> player1InHandCards;
    private final ArrayList<Card> player2InHandCards;
    private final ArrayList<Card> player1Deck;
    private final ArrayList<Card> player2Deck;
    private final ArrayList<Card> player1GraveyardCards;
    private final ArrayList<Card> player2GraveyardCards;
    private final RoundsInfo roundsInfo;
    private GameStatus status;

    public Game(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.date = new Date(System.currentTimeMillis());

        this.isPlayer1Turn = RANDOM.nextBoolean();
        this.vetoForPLayer1Shown = false;
        this.vetoForPLayer2Shown = false;
        this.player1HasPassed = false;
        this.player2HasPassed = false;
        this.player1UsedLeaderAbility = false;
        this.player2UsedLeaderAbility = false;

        this.player1Deck = player1.getDeck().getCards();
        this.player2Deck = player2.getDeck().getCards();
        this.player1InHandCards = new ArrayList<>();
        this.player2InHandCards = new ArrayList<>();
        this.player1GraveyardCards = new ArrayList<>();
        this.player2GraveyardCards = new ArrayList<>();
        this.inGameCards = new ArrayList<>();
        this.player1LeaderCard = player1.getDeck().getLeader();
        this.player2LeaderCard = player2.getDeck().getLeader();
        this.player1Faction = player1.getDeck().getFaction();
        this.player2Faction = player2.getDeck().getFaction();

        this.status = GameStatus.ACTIVE;
        this.roundsInfo = new RoundsInfo();
    }

    public Game(int ID, User player1, User player2, Date date, boolean isPlayer1Turn, boolean vetoForPLayer1Shown, boolean vetoForPLayer2Shown, boolean player1HasPassed, boolean player2HasPassed, boolean player1UsedLeaderAbility, boolean player2UsedLeaderAbility, ArrayList<Card> player1Deck, ArrayList<Card> player2Deck, ArrayList<Card> player1InHandCards, ArrayList<Card> player2InHandCards, ArrayList<Card> player1GraveyardCards, ArrayList<Card> player2GraveyardCards, ArrayList<Card> inGameCards, Leader player1LeaderCard, Leader player2LeaderCard, Faction player1Faction, Faction player2Faction, GameStatus status, RoundsInfo roundsInfo, int player1Points, int player2Points) {
        this.ID = ID;
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;

        this.isPlayer1Turn = isPlayer1Turn;
        this.vetoForPLayer1Shown = vetoForPLayer1Shown;
        this.vetoForPLayer2Shown = vetoForPLayer2Shown;
        this.player1HasPassed = player1HasPassed;
        this.player2HasPassed = player2HasPassed;
        this.player1UsedLeaderAbility = player1UsedLeaderAbility;
        this.player2UsedLeaderAbility = player2UsedLeaderAbility;

        this.player1Deck = player1Deck;
        this.player2Deck = player2Deck;
        this.player1InHandCards = player1InHandCards;
        this.player2InHandCards = player2InHandCards;
        this.player1GraveyardCards = player1GraveyardCards;
        this.player2GraveyardCards = player2GraveyardCards;
        this.inGameCards = inGameCards;
        this.player1LeaderCard = player1LeaderCard;
        this.player2LeaderCard = player2LeaderCard;
        this.player1Faction = player1Faction;
        this.player2Faction = player2Faction;

        this.status = status;
        this.roundsInfo = roundsInfo;

        this.player1Points = player1Points;
        this.player2Points = player2Points;
    }

    public void initializeGameObjectsFromSaved() {

    }

    // functions for game main logics:

    public void startGame() {
        thread = new Thread(this::start);
        thread.start();
    }

    private void start() {
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            player1GetRandomCard();
            player2GetRandomCard();
        }
        OpeningAbility.StartRound(this);
        isPlayer1Turn = true;
        player1VetoCard();
        player2VetoCard();
        while (!roundsInfo.isGameFinished(this)) {
            player2HasPassed = false;
            player1HasPassed = false;
            checkHasAnythingToDo();
            if (player1HasPassed && player2HasPassed) {
                break;
            }
            if (isPlayer1Turn && player1HasPassed) {
                isPlayer1Turn = false;
            } else if (!isPlayer1Turn && player2HasPassed) {
                isPlayer1Turn = true;
            }
            while (!player1HasPassed || !player2HasPassed) {
                try {
                    startTurn();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                calculatePoints();
                switchSides();
            }
            roundsInfo.finishRound(player1Points, player2Points);
            resetCards();
        }
        try {
            endGame();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void endGame() throws SQLException {
        status = GameStatus.COMPLETED;
        task = "show end screen";
        giveTask(); // it doesn't need to be handled
    }

    private void startTurn() throws SQLException {
        EjectAbility.startTurnAffect(this);
        task = "play";
        handleTask();
    }

    private void switchSides() {
        checkHasAnythingToDo();
        if (isPlayer1Turn && !player2HasPassed) {
            isPlayer1Turn = false;
        } else if (!isPlayer1Turn && !player1HasPassed) {
            isPlayer1Turn = true;
        }
    }

    private void checkHasAnythingToDo() {
        if (player1InHandCards.isEmpty() && !player1CanUseLeaderAbility()) {
            player1HasPassed = true;
        }
        if (player2InHandCards.isEmpty() && !player2CanUseLeaderAbility()) {
            player2HasPassed = true;
        }
    }

    private void player1VetoCard() {
        for (int i = 0; i < VETO_TIMES; i++) {
            Optional<Card> chosenCard = chooseCardOrPass(player1InHandCards);
            if (chosenCard == null) {
                return;
            }
            player1GetRandomCard();
            moveCard(chosenCard.orElseThrow(), player1InHandCards, player1Deck);
        }
    }

    private void player2VetoCard() {
        for (int i = 0; i < VETO_TIMES; i++) {
            Optional<Card> chosenCard = chooseCardOrPass(player2InHandCards);
            if (chosenCard == null) {
                return;
            }
            player2GetRandomCard();
            moveCard(chosenCard.orElseThrow(), player2InHandCards, player2Deck);
        }
    }

    private void calculatePoints() {
        for (Card card : getAllCards()) {
            card.setPower(card.getFirstPower());
        }
        PersistentAbility.calculatePowers(inGameCards);
        player1Points = calculatePoints(Row::isPlayer1);
        player2Points = calculatePoints(row -> !row.isPlayer1());
    }

    private void resetCards() {
        Card player1CardToKeep = null, player2CardToKeep = null;
        if (player1Faction == Faction.MONSTER) {
            player1CardToKeep = chooseRandomCard(inGameCards.stream().filter(card -> card.getRow().isPlayer1()).toList(),
                    true).orElse(null);
        }
        if (player2Faction == Faction.MONSTER) {
            player2CardToKeep = chooseRandomCard(inGameCards.stream().filter(card -> !card.getRow().isPlayer1()).toList(),
                    true).orElse(null);
        }

        while (!inGameCards.isEmpty()) {
            Card card = inGameCards.getFirst();
            if (card != player1CardToKeep && card != player2CardToKeep) {
                moveCardToGraveyard(card);
            }
        }

        Winner lastWinner = roundsInfo.getRoundWinner(roundsInfo.getCurrentRound() - 1, this);
        if (player1Faction == Faction.REALMS_NORTHERN && lastWinner == Winner.PLAYER1) {
            player1GetRandomCard();
        } else if (player2Faction == Faction.REALMS_NORTHERN && lastWinner == Winner.PLAYER2) {
            player2GetRandomCard();
        }

        if (roundsInfo.getCurrentRound() == 3) {
            if (player1Faction == Faction.SKELLIGE) {
                player1GetRandomCard();
                player1GetRandomCard();
            }
            if (player2Faction == Faction.SKELLIGE) {
                player2GetRandomCard();
                player2GetRandomCard();
            }
        }
    }

    // functions for play tasks:

    private void useLeaderAbility() {
        if (isPlayer1Turn) {
            if (player1CanUseLeaderAbility()) {
                player1UsedLeaderAbility = true;
                ((InstantaneousAbility) player1LeaderCard.getAbility()).affect(this, null);
            }
        } else {
            if (player2CanUseLeaderAbility()) {
                player2UsedLeaderAbility = true;
                ((InstantaneousAbility) player2LeaderCard.getAbility()).affect(this, null);
            }
        }
        latch.countDown();
    }

    private void pass() {
        if (isPlayer1Turn) {
            player1HasPassed = true;
        } else {
            player2HasPassed = true;
        }
        latch.countDown();
    }

    private void playCard(Card card, Row row) {
        if (isPlayer1Turn) {
            if (!player1PlayCard(card, row)) {
                throw new IllegalArgumentException("card cannot be played");
            }
        } else {
            if (!player2PlayCard(card, row)) {
                throw new IllegalArgumentException("card cannot be played");
            }
        }
        latch.countDown();
    }

    // functions that say if the player can do a play task:

    public boolean player1CanUseLeaderAbility() {
        return !(player2LeaderCard.getAbility() instanceof CancelLeaderAbility) &&
                player1LeaderCard.getAbility() instanceof InstantaneousAbility &&
                !player1UsedLeaderAbility;
    }

    public boolean player2CanUseLeaderAbility() {
        return !(player1LeaderCard.getAbility() instanceof CancelLeaderAbility) &&
                player2LeaderCard.getAbility() instanceof InstantaneousAbility &&
                !player2UsedLeaderAbility;
    }

    public boolean canPlay(Card card, Row row) {
        return card.getType() != Type.SPELL ||
                inGameCards.stream().noneMatch(inGameCard -> inGameCard.same(card) && inGameCard.getRow() == row);
    }


    // Functions that move a card:

    public void moveCard(Card card, ArrayList<Card> cards1, ArrayList<Card> cards2) {
        if (cards1 == cards2) {
            throw new IllegalArgumentException("Cards1 and cards2 are the same");
        }
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
        if (cards1 == inGameCards && card.getAbility() instanceof EjectAbility) {
            ((EjectAbility) card.getAbility()).affect(card);
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

    public Optional<Card> chooseCard(List<Card> cards, boolean onlyAffectables) {
        if (onlyAffectables) {
            cards = cards.stream().filter(Ability::canBeAffected).toList();
        }
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        cardChoices = cards;
        task = "choose false";
        try {
            handleTask();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(chosenCard);
    }

    public Optional<Card> chooseCardOrPass(List<Card> cards) {
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        cardChoices = cards;
        task = "choose true";
        try {
            handleTask();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chosenCard == null ? null : Optional.of(chosenCard);
    }

    // Getter and setter functions:

    public static void setCurrentGame(Game game) {
        currentGame = game;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentUser(User user) {
        if (user == player1) {
            isPlayer1Turn = true;
        } else if (user == player2) {
            isPlayer1Turn = false;
        } else {
            throw new IllegalArgumentException("User is not a player");
        }
    }

    public void setPlayer1Turn(boolean player1Turn) {
        this.isPlayer1Turn = player1Turn;
    }

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
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

    public User getWinnerUser() {
        if (roundsInfo.getWinner() == Winner.PLAYER1) {
            return player1;
        }
        if (roundsInfo.getWinner() == Winner.PLAYER2) {
            return player2;
        }
        return null;
    }

    public Winner getWinner() {
        return roundsInfo.getWinner();
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public int getPlayer2Points() {
        return player2Points;
    }

    public Faction getPlayer1Faction() {
        return player1Faction;
    }

    public Faction getPlayer2Faction() {
        return player2Faction;
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

    public List<Card> getCardChoices() {
        return cardChoices;
    }

    public List<Card> getAllCards() {
        return Stream.of(player1InHandCards, player2InHandCards, player1Deck, player2Deck, inGameCards,
                player1GraveyardCards, player2GraveyardCards).flatMap(ArrayList::stream).toList();
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setGamePaneController(GamePaneController gamePaneController) {
        this.gamePaneController = gamePaneController;
    }

    public String getTask() {
        return task;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public RoundsInfo getRoundsInfo() {
        return roundsInfo;
    }

    public int calculatePoints(Predicate<Row> predicate) {
        return inGameCards.stream()
                .filter(card -> predicate.test(card.getRow()))
                .mapToInt(Card::getPower)
                .sum();
    }

    // functions for handling tasks:

    public void receiveTaskResult(String taskResult, User sender) { // done in javafx thread
        if (isOnline) {
            if ((isPlayer1Turn && !sender.equals(player1)) || (!isPlayer1Turn && !sender.equals(player2))) {
                System.out.println("wrong user send taskResult");
                return;
            }
        }
        this.taskResult = taskResult;
        latch.countDown();
    }

    private void handleTaskResult() {
        if (taskResult == null) {
            System.out.println("task result is still null");
        }
        String[] args = taskResult.split(" ");
        if (args[0].equals("chose") && task.startsWith("choose ")) {
            if (args[1].equals("null")) {
                if (task.endsWith("false")) {
                    System.out.println("can't pass choosing");
                } else {
                    chosenCard = null;
                    task = null;
                }
            } else {
                chosenCard = cardChoices.get(Integer.parseInt(args[1]));
                task = null;
            }
        } else if (task.equals("play")) {
            switch (args[0]) {
                case "play" -> {
                    Card card = CardsPlace.IN_HAND.getPlayerCards(this).get(Integer.parseInt(args[1]));
                    Row row = Row.valueOf(args[2]);
                    if (card.getAbility() instanceof Decoy) {
                        Card returnCard = CardsPlace.IN_HAND.getPlayerCards(this).get(Integer.parseInt(args[3]));
                        ((Decoy) card.getAbility()).setReturnCard(returnCard);
                    }
                    playCard(card, row);
                    task = null;
                }
                case "pass" -> {
                    pass();
                    task = null;
                }
                case "leader" -> {
                    useLeaderAbility();
                    task = null;
                }
                default -> {
                    System.out.println("invalid play task result");
                    System.out.println("task result: " + taskResult);
                }
            }
        } else {
            System.out.println("invalid task result");
            System.out.println("task: " + task);
            System.out.println("task result: " + taskResult);
        }
        taskResult = null;
    }

    private void handleTask() throws SQLException { // this and any other private functions run in game thread
        while (task != null) {
            giveTask();
            try {
                latch = new CountDownLatch(1);
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            handleTaskResult();
        }
    }

    private void giveTask() throws SQLException {
        DatabaseConnection.updateGame(this);
        if (isOnline) {
            for (String username: DatabaseConnection.getUsernames()) {
                sendOutput(username, "online game move made " + this.ID);
            }
        } else {
            Platform.runLater(gamePaneController::doTask);
        }
    }

    // Saving functions: ali ina ro nemikhay pakeshoon kon

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

    // GameStatus enum

    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
