package enums.leaders;

import enums.cardsinformation.Faction;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.ResetGraveyardToDeck;
import model.abilities.passiveabilities.WeatherEndurance;
import model.card.Leader;

public enum SkelligeLeaders {
    CRACH_AN_CRAITE("Crach an Craite", new ResetGraveyardToDeck(), "Shuffle all dead cards back into both players' decks.", "/gwentImages/img/lg/skellige_crach_an_craite.jpg"),
    KING_BRAN("King Bran", new WeatherEndurance(), "Units only lose half their power due to weather effects.", "/gwentImages/img/lg/skellige_king_bran.jpg");

    private final String NAME;
    private final Ability ABILITY;
    private final String DESCRIPTION;
    private final String IMAGE_PATH;

    SkelligeLeaders(String name, Ability ability, String description, String imagePath) {
        NAME = name;
        ABILITY = ability;
        DESCRIPTION = description;
        IMAGE_PATH = imagePath;
    }

    public Leader getLeader() {
        return new Leader(NAME, ABILITY, Faction.SKELLIGE, DESCRIPTION, IMAGE_PATH);
    }
}
