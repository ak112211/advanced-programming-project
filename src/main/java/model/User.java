package model;

import enums.cardsinformation.Faction;
import model.card.Card;
import model.card.Leader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {

    private static final ArrayList<User> USERS = new ArrayList<User>();

    private String username;
    private String nickname;
    private String email;
    private String password;
    private int questionNumber;
    private String answer;

    private ArrayList<Game> games = new ArrayList<Game>();
    private Deck deck;
    private ArrayList<Deck> decks = new ArrayList<Deck>();
    private List<String> friends;

    private Card playCard;
    private int highScore = 0;

    private static User currentUser;
    public User(String username, String nickname, String email, String password) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        USERS.add(this);
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

    public User getUserByUsername(String username) {
        for (User user : USERS) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
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

    public int getRank() {
        sortUsers();
        return USERS.indexOf(this) + 1;
    }

    public void addFriend(String friendUsername) {
        if (!friends.contains(friendUsername)) {
            friends.add(friendUsername);
        }
    }

    public List<String> getFriends() {
        return friends;
    }

    public static User deserializeUser(String serializedUser) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedUser.getBytes());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to load user data.");
            return null;
        }
    }
}


