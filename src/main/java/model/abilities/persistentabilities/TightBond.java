package model.abilities.persistentabilities;

import model.abilities.Animatable;
import model.card.Card;

import java.util.ArrayList;

public class TightBond extends PersistentAbility implements Animatable {
    public static final ArrayList<Card> affectedCards = new ArrayList<>();

    public TightBond() {
        super(TightBond::doesAffectDefault, "bond");
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return myCard != card && myCard.same(card) && myCard.sameRow(card);
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return affectedCards;
    }

    public static void affect(Card card) {
        card.setPower(card.getPower() + card.getFirstPower());
    }

}