package model.card;

import enums.cardsinformation.Faction;

public class Leader {
    private final String NAME;
    private final Faction FACTION;
    private int numberOfAction = 1;

    public Leader(String name, Faction faction) {
        this.NAME = name;
        this.FACTION = faction;
    }

    public String getName() {
        return NAME;
    }

    public Faction getFaction() {
        return FACTION;
    }

    public int getNumberOfAction() {
        return numberOfAction;
    }

    public void setNumberOfAction(int numberOfAction) {
        this.numberOfAction = numberOfAction;
    }
}
