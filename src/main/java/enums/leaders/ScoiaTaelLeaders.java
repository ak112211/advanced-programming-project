package enums.leaders;

import enums.cardsinformation.Faction;
import model.abilities.Ability;
import model.card.Leader;

public enum ScoiaTaelLeaders {
    QUEEN_OF_DOL_BLATHANNA("Queen of Dol Blathanna", "Destroy Strongest Enemy Ranged", "Destroys the strongest ranged unit if the opponent's close combat units have a total power greater than 10.", "/gwentImages/img/lg/scoiatael_francesca_silver.jpg"),
    THE_BEAUTIFUL("The Beautiful", "Double Ranged Combat Units", "Doubles the power of your ranged units unless there is a Commander's Horn in that row.", "/gwentImages/img/lg/scoiatael_francesca_gold.jpg"),
    DAISY_OF_THE_VALLEY("Daisy of the Valley", "Draw Extra Card at Start", "Draw one extra random card at the start of the game.", "/gwentImages/img/lg/scoiatael_francesca_copper.jpg"),
    PUREBLOOD_ELF("Pureblood Elf", "Play Biting Frost", "Select and play a Biting Frost card from your deck.", "/gwentImages/img/lg/scoiatael_francesca_bronze.jpg"),
    HOPE_OF_THE_AEN_SEIDHE("Hope of the Aen Seidhe", "Move Agile Units to Optimal Row", "Move all agile units to the row where they have the highest power. Units already in the optimal row do not move.", "/gwentImages/img/lg/scoiatael_francesca_hope_of_the_aen_seidhe.jpg");

    private final String name;
    private final Ability ability;
    private final String description;
    private final String imagePath;

    ScoiaTaelLeaders(String name, Ability ability, String description, String imagePath) {
        this.name = name;
        this.ability = ability;
        this.description = description;
        this.imagePath = imagePath;
    }

    public Leader getLeader() {
        return new Leader(name, ability, Faction.SCOIA_TAEL, description, imagePath);
    }
}
