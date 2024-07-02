package view;

import enums.Menu;
import enums.cardsinformation.Faction;
import enums.cards.*;
import enums.leaders.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.App;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;
import model.Deck;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static view.Tools.clearUserSession;

public class MainMenuController {
    @FXML
    public Label usernameField;
    @FXML
    public ImageView backgroundImageView;

    @FXML
    private void initialize() {
        usernameField.setText("Hi " + User.getCurrentUser().getUsername() + "!");
        backgroundImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gwentImages/img/maxresdefault.jpg"))));
    }

    @FXML
    private void goToProfile() {
        App.loadScene(Menu.PROFILE_MENU.getPath());
    }

    @FXML
    private void goToAddFriends() {
        App.loadScene(Menu.CHAT_MENU.getPath());
    }

    public void showScoreboard(ActionEvent actionEvent) {
        App.loadScene("/fxml/Scoreboard.fxml");
    }

    public void goToDeckMenu(ActionEvent actionEvent) {
        ChooseDeckMenuController.isMulti = false;
        App.loadScene(Menu.DECK_MENU.getPath());

    }

    public void logout(ActionEvent actionEvent) {
        App.getServerConnection().sendMessage(User.getCurrentUser().getUsername(), "logout");
        User.setCurrentUser(null);
        Game.setCurrentGame(null);
        clearUserSession();
        App.loadScene(Menu.LOGIN_MENU.getPath());
    }
}
