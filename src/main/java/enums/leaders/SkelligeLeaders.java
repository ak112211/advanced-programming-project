package enums.leaders;

import enums.cardsinformation.Faction;
import model.card.Leader;

public enum SkelligeLeaders {
    CRACH_AN_CRAITE("Crach an Craite", "Shuffle Dead Cards into Decks", "Shuffle all dead cards back into both players' decks."),
    KING_BRAN("King Bran", "Halve Weather Effects", "Units only lose half their power due to weather effects.");

    private final String name;
    private final String ability;
    private final String description;

    SkelligeLeaders(String name, String ability, String description) {
        this.name = name;
        this.ability = ability;
        this.description = description;
    }

    public Leader getLeader() {
        return new Leader(name, Faction.SKELLIGE, description);
    }
}
