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
        return jsonObject;
    }

    @Override
    public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        for (RealmsNorthernCards cardEnum : RealmsNorthernCards.values()) {
            if (cardEnum.toString().equals(jsonObject.get("CARD_ENUM").toString())) {
                return cardEnum.getCard();
            }
        }
        for (ScoiaTaelCards cardEnum : ScoiaTaelCards.values()) {
            if (cardEnum.toString().equals(jsonObject.get("CARD_ENUM").toString())) {
                return cardEnum.getCard();
            }        }
        for (MonstersCards cardEnum : MonstersCards.values()) {
            if (cardEnum.toString().equals(jsonObject.get("CARD_ENUM").toString())) {
                return cardEnum.getCard();
            }        }
        for (EmpireNilfgaardianCards cardEnum : EmpireNilfgaardianCards.values()) {
            if (cardEnum.toString().equals(jsonObject.get("CARD_ENUM").toString())) {
                return cardEnum.getCard();
            }        }
        for (SkelligeCards cardEnum : SkelligeCards.values()) {
            if (cardEnum.toString().equals(jsonObject.get("CARD_ENUM").toString())) {
                return cardEnum.getCard();
            }        }
        for (NeutralCards cardEnum : NeutralCards.values()) {
            if (cardEnum.toString().equals(jsonObject.get("CARD_ENUM").toString())) {
                return cardEnum.getCard();
            }
        }

        return null;
    }
}
