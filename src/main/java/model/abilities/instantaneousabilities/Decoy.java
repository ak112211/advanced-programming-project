package model.abilities.instantaneousabilities;

import model.Game;
import model.card.Card;

public class Decoy extends InstantaneousAbility {
    public void affect(Game game, Card card){
        game.moveCardBackToHand(card);
    }
}