package model.abilities.instantaneousabilities;

import model.Game;
import model.abilities.Ability;
import model.card.Card;

public abstract class InstantaneousAbility extends Ability {
    public static void affect(InstantaneousAbility ability, Game game, Card card){
        if (ability instanceof ClearWeather){
            ((ClearWeather)ability).affect(game);
        } else if (ability instanceof Decoy){
            ((Decoy)ability).affect(game, card);
        } else if (ability instanceof Medic){
            ((Medic)ability).affect(game, card, null);
        } else if (ability instanceof Muster){
            ((Muster)ability).affect(game);
        } else if (ability instanceof Scorch){
            ((Scorch)ability).affect(game);
        } else if (ability instanceof Spy){
            ((Spy)ability).affect(game);
        } else {
            throw new RuntimeException("mage mishe hich kodoom az ina nabashe?!");
        }
    }
}