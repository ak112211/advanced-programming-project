module Gwent {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;
    requires org.json;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires java.mail;

    exports view;
    exports model;
    exports util;
    opens view to javafx.fxml, com.fasterxml.jackson.databind;
    opens model to com.google.gson, com.fasterxml.jackson.databind;
    opens enums.cardsinformation to com.google.gson, com.fasterxml.jackson.databind;
    opens model.card to com.google.gson, com.fasterxml.jackson.databind;
    opens model.abilities to com.google.gson, com.fasterxml.jackson.databind;
    opens model.abilities.instantaneousabilities to com.google.gson;
    opens enums.cards to com.google.gson;
    opens enums.leaders to com.google.gson;
}
