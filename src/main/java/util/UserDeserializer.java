package util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Deck;
import model.Game;
import model.User;
import model.card.Card;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDeserializer implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String username = jsonObject.get("username").getAsString().replaceAll("\"", "");
        String nickname = jsonObject.has("nickname") ? jsonObject.get("nickname").getAsString().replaceAll("\"", "") : null;
        String email = jsonObject.has("email") ? jsonObject.get("email").getAsString().replaceAll("\"", "") : null;
        String password = jsonObject.has("password") ? jsonObject.get("password").getAsString().replaceAll("\"", "") : null;

        User user = new User(username, nickname, email, password);

        user.setSecurityQuestion(jsonObject.has("securityQuestion") ? jsonObject.get("securityQuestion").getAsString().replaceAll("\"", "") : null);
        user.setAnswer(jsonObject.has("answer") ? jsonObject.get("answer").getAsString().replaceAll("\"", "") : null);
        user.setVerified(jsonObject.has("verified") && jsonObject.get("verified").getAsBoolean());
        user.setTwoFactorOn(jsonObject.has("twoFactorOn") && jsonObject.get("twoFactorOn").getAsBoolean());

        user.setHighScore(jsonObject.has("highScore") ? jsonObject.get("highScore").getAsInt() : 0);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Deck.class, new DeckDeserializer());
        gsonBuilder.registerTypeAdapter(Game.class, new GameDeserializer());
        Gson gson = gsonBuilder.create();

        if (jsonObject.has("deck")) {
            Deck deck = gson.fromJson(jsonObject.get("deck"), Deck.class);
            user.setDeck(deck);
        }

        if (jsonObject.has("decks")) {
            ArrayList<Deck> decks = gson.fromJson(jsonObject.get("decks"), new TypeToken<ArrayList<Deck>>(){}.getType());
            user.setDecks(decks);
        }

        if (jsonObject.has("friends")) {
            List<String> friends = context.deserialize(jsonObject.get("friends"), new TypeToken<List<String>>() {}.getType());
            user.setFriends(friends);
        }

        if (jsonObject.has("pendingRequests")) {
            List<String> pendingRequests = context.deserialize(jsonObject.get("pendingRequests"), new TypeToken<List<String>>() {}.getType());
            user.setPendingRequests(pendingRequests);
        }

        if (jsonObject.has("playCard")) {
            Card playCard = context.deserialize(jsonObject.get("playCard"), Card.class);
            user.setPlayCard(playCard);
        }

        if (jsonObject.has("games")) {
            List<Integer> friends = context.deserialize(jsonObject.get("games"), new TypeToken<List<Integer>>() {}.getType());
            user.setGames(friends);
        }

        return user;
    }
}
