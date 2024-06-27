package model.abilities.persistentabilities;

import model.card.Card;

import java.util.ArrayList;

public class MoraleBoost extends PersistentAbility {
    public static final ArrayList<Card> AFFECTED_CARDS = new ArrayList<>();

    public MoraleBoost() {
        super(MoraleBoost::doesAffectDefault);
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return canBeAffected(card) && notSameCards(myCard, card) && sameRow(myCard, card);
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return AFFECTED_CARDS;
    }

    public static void affect(Card card) {
        card.setPower(card.getPower() + 1);
    }


}