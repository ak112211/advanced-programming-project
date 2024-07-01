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
        jsonObject.add("card_enum", context.serialize(src.getCardEnum()));
        jsonObject.add("power", context.serialize(src.getPower()));
        if (src.getRow() == null) {
            jsonObject.add("row", context.serialize(""));
        } else {
            jsonObject.add("row", context.serialize(src.getRow()));
        }
        return jsonObject;
    }

    @Override
    public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.get("row").toString().replaceAll("\"", "").isEmpty()) {
            return Card.getCardFromSaved(jsonObject.get("card_enum").toString(), Integer.parseInt(jsonObject.get("power").toString()), null);
        } else {
            return Card.getCardFromSaved(jsonObject.get("card_enum").toString(), Integer.parseInt(jsonObject.get("power").toString()), Row.valueOf(jsonObject.get("row").toString()));
        }
    }
}
