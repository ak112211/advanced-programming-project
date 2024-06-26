package model.abilities.instantaneousabilities;

import model.Game;
import model.card.Card;

public class KillAndGetCard extends InstantaneousAbility{
    private final int killAmount;
    public KillAndGetCard(int killAmount) {
        this.killAmount = killAmount;
    }

    public void affect(Game game, Card myCard) {

    }
}
