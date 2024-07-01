package model.abilities;

import enums.cardsinformation.Type;
import javafx.scene.image.Image;
import model.card.Card;

import java.util.Objects;

public abstract class Ability {
    private String iconPath;
    public static boolean canBeAffected(Card card) {
        return card.getType() != Type.SPELL && card.getType() != Type.DECOY && !card.isHero();
    }

    public void setIconPath(String name) {
        iconPath = "/gwentImages/img/icons/card_ability_" + name + ".png";
    }

    public Image getIcon() {
        return new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toExternalForm());
    }

    public Image getAnimationIcon() {
        if (! (this instanceof Animatable)) {
            throw new UnsupportedOperationException("This ability is not animatable");
        }
        return new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toExternalForm());
    }
}