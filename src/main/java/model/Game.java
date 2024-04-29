package model;

public class Game {

    private final User PLAYER1;
    private final User PLAYER2;

    public Game(User player1, User player2) {
        this.PLAYER1 = player1;
        this.PLAYER2 = player2;
    }

    public User getPlayer1() {
        return PLAYER1;
    }

    public User getPlayer2() {
        return PLAYER2;
    }
}
