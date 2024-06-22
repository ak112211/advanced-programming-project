package controller;

import model.Command;

public class GameController extends AppController {

    @Override
    public String menuEnter(String menuName) {
        return null;
    }

    @Command(command = "veto card <cardNumber>")
    public String vetoCard(int cardNumber) {
        return null;
    }

    @Command(command = "in hand deck")
    public String showInHandDeck() {
        return null;
    }

    @Command(command = "in hand deck card -option <cardNumber>")
    public String showInHandDeckCard(int cardNumber) {
        return null;
    }

    @Command(command = "remaining cards to play")
    public String showRemainingCards() {
        return null;
    }

    @Command(command = "out of play cards")
    public String showOutOfPlayCards() {
        return null;
    }

    @Command(command = "cards in -row <rowNumber>")
    public String showRow() {
        return null;
    }

    @Command(command = "spells in play")
    public String showSpellsInPlay() {
        return null;
    }

    @Command(command = "place -card <cardNumber> in -row <rowNumber>")
    public String placeCardInRow(int cardNumber, int rowNumber) {
        return null;
    }

    @Command(command = "show leader")
    public String showLeader() {
        return null;
    }

    @Command(command = "leader power play")
    public String leaderAction() {
        return null;
    }

    @Command(command = "show players info")
    public String showPlayersInfo() {
        return null;
    }

    @Command(command = "show players lives")
    public String showPlayersLives() {
        return null;
    }

    @Command(command = "show number of cards in hand")
    public String showNumberOfCardsInHand() {
        return null;
    }

    @Command(command = "show turn info")
    public String showTurnInfo() {
        return null;
    }

    @Command(command = "show total score")
    public String showTotalScore() {
        return null;
    }

    @Command(command = "show total score of row <rowNumber>")
    public String showTotalScoreOfRow(int rowNumber) {
        return null;
    }

    @Command(command = "pass round")
    public String pass() {
        return null;
    }

}
