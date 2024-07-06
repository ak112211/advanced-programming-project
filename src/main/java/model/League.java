package model;

import java.util.ArrayList;

public class League {

    private String name;
    private ArrayList<String> players;
    private LeagueHierarchy leagueHierarchy;
    private String winner;
    private Game quarter1;
    private Game quarter2;
    private Game quarter3;
    private Game quarter4;
    private Game semi1;
    private Game semi2;

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

    public Game getQuarter1() {
        return quarter1;
    }

    public void setQuarter1(Game quarter1) {
        this.quarter1 = quarter1;
    }

    public Game getQuarter2() {
        return quarter2;
    }

    public void setQuarter2(Game quarter2) {
        this.quarter2 = quarter2;
    }

    public Game getQuarter3() {
        return quarter3;
    }

    public void setQuarter3(Game quarter3) {
        this.quarter3 = quarter3;
    }

    public Game getQuarter4() {
        return quarter4;
    }

    public void setQuarter4(Game quarter4) {
        this.quarter4 = quarter4;
    }

    public Game getSemi1() {
        return semi1;
    }

    public void setSemi1(Game semi1) {
        this.semi1 = semi1;
    }

    public Game getSemi2() {
        return semi2;
    }

    public void setSemi2(Game semi2) {
        this.semi2 = semi2;
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
