package model.card;

import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.abilities.Ability;

import java.awt.*;
import java.util.Objects;

public class Leader extends Rectangle {
    private final String NAME;
    private final Faction FACTION;
    private final String DESCRIPTION;
    private final Ability ABILITY;
    private int numberOfAction = 1;

    private String imagePath;
    public Leader(String name, Ability ability, Faction faction, String description, String imagePath) {
        NAME = name;
        imagePath = imagePath;
        FACTION = faction;
        DESCRIPTION = description;
        ABILITY = ability;

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
}
