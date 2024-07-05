package view.cardpane;

import enums.cardsinformation.Type;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import view.Tools;

import static javafx.geometry.Pos.CENTER;

public class CardIcon {
    private final String abilityName;
    private final Type type;
    private final boolean isHero;

    public CardIcon(String abilityName, Type type, boolean isHero) {
        this.abilityName = abilityName;
        this.type = type;
        this.isHero = isHero;
    }

    public ImageView getAbilityIcon() {
        ImageView imageView = Tools.getImageView("/gwentImages/img/icons/card_ability_" + abilityName + ".png");
        imageView.setFitWidth(17);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(15);
        imageView.setLayoutY(60);
        return imageView;
    }

    public ImageView getTypeIcon() {
        ImageView imageView = Tools.getImageView("/gwentImages/img/icons/card_row_"
                + type.toString().split("_")[0].toLowerCase() + ".png");
        imageView.setFitWidth(17);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(34);
        imageView.setLayoutY(60);
        return imageView;
    }

    public ImageView getPowerIcon() {
        String powerIconName = type.isSpecial() ? abilityName : isHero ? "hero" : "normal";
        ImageView imageView = Tools.getImageView("/gwentImages/img/icons/power_" + powerIconName + ".png");
        imageView.setFitWidth(37);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(-2);
        imageView.setLayoutY(-2);
        return imageView;
    }

    public Label getPowerLabel() {
        Label label = new Label();
        label.setPrefWidth(19);
        label.setPrefHeight(19);
        label.setAlignment(CENTER);
        label.setBackground(null);
        return label;
    }

    public void setPowerText(Label powerLabel, Integer power, Integer firstPower) {
        powerLabel.setText(power.toString());
        if (power.equals(firstPower)) {
            if (isHero) {
                powerLabel.setTextFill(Paint.valueOf("FFFFFF"));
            } else {
                powerLabel.setTextFill(Paint.valueOf("000000"));
            }
        } else {
            powerLabel.setTextFill(Paint.valueOf("FF0000"));
        }
    }

    public ImageView getAnimationIcon() {
        return Tools.getImageView("/gwentImages/img/icons/anim_" + abilityName + ".png");
    }

    public Type getType() {
        return type;
    }

    public boolean isHero() {
        return isHero;
    }

    public String getAbilityName() {
        return abilityName;
    }
}
