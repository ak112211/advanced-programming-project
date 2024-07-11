package model.abilities;

import model.card.Card;

public abstract class Ability {
    private String iconName;

    public static boolean canBeAffected(Card card) {
        return !card.getType().isSpecial() && !card.isHero();
    }

    public void setIconName(String name) {
        iconName = name;
    }

    public String getIconName() {
        return iconName;
    }
}