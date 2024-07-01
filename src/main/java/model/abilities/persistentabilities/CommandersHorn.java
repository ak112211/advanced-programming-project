package model.abilities.persistentabilities;

import enums.cardsinformation.Type;
import model.abilities.instantaneousabilities.Spy;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class CommandersHorn extends PersistentAbility {
    public static final ArrayList<Card> AFFECTED_CARDS = new ArrayList<>();

    public CommandersHorn(boolean forSpies) {
        super(forSpies ? CommandersHorn::doesAffectSpy : CommandersHorn::doesAffectDefault, "horn");
    }

    public CommandersHorn() {
        this(false);
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return myCard != card && myCard.sameRow(card) && !AFFECTED_CARDS.contains(card);
    }

    public static boolean doesAffectSpy(Card myCard, Card card) {
        return card.getAbility() instanceof Spy && !AFFECTED_CARDS.contains(card);
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return AFFECTED_CARDS;
    }

    public static void affect(Card card) {
        card.setPower(card.getPower() * 2);
    }

}