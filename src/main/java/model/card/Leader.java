package model.card;

import enums.cardsinformation.Faction;

public class Leader {
    private final String NAME;
    private final Faction FACTION;

    public Leader(String name, Faction faction) {
        this.NAME = name;
        this.FACTION = faction;
    }
}
