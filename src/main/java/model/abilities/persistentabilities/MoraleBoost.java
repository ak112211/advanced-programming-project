package model.abilities.persistentabilities;

import model.card.Card;

import java.util.ArrayList;

public class MoraleBoost extends PersistentAbility {
    public static final ArrayList<Card> AFFECTED_CARDS = new ArrayList<>();

    public MoraleBoost() {
        super(MoraleBoost::doesAffectDefault, "morale");
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return myCard != card && myCard.sameRow(card);
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return AFFECTED_CARDS;
    }

    public static void affect(Card card) {
        card.setPower(card.getPower() + 1);
    }


}