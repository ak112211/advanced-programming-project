package view;

import enums.Menu;
import enums.cardsinformation.Faction;
import enums.leaders.MonstersLeaders;
import enums.leaders.RealmsNorthernLeaders;
import enums.leaders.ScoiaTaelLeaders;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.App;
import model.Deck;
import model.Game;
import model.User;

import java.io.IOException;

public class GameLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            App.setMenu(Menu.GAME_PANE);
            App.setStage(primaryStage);
            if (Game.getCurrentGame() == null) { // for opening project with GameLauncher.main for testing game only
                initialize();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GamePane.fxml"));
            Parent root = loader.load();
            App.setCurrentController(loader.getController());

            Scene scene = new Scene(root, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gwent Game");
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initialize() {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();
        deck1.setFaction(Faction.SCOIA_TAEL);
        deck2.setFaction(Faction.MONSTER);
        deck1.addCards(deck1.getFaction().getAllCards());
        deck2.addCards(deck2.getFaction().getAllCards());
        deck1.setLeader(ScoiaTaelLeaders.DAISY_OF_THE_VALLEY.getLeader());
        deck2.setLeader(MonstersLeaders.BRINGER_OF_DEATH.getLeader());

        User user1 = new User("username1", "nickname1", "email1", "password1");
        User user2 = new User("username2", "nickname2", "email2", "password2");
        user1.setDeck(deck1);
        user2.setDeck(deck2);
        Game.setCurrentGame(new Game(user1, user2));
        User.setCurrentUser(user1);
    }
}
