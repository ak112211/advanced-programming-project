package model;

import java.util.Date;

public class Game {

    private final User PLAYER1;
    private final User PLAYER2;
    private final Date DATE;

    public Game(User player1, User player2, Date date) {
        this.PLAYER1 = player1;
        this.PLAYER2 = player2;
        this.DATE = date;
    }

    public User getPlayer1() {
        return PLAYER1;
    }

    public User getPlayer2() {
        return PLAYER2;
    }

    public Date getDATE() {
        return DATE;
    }
}
