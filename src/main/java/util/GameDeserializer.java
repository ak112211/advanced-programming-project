package util;

import com.google.gson.*;
import enums.Row;
import enums.cardsinformation.Faction;
import model.Deck;
import model.Game;
import model.RoundsInfo;
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
        boolean isPlayer1Turn = jsonObject.get("isPlayer1Turn").getAsBoolean();
        boolean vetoForPLayer1Shown = jsonObject.get("vetoForPLayer1Shown").getAsBoolean();
        boolean vetoForPLayer2Shown = jsonObject.get("vetoForPLayer2Shown").getAsBoolean();
        boolean player1HasPassed = jsonObject.get("player1HasPassed").getAsBoolean();
        boolean player2HasPassed = jsonObject.get("player2HasPassed").getAsBoolean();
        boolean player1UsedLeaderAbility = jsonObject.get("player1UsedLeaderAbility").getAsBoolean();
        boolean player2UsedLeaderAbility = jsonObject.get("player2UsedLeaderAbility").getAsBoolean();
        Game.GameStatus status = Game.GameStatus.valueOf(jsonObject.get("status").getAsString().replaceAll("\"", ""));
        RoundsInfo roundsInfo = gson.fromJson(jsonObject.get("roundsInfo"), RoundsInfo.class);
        int ID = jsonObject.get("ID").getAsInt();

        Faction player1Faction = Faction.valueOf(jsonObject.get("player1Faction").getAsString());
        Faction player2Faction = Faction.valueOf(jsonObject.get("player2Faction").getAsString());

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

        return new Game(ID, player1, player2, date, isPlayer1Turn, vetoForPLayer1Shown, vetoForPLayer2Shown,
                player1HasPassed, player2HasPassed, player1UsedLeaderAbility, player2UsedLeaderAbility,
                player1Deck, player2Deck, player1InHandCards, player2InHandCards,
                player1GraveyardCards, player2GraveyardCards, inGameCards, player1LeaderCard, player2LeaderCard,
                player1Faction, player2Faction, status, roundsInfo);
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
