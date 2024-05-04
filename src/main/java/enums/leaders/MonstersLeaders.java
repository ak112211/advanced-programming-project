package enums.leaders;

import enums.cardsinformation.Faction;
import model.card.Leader;

public enum MonstersLeaders {
    BRINGER_OF_DEATH("Bringer of Death");

    private final String NAME;

    MonstersLeaders(String name) {
        this.NAME = name;
    }

    public Leader getLeader() {
        return new Leader(NAME, Faction.MONSTER);
    }
}
