package model.abilities.instantaneousabilities;

import model.Game;
import model.abilities.Ability;
import model.card.Card;

public abstract class InstantaneousAbility extends Ability {
    public abstract void affect(Game game);
}