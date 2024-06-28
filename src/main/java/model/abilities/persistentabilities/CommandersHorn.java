package model.abilities.persistentabilities;

import enums.cardsinformation.Type;
import model.abilities.instantaneousabilities.Spy;
import model.card.Card;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class CommandersHorn extends PersistentAbility {
    public static final ArrayList<Card> AFFECTED_CARDS = new ArrayList<>();

    public CommandersHorn(BiFunction<Card, Card, Boolean> doesAffect) {
        super(doesAffect);
    }

    public CommandersHorn() {
        super(CommandersHorn::doesAffectDefault);
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return notSameCards(myCard, card) && sameRow(myCard, card) && !AFFECTED_CARDS.contains(card);
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