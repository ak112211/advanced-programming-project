package util;

import com.google.gson.*;
import model.card.Card;

import java.lang.reflect.Type;

public class CardSerializer implements JsonSerializer<Card>, JsonDeserializer<Card> {
    @Override
    public JsonElement serialize(Card src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("CARD_ENUM", context.serialize(src.getCardEnum()));
        return jsonObject;
    }

    @Override
    public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return Card.getCardFromEnumString(jsonObject.get("CARD_ENUM").toString());
    }
}
