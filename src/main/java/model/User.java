package model;

import model.card.Card;
import util.DatabaseConnection;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private boolean verified;
    private boolean twoFactorOn;
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String securityQuestion;
    private String answer;

    private List<Integer> games = new ArrayList<>();
    private Deck deck;
    private ArrayList<Deck> decks = new ArrayList<>();
    private List<String> friends = new ArrayList<>();
    private List<String> pendingRequests = new ArrayList<>();

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

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<Integer> getGames() {
        return games;
    }

    public void setGames(List<Integer> games) {
        this.games = games;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void addFriend(String friendUsername) {
        if (!friends.contains(friendUsername)) {
            friends.add(friendUsername);
        }
    }

    public List<String> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(List<String> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public void addPendingRequest(String requestUsername) {
        if (!pendingRequests.contains(requestUsername)) {
            pendingRequests.add(requestUsername);
        }
    }

    public void removePendingRequest(String requestUsername) {
        pendingRequests.remove(requestUsername);
    }

    public Card getPlayCard() {
        return playCard;
    }

    public void setPlayCard(Card playCard) {
        this.playCard = playCard;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getRank() throws SQLException {
        return DatabaseConnection.getUserRank(this.username);
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isTwoFactorOn() {
        return twoFactorOn;
    }

    public void setTwoFactorOn(boolean twoFactorOn) {
        this.twoFactorOn = twoFactorOn;
    }
}
