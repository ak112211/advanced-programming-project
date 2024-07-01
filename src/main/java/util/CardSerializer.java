package util;

import com.google.gson.*;
import enums.Row;
import enums.cards.*;
import enums.cardsinformation.Description;
import enums.cardsinformation.Faction;
import model.abilities.Ability;
import model.card.Card;

import java.lang.reflect.Type;

public class CardSerializer implements JsonSerializer<Card>, JsonDeserializer<Card> {
    @Override
    public JsonElement serialize(Card src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("CARD_ENUM", context.serialize(src.getCardEnum()));
        jsonObject.add("POWER", context.serialize(src.getPower()));
        jsonObject.add("ROW", context.serialize(src.getRow()));
        return jsonObject;
    }

    @Override
    public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return Card.getCardFromType(jsonObject.get("CARD_ENUM").toString(), Integer.parseInt(jsonObject.get("POWER").toString()), Row.valueOf(jsonObject.get("ROW").toString()));
    }
}
