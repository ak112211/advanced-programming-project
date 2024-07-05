package model.card;

import enums.cardsinformation.Faction;
import enums.leaders.*;
import javafx.scene.shape.Rectangle;
import model.abilities.Ability;
import view.Tools;

public class Leader extends Rectangle {
    private final String name;
    private final Faction faction;
    private final String description;
    private final Ability ability;
    private int numberOfAction = 1;
    private final LeaderEnum leaderEnum;
    private final String imagePath;

    public Leader(String name, Ability ability, Faction faction, String description, String imagePath, LeaderEnum leaderEnum) {
        this.name = name;
        this.imagePath = imagePath;
        this.faction = faction;
        this.description = description;
        this.ability = ability;
        this.leaderEnum = leaderEnum;

        setBigImage();
    }

    public void setSmallImage() {
        this.setArcWidth(10);
        this.setArcHeight(10);
        this.setWidth(68);
        this.setHeight(90);
        this.setFill(Tools.getImagePattern(imagePath.replaceFirst("lg", "sm")));
    }

    public void setBigImage() {
        this.setArcWidth(10);
        this.setArcHeight(10);
        this.setWidth(70);
        this.setHeight(100);
        this.setFill(Tools.getImagePattern(imagePath));
    }

    public String getName() {
        return name;
    }

    public Faction getFaction() {
        return faction;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfAction() {
        return numberOfAction;
    }

    public void setNumberOfAction(int numberOfAction) {
        this.numberOfAction = numberOfAction;
    }

    public Ability getAbility() {
        return ability;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LeaderEnum getLeaderEnum() {
        return leaderEnum;
    }

    public static Leader getLeaderFromType(String type) {
        for (RealmsNorthernLeaders cardEnum : RealmsNorthernLeaders.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getLeader();
            }
        }
        for (ScoiaTaelLeaders cardEnum : ScoiaTaelLeaders.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getLeader();
            }
        }
        for (MonstersLeaders cardEnum : MonstersLeaders.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getLeader();
            }
        }
        for (EmpireNilfgaardianLeaders cardEnum : EmpireNilfgaardianLeaders.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getLeader();
            }
        }
        for (ScoiaTaelLeaders cardEnum : ScoiaTaelLeaders.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getLeader();
            }
        }
        return null;
    }

}
