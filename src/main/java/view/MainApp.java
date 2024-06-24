package view;

import controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {
    public LoginController controller;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        AppController.setStage(stage);

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gwentImages/img/icon.png")));
        stage.getIcons().add(image);

        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane pane = fxmlLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/LoginScreen.fxml")));
        controller = fxmlLoader.getController();
        stage.setScene(new Scene(pane));
        stage.show();
    }
}
