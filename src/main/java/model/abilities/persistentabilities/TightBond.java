package model.abilities.persistentabilities;

import model.card.Card;

import java.util.ArrayList;

public class TightBond extends PersistentAbility {
    public static ArrayList<Card> AffectedCards = new ArrayList<>();

    public TightBond() {
        super(TightBond::doesAffectDefault);
    }

    public static boolean doesAffectDefault(Card myCard, Card card) {
        return notSameCards(myCard, card) && sameName(myCard, card) && sameRow(myCard, card);
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return AffectedCards;
    }

    public static void affect(Card card) {
        card.setPower(card.getPower() + card.getFirstPower());
    }

}