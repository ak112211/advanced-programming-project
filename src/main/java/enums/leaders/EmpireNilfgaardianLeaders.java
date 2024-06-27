package enums.leaders;

import enums.cards.NeutralCards;
import enums.cardsinformation.CardsPlace;
import enums.cardsinformation.Faction;
import model.abilities.Ability;
import model.abilities.instantaneousabilities.GetCard;
import model.abilities.instantaneousabilities.PlayCard;
import model.abilities.instantaneousabilities.SeeCards;
import model.abilities.passiveabilities.CancelLeaderAbility;
import model.abilities.passiveabilities.DisruptMedic;
import model.card.Leader;

public enum EmpireNilfgaardianLeaders {
    THE_WHITE_FLAME("The White Flame", new PlayCard(CardsPlace.DECK, NeutralCards.TORRENTIAL_RAIN), "Select and play a Torrential Rain card from your deck.", "/gwentImages/img/lg/nilfgaard_emhyr_silver.jpg"),
    HIS_IMPERIAL_MAJESTY("His Imperial Majesty", new SeeCards(3), "View three random cards from the opponent's hand.", "/gwentImages/img/lg/nilfgaard_emhyr_copper.jpg"),
    EMPEROR_OF_NILFGAARD("Emperor of Nilfgaard", new CancelLeaderAbility(), "Cancels the opponent's leader ability.", "/gwentImages/img/lg/nilfgaard_emhyr_bronze.jpg"),
    THE_RELENTLESS("The Relentless", new GetCard(CardsPlace.GRAVEYARD, false, 1, false), "Take a card from the opponent's graveyard (cannot take a Hero card).", "/gwentImages/img/lg/nilfgaard_emhyr_gold.jpg"),
    INVADER_OF_THE_NORTH("Invader of the North", new DisruptMedic(), "Abilities that restore a unit to the battlefield restore a randomly-chosen unit. Affects both players.", "/gwentImages/img/lg/nilfgaard_emhyr_invader_of_the_north.jpg");

    private final String NAME;
    private final Ability ABILITY;
    private final String DESCRIPTION;
    private final String IMAGE_PATH;

    EmpireNilfgaardianLeaders(String name, Ability ability, String description, String imagePath) {
        NAME = name;
        ABILITY = ability;
        DESCRIPTION = description;
        IMAGE_PATH = imagePath;
    }

    public Leader getLeader() {
        return new Leader(NAME, ABILITY, Faction.EMPIRE_NILFGAARDIAM, DESCRIPTION, IMAGE_PATH);
    }
}
