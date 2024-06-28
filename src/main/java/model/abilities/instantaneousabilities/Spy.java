package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Spy extends InstantaneousAbility {
    public void affect(Game game, Card myCard) {
        ArrayList<Card> deck = CardsPlace.DECK.getPlayerCards(game);
        ArrayList<Card> inHandCards = CardsPlace.IN_HAND.getPlayerCards(game);
        for (int i = 0; i < 2; i++) {
            Optional<Card> card = Game.chooseRandomCard(deck, false);
            if (card.isEmpty()) {
                return;
            }
            game.moveCard(card.get(), deck, inHandCards);
        }
    }
}