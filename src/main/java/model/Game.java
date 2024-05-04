package model;

import java.util.Date;

public class Game {

    private final User PLAYER1;
    private final User PLAYER2;
    private final Date DATE;
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

    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }
}

