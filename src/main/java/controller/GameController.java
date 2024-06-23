package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.input.KeyEvent;
import model.Game;
import model.User;
import util.GameSerializer;
import java.io.FileWriter;
import java.io.IOException;

public class GameController {

    public static Game game;

    public static void handleKeyPress(KeyEvent event) {

    }



    public static void saveGameState(Game game) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Game.class, new GameSerializer())
                .create();

        String gameState = gson.toJson(game, Game.class);
        try (FileWriter writer = new FileWriter("game_state_" + User.getCurrentUser().getUsername() + ".json")) {
            writer.write(gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
