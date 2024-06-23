package model.card;

import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;

public class Leader {
    private final String NAME;
    private final Faction FACTION;
    private final String DESCRIPTION;
    private int numberOfAction = 1;

    private String imagePath;
    public Leader(String name, Faction faction, String description) {
        NAME = name;
        FACTION = faction;
        DESCRIPTION = description;
    }

    public String getName() {
        return NAME;
    }

    public Faction getFaction() {
        return FACTION;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public int getNumberOfAction() {
        return numberOfAction;
    }

    public void setNumberOfAction(int numberOfAction) {
        this.numberOfAction = numberOfAction;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
