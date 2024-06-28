package model;

import model.card.Card;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {

    private String username;
    private String nickname;
    private String email;
    private String password;
    private int questionNumber;
    private String answer;

    private ArrayList<Game> games = new ArrayList<>();
    private Deck deck;
    private ArrayList<Deck> decks = new ArrayList<>();
    private List<String> friends;

    private Card playCard;
    private int highScore = 0;

    private static User currentUser;

    public User(String username, String nickname, String email, String password) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static User getCurrentUser() {
        return User.currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
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

    public ArrayList<Game> getGames() {
        return games;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public void setPlayCard(Card playCard) {
        this.playCard = playCard;
    }

    public Card getPlayCard() {
        return playCard;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    private static void sortUsers() {

    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void addFriend(String friendUsername) {
        if (!friends.contains(friendUsername)) {
            friends.add(friendUsername);
        }
    }

    public List<String> getFriends() {
        return friends;
    }

    public int getRank() {
        // TODO:
        return 1;
    }

}


