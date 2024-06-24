package model.abilities;

import enums.cardsinformation.Type;
import model.card.Card;

public abstract class Ability {

    private Card card;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public static boolean canBeAffected(Card card) {
        return card.getType() != Type.SPELL && card.getType() != Type.DECOY && !card.isHero();
    }

    public static boolean sameRow(Card abilityCard, Card card) {
        return abilityCard.getRow() == card.getRow();
    }

    public static boolean notSameCards(Card abilityCard, Card card) {
        return abilityCard == card;
    }

    public static boolean sameName(Card abilityCard, Card card) {
        return abilityCard.getName().equals(card.getName());
    }

}