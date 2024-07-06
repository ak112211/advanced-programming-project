package model;

import java.util.ArrayList;

public class League {

    private String name;
    private ArrayList<String> players;
    private LeagueHierarchy leagueHierarchy;
    private String winner;
    private String quarter1Game;
    private String quarter2Game;
    private String quarter3Game;
    private String quarter4Game;
    private String semi1Game;
    private String semi2Game;

    private Game finalPlay;

    public League(String name, String firstPlayer) {
        this.name = name;
        addPlayer(firstPlayer);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public LeagueHierarchy getLeagueHierarchy() {
        return leagueHierarchy;
    }

    public void setLeagueHierarchy(LeagueHierarchy leagueHierarchy) {
        this.leagueHierarchy = leagueHierarchy;
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

    public Game getFinalPlay() {
        return finalPlay;
    }

    public void setFinalPlay(Game finalPlay) {
        this.finalPlay = finalPlay;
    }

    public void addPlayer(String user) {
        this.players.add(user);
    }

    public void removePlayer(String user) {
        this.players.remove(user);
    }
}
