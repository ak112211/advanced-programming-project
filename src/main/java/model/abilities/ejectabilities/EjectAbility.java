package model.abilities.ejectabilities;

import model.Game;
import model.abilities.Ability;
import model.card.Card;

import java.util.ArrayList;

public abstract class EjectAbility extends Ability {
    public static void startTurnAffect(Game game){
        SummonAvenger.startTurnAffect(game);
    }

    public abstract void affect(Card myCard);
}