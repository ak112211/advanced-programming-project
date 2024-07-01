package model.abilities.persistentabilities;

import model.abilities.Animatable;
import model.abilities.instantaneousabilities.Spy;
import model.card.Card;

import java.util.ArrayList;

public class CommandersHorn extends PersistentAbility implements Animatable {
    public static final ArrayList<Card> affectedCards = new ArrayList<>();

    public CommandersHorn(boolean forSpies) {
        super(forSpies ? CommandersHorn::doesAffectSpy : CommandersHorn::doesAffectDefault, "horn");
    }

    public CommandersHorn() {
        this(false);
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return myCard != card && myCard.sameRow(card) && !affectedCards.contains(card);
    }

    public static boolean doesAffectSpy(Card myCard, Card card) {
        return card.getAbility() instanceof Spy && !affectedCards.contains(card);
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return affectedCards;
    }

    public static void affect(Card card) {
        card.setPower(card.getPower() * 2);
    }

}