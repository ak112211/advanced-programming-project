package view.cardpane;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class CardPane extends StackPane {
    protected final String imagePath;
    private Rectangle image;

    public CardPane(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setSmallImage() {
        setWidth(53);
        setHeight(79);
        getChildren().clear();
        try {
            image = new Rectangle(53, 79, getImagePattern(imagePath.replaceFirst("/lg/", "/sm/")));
        } catch (RuntimeException e){
            image = new Rectangle(53, 79, getImagePattern(imagePath));
        }
        image.setArcWidth(5);
        image.setArcHeight(5);
        getChildren().add(image);
    }

    public void setBigImage() {
        setWidth(70);
        setHeight(100);
        getChildren().clear();
        image = new Rectangle(70, 100, getImagePattern(imagePath));
        image.setArcWidth(10);
        image.setArcHeight(10);
        getChildren().add(image);
    }

    private static ImagePattern getImagePattern(String path) {
        return new ImagePattern(new Image(Objects.requireNonNull(CardPane.class.getResource(path)).toExternalForm()));
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
        rectangle.setFill(new ImagePattern(new Image(
                Objects.requireNonNull(getClass().getResource(imagePath))
                        .toExternalForm())));
        return rectangle;
    }
}
