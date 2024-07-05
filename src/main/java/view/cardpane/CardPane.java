package view.cardpane;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import view.Tools;

public abstract class CardPane extends Pane {
    public static final int SMALL_WIDTH = 53;
    public static final int SMALL_HEIGHT = 79;

    protected final String imagePath;
    protected final CardIcon icon;
    private Rectangle image;
    private Label powerLabel;

    public CardPane(String imagePath, CardIcon icon) {
        this.imagePath = imagePath;
        this.icon = icon;
    }

    public void setSmallImage() {
        setWidth(SMALL_WIDTH);
        setHeight(SMALL_HEIGHT);

        getChildren().clear();
        try {
            image = new Rectangle(SMALL_WIDTH, SMALL_HEIGHT, Tools.getImagePattern(imagePath.replaceFirst("/lg/", "/sm/")));
        } catch (RuntimeException e) {
            image = new Rectangle(SMALL_WIDTH, SMALL_HEIGHT, Tools.getImagePattern(imagePath));
        }
        image.setArcWidth(5);
        image.setArcHeight(5);
        getChildren().add(image);
        getChildren().add(icon.getPowerIcon());
        if (!icon.getType().isSpecial()) {
            powerLabel = icon.getPowerLabel();
            getChildren().add(powerLabel);
            getChildren().add(icon.getTypeIcon());
            if (icon.getAbilityName() != null) {
                getChildren().add(icon.getAbilityIcon());
            }
        }
    }

    protected void setPowerText(int power, int firstPower) {
        icon.setPowerText(powerLabel, power, firstPower);
    }

    public void setBigImage() {
        setWidth(70);
        setHeight(100);
        getChildren().clear();
        image = new Rectangle(70, 100, Tools.getImagePattern(imagePath));
        image.setArcWidth(10);
        image.setArcHeight(10);
        getChildren().add(image);
    }

    public void setStroke() {
        image.setStroke(Paint.valueOf("FFFFA0B0"));
        image.setStrokeWidth(2);
    }

    public void removeStroke() {
        image.setStroke(null);
        image.setStrokeWidth(0);
    }


    public Rectangle getBigRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        rectangle.setWidth(70);
        rectangle.setHeight(100);
        rectangle.setFill(Tools.getImagePattern(imagePath));
        return rectangle;
    }

}
