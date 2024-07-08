package model;

import enums.cardsinformation.Faction;

import java.util.ArrayList;

public class RoundsInfo implements Cloneable{
    private static final int TOTAL_ROUND_NUMBER = 3;
    private static final int ROUND_TO_WIN = 2;

    private final ArrayList<Integer> player1Scores = new ArrayList<>();
    private final ArrayList<Integer> player2Scores = new ArrayList<>();
    private int player1Hearts = 2;
    private int player2Hearts = 2;
    private int currentRound = 1;
    private Winner winner = null;

    public void finishRound(int player1Score, int player2Score,Game game) {
        player1Scores.add(player1Score);
        player2Scores.add(player2Score);
        Winner winner = getRoundWinner(currentRound, game);
        switch (winner) {
            case PLAYER1:
                player1Hearts--;
                break;
            case PLAYER2:
                player2Hearts--;
                break;
            case DRAW:
                player1Hearts--;
                player2Hearts--;
                break;
        }
        currentRound++;
    }

    public boolean isGameFinished() {
        if (player1Hearts == 0 && player2Hearts == 0) {
            winner = Winner.DRAW;
            return true;
        }
        if (player1Hearts == 0) {
            winner = Winner.PLAYER2;
            return true;
        }
        if (player2Hearts == 0) {
            winner = Winner.PLAYER1;
            return true;
        }
        return false;
    }

    public int getPlayer1Score(int round) {
        return player1Scores.get(round - 1);
    }

    public int getPlayer2Score(int round) {
        return player2Scores.get(round - 1);
    }

    public Winner getRoundWinner(int round, Game game) {
        if (getPlayer1Score(round) > getPlayer2Score(round)) {
            return Winner.PLAYER1;
        } else if (getPlayer2Score(round) > getPlayer1Score(round)) {
            return Winner.PLAYER2;
        } else {
            if (game.getPlayer1Faction() == Faction.EMPIRE_NILFGAARDIAM) {
                if (game.getPlayer2Faction() == Faction.EMPIRE_NILFGAARDIAM) {
                    return Winner.DRAW;
                } else {
                    return Winner.PLAYER1;
                }
            } else if (game.getPlayer2Faction() == Faction.EMPIRE_NILFGAARDIAM) {
                return Winner.PLAYER2;
            } else {
                return Winner.DRAW;
            }
        }
    }

    public int player1LostRoundNumber(Game game) {
        int lostRounds = 0;
        for (int round = 1; round < currentRound; round++) {
            if (getRoundWinner(round, game) != Winner.PLAYER1) {
                lostRounds++;
            }
        }
        return lostRounds;
    }

    public int player2LostRoundNumber(Game game) {
        int lostRounds = 0;
        for (int round = 1; round < currentRound; round++) {
            if (getRoundWinner(round, game) != Winner.PLAYER2) {
                lostRounds++;
            }
        }
        return lostRounds;
    }

    public Winner getWinner() {
        return winner;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public RoundsInfo clone() {
        RoundsInfo clone = new RoundsInfo();
        clone.player2Hearts = player2Hearts;
        clone.player1Hearts = player1Hearts;
        clone.currentRound = currentRound;
        clone.player1Scores.addAll(player1Scores);
        clone.player2Scores.addAll(player2Scores);
        clone.winner = winner;
        return clone;
    }


    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public int getPlayer1Hearts() {
        return player1Hearts;
    }

    public void setPlayer1Hearts(int player1Hearts) {
        this.player1Hearts = player1Hearts;
    }

    public int getPlayer2Hearts() {
        return player2Hearts;
    }

    public void setPlayer2Hearts(int player2Hearts) {
        this.player2Hearts = player2Hearts;
    }

    public enum Winner {
        DRAW, PLAYER1, PLAYER2
    }
}
