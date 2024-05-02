package model.card;

import enums.card.*;

public class Card {
    private String name;
    private Type type;
    private Power power;
    private Ability ability;  // Assuming you have an Ability enum similar to CardType and CardPower
    private Faction faction;  // Adding Faction as a property of Card
    private Description description;
    // Constructor updated to include faction
    public Card(String name, Type type, Power power, Ability ability, Faction faction, Description description) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.ability = ability;
        this.description = description;
        this.faction = faction;  // Initialize faction
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Power getPower() {
        return power;
    }

    public Ability getAbility() {
        return ability;
    }

    public Faction getFaction() {
        return faction;
    }

    public Description getDescription() {
        return description;
    }
}
