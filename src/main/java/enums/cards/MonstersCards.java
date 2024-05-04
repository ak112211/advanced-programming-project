package enums.cards;

import enums.cardsinformation.Ability;
import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import enums.cardsinformation.Type;
import model.card.Card;

public enum MonstersCards {
    DRAUG("Draug", 10, 1, Type.CLOSE_COMBAT_UNIT, null, true, Description.NOTHING);
    private final String NAME;
    private final Type TYPE;
    private final int NO_OF_CARDS_IN_GAME;
    private final int POWER;
    private final Ability ABILITY;
    private final boolean IS_HERO;
    private final Description DESCRIPTION;

    MonstersCards(String name, int power, int noOfCardsInGame, Type type,
                  Ability ability, boolean isHero, Description description) {
        this.NAME = name;
        this.POWER = power;
        this.NO_OF_CARDS_IN_GAME = noOfCardsInGame;
        this.TYPE = type;
        this.ABILITY = ability;
        this.IS_HERO = isHero;
        this.DESCRIPTION = description;
    }

    public Card getCard() {
        return new Card(NAME, TYPE, NO_OF_CARDS_IN_GAME, POWER, ABILITY, IS_HERO, Faction.MONSTER, DESCRIPTION);
    }
}
