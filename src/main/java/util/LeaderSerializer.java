package util;

import com.google.gson.*;
import enums.cards.*;
import enums.leaders.*;
import model.card.Leader;

import java.lang.reflect.Type;

public class LeaderSerializer implements JsonSerializer<Leader>, JsonDeserializer<Leader> {
    @Override
    public JsonElement serialize(Leader src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("leader_enum", context.serialize(src.getLeaderEnum()));
        jsonObject.add("number_of_actions", context.serialize(src.getNumberOfAction()));
        return jsonObject;
    }

    @Override
    public Leader deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return Leader.getLeaderFromType(jsonObject.get("leader_enum").toString(), Integer.parseInt(jsonObject.get("number_of_actions").toString()));
    }
}
