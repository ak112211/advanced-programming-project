module Gwent {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires java.desktop;
    requires javafx.media;
    requires org.json;

    exports view;
    opens view to javafx.fxml;

}