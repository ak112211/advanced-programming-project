package model.abilities.instantaneousabilities;

import model.Game;
import model.card.Card;

public class Decoy extends InstantaneousAbility {
    public void affect(Game game, Card card){
        game.moveCard(card, game.getInGameCards(),
                card.getRow().isPlayer1() ? game.getPlayer1Deck() : game.getPlayer2Deck());
    }
}