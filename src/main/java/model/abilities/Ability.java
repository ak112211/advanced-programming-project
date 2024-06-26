package model.abilities;

import enums.cardsinformation.Type;
import model.card.Card;

public abstract class Ability {
    public static boolean canBeAffected(Card card) {
        return card.getType() != Type.SPELL && card.getType() != Type.DECOY && !card.isHero();
    }

    public static boolean sameRow(Card myCard, Card card) {
        return myCard.getRow() == card.getRow();
    }

    public static boolean notSameCards(Card myCard, Card card) {
        return myCard == card;
    }

    public static boolean sameName(Card myCard, Card card) {
        return myCard.getName().equals(card.getName());
    }

}