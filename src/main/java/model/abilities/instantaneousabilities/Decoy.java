package model.abilities.instantaneousabilities;

import model.Game;
import model.card.Card;

public class Decoy extends InstantaneousAbility {
    Card returnCard;

    public void setReturnCard(Card card) {
        returnCard = card;
    }

    public void affect(Game game, Card myCard) {
        game.moveCard(returnCard, game.getInGameCards(),
                game.isPlayer1Turn() ? game.getPlayer1Deck() : game.getPlayer2Deck());
    }
}