package model.abilities.instantaneousabilities;

import model.Game;
import model.card.Card;

import java.util.ArrayList;
import java.util.Random;

public class Spy extends InstantaneousAbility {
    public void affect(Game game) {
        ArrayList<Card> deck = !getCard().getRow().isPlayer1() ? game.getPlayer1Deck() : game.getPlayer2Deck();
        ArrayList<Card> inHandCards = !getCard().getRow().isPlayer1() ? game.getPlayer1InHandCards() : game.getPlayer2InHandCards();
        Random rand = new Random();
        for (int i = 0; i < 2; i++) {
            Card card = deck.get(rand.nextInt(deck.size()));
            game.moveCard(card, deck, inHandCards);
        }
    }
}