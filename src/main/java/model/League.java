package model;

import java.util.ArrayList;
import java.util.List;

public class League {

    private int ID;
    private String name;
    private List<String> players = new ArrayList<>();
    private String winner;
    private String quarter1Game;
    private String quarter2Game;
    private String quarter3Game;
    private String quarter4Game;
    private String semi1Game;
    private String semi2Game;
    private String finalPlay;

    public League(String name) {
        this.name = name;
    }

    // Getters and Setters

    public void addPlayer(String user) {
        if (this.players.size() < 8) {
            this.players.add(user);
        } else {
            throw new IllegalStateException("League is already full with 8 players.");
        }
    }

    public void removePlayer(String user) {
        this.players.remove(user);
    }

    public void startQuarterFinals() {
        if (players.size() != 8) {
            throw new IllegalStateException("Need exactly 8 players to start the league.");
        }
        quarter1Game = players.get(0) + " vs " + players.get(1);
        quarter2Game = players.get(2) + " vs " + players.get(3);
        quarter3Game = players.get(4) + " vs " + players.get(5);
        quarter4Game = players.get(6) + " vs " + players.get(7);
    }

    public void startSemiFinals(String winner1, String winner2, String winner3, String winner4) {
        semi1Game = winner1 + " vs " + winner2;
        semi2Game = winner3 + " vs " + winner4;
    }

    public void startFinals(String semiWinner1, String semiWinner2) {
        finalPlay = semiWinner1 + " vs " + semiWinner2;
    }

    public void declareWinner(String finalWinner) {
        winner = finalWinner;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getQuarter1Game() {
        return quarter1Game;
    }

    public void setQuarter1Game(String quarter1Game) {
        this.quarter1Game = quarter1Game;
    }

    public String getQuarter2Game() {
        return quarter2Game;
    }

    public void setQuarter2Game(String quarter2Game) {
        this.quarter2Game = quarter2Game;
    }

    public String getQuarter3Game() {
        return quarter3Game;
    }

    public void setQuarter3Game(String quarter3Game) {
        this.quarter3Game = quarter3Game;
    }

    public String getQuarter4Game() {
        return quarter4Game;
    }

    public void setQuarter4Game(String quarter4Game) {
        this.quarter4Game = quarter4Game;
    }

    public String getSemi1Game() {
        return semi1Game;
    }

    public void setSemi1Game(String semi1Game) {
        this.semi1Game = semi1Game;
    }

    public String getSemi2Game() {
        return semi2Game;
    }

    public void setSemi2Game(String semi2Game) {
        this.semi2Game = semi2Game;
    }

    public String getFinalPlay() {
        return finalPlay;
    }

    public void setFinalPlay(String finalPlay) {
        this.finalPlay = finalPlay;
    }
}
