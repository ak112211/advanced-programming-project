package model.abilities;

import enums.cardsinformation.Type;
import model.card.Card;

public abstract class Ability {
    public static boolean canBeAffected(Card card) {
        return card.getType() != Type.SPELL && card.getType() != Type.DECOY && !card.isHero();
    }
}