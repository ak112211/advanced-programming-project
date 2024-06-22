package model.abilities.persistentabilities;

import model.card.Card;

import java.util.ArrayList;

public class TightBond extends PersistentAbility {
    public static ArrayList<Card> AffectedCards = new ArrayList<>();

    public TightBond() {
        super(CommandersHorn::doesAffectDefault);
    }

    public static boolean doesAffectDefault(Card abilityCard, Card card) {
        return notSameCards(abilityCard, card) && sameName(abilityCard, card) && sameRow(abilityCard, card);
    }

    @Override
    public ArrayList<Card> getAffectedCards() {
        return AffectedCards;
    }

    public static void affect(Card card) {
        card.setPower(card.getPower() + card.getFirstPower());
    }

}