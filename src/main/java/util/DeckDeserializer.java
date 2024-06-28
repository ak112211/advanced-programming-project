package util;

import com.google.gson.*;
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

        List<Card> cards = new ArrayList<>();
        JsonArray cardsArray = jsonObject.getAsJsonArray("CARDS");
        for (JsonElement cardElement : cardsArray) {
            JsonObject jsonElement = cardElement.getAsJsonObject();
            cards.add(Card.getCardFromType(jsonElement.get("CARD_ENUM").toString()));
        }

        Deck deck = new Deck();
        deck.setFaction(faction);
        deck.setLeader(Leader.getLeaderFromType(jsonObject.get("leader").getAsJsonObject().get("LEADER_ENUM").toString()));

        return deck;
    }
}
