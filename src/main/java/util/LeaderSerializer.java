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
        jsonObject.add("LEADER_ENUM", context.serialize(src.getLeaderEnum()));
        return jsonObject;
    }

    @Override
    public Leader deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        for (RealmsNorthernLeaders cardEnum : RealmsNorthernLeaders.values()) {
            if (cardEnum.toString().equals(jsonObject.get("LEADER_ENUM").toString())) {
                return cardEnum.getLeader();
            }
        }
        for (ScoiaTaelLeaders cardEnum : ScoiaTaelLeaders.values()) {
            if (cardEnum.toString().equals(jsonObject.get("LEADER_ENUM").toString())) {
                return cardEnum.getLeader();
            }
        }
        for (MonstersLeaders cardEnum : MonstersLeaders.values()) {
            if (cardEnum.toString().equals(jsonObject.get("LEADER_ENUM").toString())) {
                return cardEnum.getLeader();
            }
        }
        for (EmpireNilfgaardianLeaders cardEnum : EmpireNilfgaardianLeaders.values()) {
            if (cardEnum.toString().equals(jsonObject.get("LEADER_ENUM").toString())) {
                return cardEnum.getLeader();
            }
        }
        for (ScoiaTaelLeaders cardEnum : ScoiaTaelLeaders.values()) {
            if (cardEnum.toString().equals(jsonObject.get("LEADER_ENUM").toString())) {
                return cardEnum.getLeader();
            }
        }
        return null;
    }
}
