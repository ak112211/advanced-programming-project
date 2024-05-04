package model.ability.PersistentAbility;

import enums.Row;
import model.Game;
import model.ability.Ability;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public abstract class PersistentAbility extends Ability {
    public BiFunction<Card, Card, Boolean> doesAffect;
    public PersistentAbility(BiFunction<Card, Card, Boolean> doesAffect){
        this.doesAffect = doesAffect;
    }
    public static boolean doesAffectDefault(Card abilityCard, Card card) {
        return canBeAffected(card) && notSameCards(abilityCard, card) && sameRow(abilityCard, card);
    }
    public abstract void affect(Card card);
    public static void calculateChangedPower(){

    }
    public abstract ArrayList<Card> getAffectedCards();
    public abstract void addToAffectedCards(Card card);

}