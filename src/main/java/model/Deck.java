package model;

import enums.cardsinformation.Faction;
import model.card.Card;
import model.card.Leader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.ArrayList;

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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
