package model.card;

import enums.cardsinformation.*;

public class Card {
    private final String NAME;
    private final Type TYPE;
    private int power;
    private final Ability ABILITY;  // Assuming you have an Ability enum similar to CardType and CardPower
    private final Faction FACTION;  // Adding Faction as a property of Card
    private final Description DESCRIPTION;

    public Card(String name, Type type, int power, Ability ability, Faction faction, Description description) {
        this.NAME = name;
        this.TYPE = type;
        this.power = power;
        this.ABILITY = ability;
        this.DESCRIPTION = description;
        this.FACTION = faction;  // Initialize faction
    }

    // Getters
    public String getName() {
        return NAME;
    }

    public Type getType() {
        return TYPE;
    }

    public int getPower() {
        return power;
    }

    public Ability getAbility() {
        return ABILITY;
    }

    public Faction getFaction() {
        return FACTION;
    }

    public Description getDescription() {
        return DESCRIPTION;
    }

    public void setPower(int power) {
        this.power = power;
    }

}
