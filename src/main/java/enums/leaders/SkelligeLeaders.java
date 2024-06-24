package enums.leaders;

import enums.cardsinformation.Faction;
import model.abilities.Ability;
import model.card.Leader;

public enum SkelligeLeaders {
    CRACH_AN_CRAITE("Crach an Craite", "Shuffle Dead Cards into Decks", "Shuffle all dead cards back into both players' decks.", "/gwentImages/img/lg/skellige_crach_an_craite.jpg"),
    KING_BRAN("King Bran", "Halve Weather Effects", "Units only lose half their power due to weather effects.", "/gwentImages/img/lg/skellige_king_bran.jpg");

    private final String name;
    private final Ability ability;
    private final String description;
    private final String imagePath;

    SkelligeLeaders(String name, Ability ability, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.ability = ability;
        this.imagePath = imagePath;
    }

    public Leader getLeader() {
        return new Leader(name, ability, Faction.SKELLIGE, description, imagePath);
    }
}
