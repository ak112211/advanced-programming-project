package model;

import java.util.Date;

public class Game {

    private final User PLAYER1;
    private final User PLAYER2;
    private final Date DATE;
    private GameStatus status;
    private User winner;  // Stores the winner of the game

    public Game(User player1, User player2, Date date) {
        this.PLAYER1 = player1;
        this.PLAYER2 = player2;
        this.DATE = date;
        this.status = GameStatus.PENDING;  // Default to pending when the game is created
        this.winner = null;
    }

    // Getter for Player1
    public User getPlayer1() {
        return PLAYER1;
    }

    // Getter for Player2
    public User getPlayer2() {
        return PLAYER2;
    }

    // Getter for the game date
    public Date getDATE() {
        return DATE;
    }

    // Getter for game status
    public GameStatus getStatus() {
        return status;
    }

    // Setter for game status
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    // Getter for winner
    public User getWinner() {
        return winner;
    }

    // Setter for winner
    public void setWinner(User winner) {
        this.winner = winner;
    }

    // Enum to represent the status of the game
    public enum GameStatus {
        PENDING, ACTIVE, COMPLETED
    }
}

