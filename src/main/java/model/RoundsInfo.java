package model;

import enums.cardsinformation.Faction;

import java.util.ArrayList;

public class RoundsInfo {
    private static final int TOTAL_ROUND_NUMBER = 3;
    private static final int ROUND_TO_WIN = 2;
    private final ArrayList<Integer> player1Scores = new ArrayList<Integer>();
    private final ArrayList<Integer> player2Scores = new ArrayList<Integer>();
    private int currentRound = 1;
    private Winner winner = null;

    public void finishRound(int player1Score, int player2Score) {
        player1Scores.add(player1Score);
        player2Scores.add(player2Score);
        currentRound++;
    }

    public boolean isGameFinished(Game game) {
        if (player1LostRoundNumber(game) == ROUND_TO_WIN) {
            winner = Winner.PLAYER1;
            return true;
        }
        if (player2LostRoundNumber(game) == ROUND_TO_WIN) {
            winner = Winner.PLAYER2;
            return true;
        }
        if (currentRound == TOTAL_ROUND_NUMBER) {
            winner = Winner.DRAW;
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

    public enum Winner {
        DRAW, PLAYER1, PLAYER2
    }
}
