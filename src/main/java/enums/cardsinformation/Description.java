package enums.cardsinformation;

public enum Description {
    NOTHING(null),
    KILLS_UNITS("Kills the opponent's card(s) with most power in enemy's Ranged combat row if the sum of powers of none-hero cards in this row is 10 or more."),
    ;
    private final String description;

    Description(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}