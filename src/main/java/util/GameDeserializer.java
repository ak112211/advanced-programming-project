package util;

import com.google.gson.*;
import enums.Row;
import model.Deck;
import model.Game;
import model.User;
import model.card.Card;
import model.card.Leader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class GameDeserializer implements JsonDeserializer<Game> {

    @Override
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserDeserializer());
        gsonBuilder.registerTypeAdapter(Deck.class, new DeckDeserializer());
        Gson gson = gsonBuilder.create();

        User player1 = gson.fromJson(jsonObject.get("player1"), User.class);
        User player2 = gson.fromJson(jsonObject.get("player2"), User.class);
        Date date = context.deserialize(jsonObject.get("date"), Date.class);
        User currentPlayer = gson.fromJson(jsonObject.get("currentPlayer"), User.class);
        Game.GameStatus status = Game.GameStatus.valueOf(jsonObject.get("status").getAsString().replaceAll("\"", ""));
        User winner = gson.fromJson(jsonObject.get("winner"), User.class);
        int ID = jsonObject.get("ID").getAsInt();

        Leader player1LeaderCard = Leader.getLeaderFromType(jsonObject.get("player1LeaderCard").getAsJsonObject().get("leader_enum").toString().replaceAll("\"", ""),
                Integer.parseInt(jsonObject.get("player1LeaderCard").getAsJsonObject().get("number_of_actions").toString().replaceAll("\"", "")));
        Leader player2LeaderCard = Leader.getLeaderFromType(jsonObject.get("player2LeaderCard").getAsJsonObject().get("leader_enum").toString().replaceAll("\"", ""),
                Integer.parseInt(jsonObject.get("player2LeaderCard").getAsJsonObject().get("number_of_actions").toString().replaceAll("\"", "")));

        ArrayList<Card> inGameCards = deserializeCards(jsonObject.getAsJsonArray("inGameCards"), context);
        ArrayList<Card> player1InHandCards = deserializeCards(jsonObject.getAsJsonArray("player1InHandCards"), context);
        ArrayList<Card> player2InHandCards = deserializeCards(jsonObject.getAsJsonArray("player2InHandCards"), context);
        ArrayList<Card> player1Deck = deserializeCards(jsonObject.getAsJsonArray("player1Deck"), context);
        ArrayList<Card> player2Deck = deserializeCards(jsonObject.getAsJsonArray("player2Deck"), context);
        ArrayList<Card> player1GraveyardCards = deserializeCards(jsonObject.getAsJsonArray("player1GraveyardCards"), context);
        ArrayList<Card> player2GraveyardCards = deserializeCards(jsonObject.getAsJsonArray("player2GraveyardCards"), context);

        Game game = new Game(player1, player2, date, player1Deck, player2Deck, player1InHandCards, player2InHandCards,
                player1GraveyardCards, player2GraveyardCards, inGameCards, player1LeaderCard, player2LeaderCard, status, winner, currentPlayer.getUsername().equals(player1.getUsername()) ? player1 : player2);
        game.setID(ID);

        return game;
    }

    private ArrayList<Card> deserializeCards(JsonArray jsonArray, JsonDeserializationContext context) {
        ArrayList<Card> cards = new ArrayList<>();
        for (JsonElement cardElement : jsonArray) {
            JsonObject jsonElement = cardElement.getAsJsonObject();
            cards.add(Card.getCardFromSaved(jsonElement.get("card_enum").getAsString().replaceAll("\"", ""), Integer.parseInt(jsonElement.get("power").getAsString().replaceAll("\"", "")),
                    jsonElement.get("row").toString().replaceAll("\"", "").isEmpty() ? null : Row.valueOf(jsonElement.get("row").getAsString().replaceAll("\"", ""))));
        }
        return cards;
    }
}
