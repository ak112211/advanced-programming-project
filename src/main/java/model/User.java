package model;

import enums.card.Faction;
import model.card.Leader;

import java.util.ArrayList;

public class User {

    private static ArrayList<User> users = new ArrayList<User>();

    private String username;
    private String nickname;
    private String email;
    private String password;
    private int questionNumber;
    private String answer;

    private ArrayList<Game> games = new ArrayList<Game>();
    private Faction faction;
    private Leader leader;
    private Deck deck;
    private ArrayList<Deck> decks = new ArrayList<Deck>();

    public User(String username, String nickname, String email, String password) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        users.add(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }
}


