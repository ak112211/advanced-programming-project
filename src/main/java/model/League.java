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

    // Getters and Setters for new fields

    public void addPlayer(String username) {
        this.players.add(username);
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
