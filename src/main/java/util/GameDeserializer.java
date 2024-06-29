package util;

import com.google.gson.*;
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

        User player1 = context.deserialize(jsonObject.get("PLAYER1"), User.class);
        User player2 = context.deserialize(jsonObject.get("PLAYER2"), User.class);
        Date date = context.deserialize(jsonObject.get("DATE"), Date.class);

        Leader player1LeaderCard = Leader.getLeaderFromType(jsonObject.get("PLAYER1_LEADER_CARD").getAsString());
        Leader player2LeaderCard = Leader.getLeaderFromType(jsonObject.get("PLAYER2_LEADER_CARD").getAsString());

        ArrayList<Card> inGameCards = deserializeCards(jsonObject.getAsJsonArray("IN_GAME_CARDS"), context);
        ArrayList<Card> player1InHandCards = deserializeCards(jsonObject.getAsJsonArray("PLAYER1_IN_HAND_CARDS"), context);
        ArrayList<Card> player2InHandCards = deserializeCards(jsonObject.getAsJsonArray("PLAYER2_IN_HAND_CARDS"), context);
        ArrayList<Card> player1Deck = deserializeCards(jsonObject.getAsJsonArray("player1Deck"), context);
        ArrayList<Card> player2Deck = deserializeCards(jsonObject.getAsJsonArray("player2Deck"), context);
        ArrayList<Card> player1GraveyardCards = deserializeCards(jsonObject.getAsJsonArray("PLAYER1_GRAVEYARD_CARDS"), context);
        ArrayList<Card> player2GraveyardCards = deserializeCards(jsonObject.getAsJsonArray("PLAYER2_GRAVEYARD_CARDS"), context);

        Game game = new Game(player1, player2);
        game.setCurrentPlayer(context.deserialize(jsonObject.get("currentPlayer"), User.class));
        game.setStatus(Game.GameStatus.valueOf(jsonObject.get("status").getAsString()));
        game.setWinner(context.deserialize(jsonObject.get("winner"), User.class));

        game.getInGameCards().addAll(inGameCards);
        game.getPlayer1InHandCards().addAll(player1InHandCards);
        game.getPlayer2InHandCards().addAll(player2InHandCards);
        game.getPlayer1Deck().addAll(player1Deck);
        game.getPlayer2Deck().addAll(player2Deck);
        game.getPlayer1GraveyardCards().addAll(player1GraveyardCards);
        game.getPlayer2GraveyardCards().addAll(player2GraveyardCards);
        game.setPLAYER1_LEADER_CARD(player1LeaderCard);
        game.setPLAYER2_LEADER_CARD(player2LeaderCard);
        return game;
    }

    private ArrayList<Card> deserializeCards(JsonArray jsonArray, JsonDeserializationContext context) {
        ArrayList<Card> cards = new ArrayList<>();
        for (JsonElement cardElement : jsonArray) {
            JsonObject jsonElement = cardElement.getAsJsonObject();
            cards.add(Card.getCardFromType(jsonElement.get("CARD_ENUM").getAsString()));
        }
        return cards;
    }
}
