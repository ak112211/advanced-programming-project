package model.card;

import enums.cards.*;
import enums.leaders.*;
import enums.cardsinformation.Faction;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.abilities.Ability;

import java.util.Objects;

public class Leader extends Rectangle {
    private final String NAME;
    private final Faction FACTION;
    private final String DESCRIPTION;
    private final Ability ABILITY;
    private int numberOfAction = 1;
    private final LeaderEnum LEADER_ENUM;
    private String imagePath;
    public Leader(String name, Ability ability, Faction faction, String description, String imagePath, LeaderEnum leaderEnum) {
        NAME = name;
        imagePath = imagePath;
        FACTION = faction;
        DESCRIPTION = description;
        ABILITY = ability;
        LEADER_ENUM = leaderEnum;

        this.setWidth(70);
        this.setHeight(100);
        this.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)))));
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

    public Ability getABILITY() {
        return ABILITY;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LeaderEnum getLeaderEnum() {
        return LEADER_ENUM;
    }

    public static Leader getLeaderFromType(String type) {
        for (
                RealmsNorthernLeaders cardEnum : RealmsNorthernLeaders.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getLeader();
            }
        }
        for (
                ScoiaTaelLeaders cardEnum : ScoiaTaelLeaders.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getLeader();
            }
        }
        for (
                MonstersLeaders cardEnum : MonstersLeaders.values()) {
            if (cardEnum.toString().equals(type)) {
                return cardEnum.getLeader();
            }
        }
        for (
                EmpireNilfgaardianLeaders cardEnum : EmpireNilfgaardianLeaders.values()) {
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
