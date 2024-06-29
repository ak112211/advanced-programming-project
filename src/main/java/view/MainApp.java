package view;

import enums.Menu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.App;
import util.ServerConnection;

import java.util.Objects;

public class MainApp extends Application {
    public LoginMenuController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        App.setStage(stage);
        App.setServerConnection(new ServerConnection());

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gwentImages/img/icon.png")));
        stage.getIcons().add(image);

        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Menu.LOGIN_MENU.getPath())));
        controller = fxmlLoader.getController();
        stage.setScene(new Scene(pane));
        stage.show();
    }
}
