package model;

import enums.cardsinformation.Faction;
import model.abilities.Ability;
import model.card.Card;
import model.card.Leader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.CardSerializer;
import util.LeaderSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {
    private String name;
    private String fileAddress;
    private Faction faction;
    private Leader leader;
    private final ArrayList<Card> CARDS = new ArrayList<Card>();

    public ArrayList<Card> getCards() {
        return CARDS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public void addCard(Card card2) {
        CARDS.add(card2);
    }

    public void addCards(List<Card> cards) {
        CARDS.addAll(cards);
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
                .create();
        return gson.fromJson(json, Deck.class);
    }

    public void saveToFile(String filePath) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Card.class, new CardSerializer())
                    .registerTypeAdapter(Leader.class, new LeaderSerializer())
                    .setPrettyPrinting()
                    .create();
            gson.toJson(this, writer);
        }
    }

    public static Deck loadFromFile(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Card.class, new CardSerializer())
                    .registerTypeAdapter(Leader.class, new LeaderSerializer())
                    .create();
            return gson.fromJson(reader, Deck.class);
        }
    }
}
