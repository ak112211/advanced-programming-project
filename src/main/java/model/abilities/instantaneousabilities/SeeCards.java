package model.abilities.instantaneousabilities;

import model.Game;
import model.card.Card;

public class SeeCards extends InstantaneousAbility{
    public final int amount;
    public SeeCards(int amount) {
        this.amount = amount;
    }

    public void affect(Game game, Card myCard) {

    }
}
