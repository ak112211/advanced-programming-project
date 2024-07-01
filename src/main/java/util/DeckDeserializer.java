package util;

import com.google.gson.*;
import enums.Row;
import model.Deck;
import enums.cards.CardEnum;
import enums.cardsinformation.Faction;
import enums.leaders.LeaderEnum;
import model.card.Card;
import model.card.Leader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeckDeserializer implements JsonDeserializer<Deck> {
    @Override
    public Deck deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Faction faction = Faction.valueOf(jsonObject.get("faction").getAsString());

        ArrayList<Card> cards = new ArrayList<>();
        JsonArray cardsArray = jsonObject.getAsJsonArray("cards");
        for (JsonElement cardElement : cardsArray) {
            JsonObject jsonElement = cardElement.getAsJsonObject();
            cards.add(Card.getCardFromType(jsonElement.get("CARD_ENUM").toString().replaceAll("\"", ""),
                    Integer.parseInt(jsonElement.get("POWER").toString().replaceAll("\"", "")),
                    Row.valueOf(jsonElement.get("ROW").toString().replaceAll("\"", ""))));
        }

        Deck deck = new Deck();
        deck.setFaction(faction);
        deck.setCards(cards);
        deck.setLeader(Leader.getLeaderFromType(jsonObject.get("leader").getAsJsonObject().get("LEADER_ENUM")
                .toString().replaceAll("\"", ""), Integer.parseInt(jsonObject.get("leader").getAsJsonObject().get("NUMBER_OF_ACTIONS")
                        .toString().replaceAll("\"", "")))
                );

        return deck;
    }
}
