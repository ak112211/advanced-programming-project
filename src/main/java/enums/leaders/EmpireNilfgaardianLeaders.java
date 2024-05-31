package enums.leaders;

import enums.cardsinformation.Faction;
import model.card.Leader;

public enum EmpireNilfgaardianLeaders {
    THE_WHITE_FLAME("The White Flame", "Play Torrential Rain", "Select and play a Torrential Rain card from your deck."),
    HIS_IMPERIAL_MAJESTY("His Imperial Majesty", "See 3 Random Opponent Cards", "View three random cards from the opponent's hand."),
    EMPEROR_OF_NILFGAARD("Emperor of Nilfgaard", "Cancel Opponent Leader Ability", "Cancels the opponent's leader ability."),
    THE_RELENTLESS("The Relentless", "Take a Dead Card from Opponent", "Take a card from the opponent's graveyard (cannot take a Hero card)."),
    INVADER_OF_THE_NORTH("Invader of the North", "Revive a Random Dead Card for Both", "Revives a random card from both players' graveyards and returns them to play.");

    private final String name;
    private final String ability;
    private final String description;

    EmpireNilfgaardianLeaders(String name, String ability, String description) {
        this.name = name;
        this.ability = ability;
        this.description = description;
    }

    public Leader getLeader() {
        return new Leader(name, Faction.EMPIRE_NILFGAARDIAM, description);
    }
}
