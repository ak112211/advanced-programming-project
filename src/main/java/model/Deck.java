package model;

import enums.cardsinformation.Faction;
import model.card.Card;
import model.card.Leader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.CardSerializer;
import util.DeckDeserializer;
import util.LeaderSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {
    private Faction faction;
    private Leader leader;
    private final ArrayList<Card> cards = new ArrayList<Card>();

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card2) {
        cards.add(card2);
    }

    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Card.class, new CardSerializer())
                .registerTypeAdapter(Leader.class, new LeaderSerializer())
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    public static Deck fromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Card.class, new CardSerializer())
                .registerTypeAdapter(Leader.class, new LeaderSerializer())
                .registerTypeAdapter(Deck.class, new DeckDeserializer())
                .create();
        return gson.fromJson(json, Deck.class);
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards.clear();
        this.cards.addAll(cards);
    }
}
