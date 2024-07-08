package model;

import enums.Row;
import enums.cards.NeutralCards;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import javafx.application.Platform;
import model.abilities.Ability;
import model.abilities.ejectabilities.EjectAbility;
import model.abilities.instantaneousabilities.*;
import model.abilities.openingabilities.OpeningAbility;
import model.abilities.passiveabilities.CancelLeaderAbility;
import model.abilities.persistentabilities.PersistentAbility;
import model.card.Card;
import model.card.Leader;
//import server.DatabaseConnection;
//import server.GwentServer;
import util.DatabaseConnection;
import view.GamePaneController;
import model.RoundsInfo.Winner;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;
import java.util.stream.Stream;

//import static server.GwentServer.sendOutput;

public class Game implements Serializable, Cloneable {
    private static final Random RANDOM = new Random();
    private static final int VETO_TIMES = 2;
    private static final int STARTING_HAND_SIZE = 10;
    private static Game currentGame;

    private transient GamePaneController gamePaneController;
    private transient CountDownLatch latch = new CountDownLatch(1);
    private transient int player1Points, player2Points;
    private transient boolean isOnline;
    private transient Thread thread;
    private transient Card chosenCard;
    private transient String taskResult;
    private transient boolean fromSaved;

    // these variables must be saved:
    private int ID;
    private boolean isPublic;
    private final User player1;
    private final User player2;
    private final Date date;
    private boolean isPlayer1Turn;
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
    private GameStatus status; // be kar nayoomad
    // these variables must be send to clients but don't have to be saved:
    private String task;
    private transient List<Card> cardChoices;

    public Game(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.date = new Date(System.currentTimeMillis());

        this.isPlayer1Turn = RANDOM.nextBoolean();
        this.fromSaved = false;
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

        this.status = GameStatus.PENDING;
        this.roundsInfo = new RoundsInfo();
    }

    public Game(int ID, User player1, User player2, Date date, boolean isPlayer1Turn, boolean player1HasPassed, boolean player2HasPassed, boolean player1UsedLeaderAbility, boolean player2UsedLeaderAbility, ArrayList<Card> player1Deck, ArrayList<Card> player2Deck, ArrayList<Card> player1InHandCards, ArrayList<Card> player2InHandCards, ArrayList<Card> player1GraveyardCards, ArrayList<Card> player2GraveyardCards, ArrayList<Card> inGameCards, Leader player1LeaderCard, Leader player2LeaderCard, Faction player1Faction, Faction player2Faction, GameStatus status, RoundsInfo roundsInfo) {
        this.fromSaved = true;

        this.ID = ID;
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;

        this.isPlayer1Turn = isPlayer1Turn;
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

        calculatePoints();
    }

    // functions for game main logics:

    public void startGameThread() {
        status = GameStatus.ACTIVE;
        thread = new Thread(this::start);
        thread.start();
    }

    private void start() {
        // The fromSaved variable is true at first, so it doesn't run some codes until it reaches the first saving.
        // Then fromSaved becomes false and code runs like normally.
        if (!fromSaved) {
            for (int i = 0; i < STARTING_HAND_SIZE; i++) {
                player1GetRandomCard();
                player2GetRandomCard();
            }
            OpeningAbility.StartRound(this);
            if (isOnline) {
                if (userIsPlayer1()) {
                    player1VetoCard();
                } else {
                    player2VetoCard();
                }
                save();
            } else {
                player1VetoCard();
                player2VetoCard();
            }
        }
        while (!roundsInfo.isGameFinished()) {
            if (!fromSaved) {
                player1UsedLeaderAbility = false;
                player2UsedLeaderAbility = false;
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
            }
            while (!player1HasPassed || !player2HasPassed) {
                if (!fromSaved) {
                    EjectAbility.startTurnAffect(this);
                    save();
                }
                fromSaved = false;
                if (isOnline && !isMyTurn()) {
                    return;
                }
                handleTask("play");
                calculatePoints();
                switchSides();
            }
            roundsInfo.finishRound(player1Points, player2Points, this);
            resetCards();
        }
        status = GameStatus.COMPLETED;
        task = "show end screen";
        giveTask(); // it doesn't need to be handled
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
        if (cards1 != null) {
            if (!cards1.remove(card)) {
                throw new IllegalArgumentException("Card doesn't exist there");
            }
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
        handleTask("choose false");
        return Optional.of(chosenCard);
    }

    public Optional<Card> chooseCardOrPass(List<Card> cards) {
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        cardChoices = cards;
        handleTask("choose true");
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
        if (Objects.equals(user.getUsername(), player1.getUsername())) {
            isPlayer1Turn = true;
        } else if (Objects.equals(user.getUsername(), player2.getUsername())) {
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

    public Thread getThread() {
        return thread;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
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

    public User getCurrentUser() {
        return isPlayer1Turn ? player1 : player2;
    }

    public Boolean userIsPlayer1() {
        if (User.getCurrentUser().equals(player1)) {
            return true;
        } else if (User.getCurrentUser().equals(player2)) {
            return false;
        } else {
            return null;
        }
    }

    public boolean isMyTurn() {
        return isUserTurn(User.getCurrentUser());
    }

    public boolean isUserTurn(User user) {
        return (isPlayer1Turn && user.equals(player1)) ||
                (!isPlayer1Turn && user.equals(player2));
    }

    // functions for handling tasks:

    public void receiveTaskResult(String taskResult, User sender) { // done in javafx thread
        if (isOnline) {
            if ((isPlayer1Turn && !sender.getUsername().equals(player1.getUsername())) || (!isPlayer1Turn && !sender.getUsername().equals(player2.getUsername()))) {
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

    private void handleTask(String newTask) { // this and any other private functions run in game thread
        task = newTask;
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

    private void giveTask() {
        Platform.runLater(gamePaneController::doTask);
    }

    // Saving functions:

    public Game clone() {
        return new Game(ID, player1, player2, date, isPlayer1Turn, player1HasPassed, player2HasPassed,
                player1UsedLeaderAbility, player2UsedLeaderAbility, (ArrayList<Card>) player1Deck.clone(),
                (ArrayList<Card>) player2Deck.clone(), (ArrayList<Card>) player1InHandCards.clone(),
                (ArrayList<Card>) player2InHandCards.clone(), (ArrayList<Card>) player1GraveyardCards.clone(),
                (ArrayList<Card>) player2GraveyardCards.clone(), (ArrayList<Card>) inGameCards.clone(),
                player1LeaderCard, player2LeaderCard, player1Faction, player2Faction, status, roundsInfo.clone());
    }

    public void save() {
        Game gameClone = clone();
        Thread savingThread = new Thread(() -> {
            try {
                DatabaseConnection.updateGame(gameClone);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (isOnline) {
                App.getServerConnection().sendMessage(
                        (User.getCurrentUser().equals(player1) ? player2.getUsername() : player1.getUsername())
                                + ":other player played move");
            }
        });
        savingThread.start();
    }

    public void setTask(String task) {
        this.task = task;
    }


    public void setFromSaved(boolean fromSaved) {
        this.fromSaved = fromSaved;
    }

    // cheats:

    public void cheatGetRandomCard() {
        if (isPlayer1Turn) {
            player1GetRandomCard();
        } else {
            player2GetRandomCard();
        }
    }

    public void cheatResetHearts() {
        if (isPlayer1Turn) {
            roundsInfo.setPlayer1Hearts(2);
        } else {
            roundsInfo.setPlayer2Hearts(2);
        }
    }

    public void cheatClearWeather() {
        new PlayCard(null, NeutralCards.CLEAR_WEATHER, null).affect(this, null);
        calculatePoints();
    }

    public void cheatPlayScorch() {
        new PlayCard(null, NeutralCards.SCORCH, Type.WEATHER).affect(this, null);
        calculatePoints();
    }

    public void cheatResetGraveyardToDeck() {
        new ResetGraveyardToDeck().affect(this, null);
        calculatePoints();
    }

    public void cheatDoubleCards() {
        inGameCards.addAll(inGameCards.stream().filter(card -> card.getRow().isPlayer1() == isPlayer1Turn)
                .map(card -> {
                    Card clone = card.getCardEnum().getCard();
                    clone.setRow(card.getRow());
                    clone.setSmallImage();
                    return clone;
                }).toList());
        calculatePoints();
    }

    public void cheatUseLeaderAbility() {
        if (isPlayer1Turn) {
            ((InstantaneousAbility) player1LeaderCard.getAbility()).affect(this, null);
        } else {
            ((InstantaneousAbility) player2LeaderCard.getAbility()).affect(this, null);
        }
        calculatePoints();
    }

    public void cheatRemoveMyCards() {
        inGameCards.stream().filter(card -> card.getRow().isPlayer1() == isPlayer1Turn).toList()
                .forEach(inGameCards::remove);
        calculatePoints();
    }

    public void cheatRemoveEnemyCards() {
        inGameCards.stream().filter(card -> card.getRow().isPlayer1() ^ isPlayer1Turn).toList()
                .forEach(inGameCards::remove);
        calculatePoints();
    }

    // GameStatus enum

    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }
}