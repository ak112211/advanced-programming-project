package model.abilities.instantaneousabilities;

import enums.cardsinformation.CardsPlace;
import model.Game;
import model.card.Card;

public class Decoy extends InstantaneousAbility {
    private Card returnCard;
    public Decoy() {
        setIconName("decoy");
    }

    public void setReturnCard(Card card) {
        if (returnCard != null) {
            throw new IllegalStateException("returnCard is not null. ye jaii ridi");
        }
        returnCard = card;
    }

    public void affect(Game game, Card myCard) {
        game.moveCard(returnCard, game.getInGameCards(), CardsPlace.IN_HAND.getPlayerCards(game));
        returnCard = null;
    }
}