package enums.cardsinformation;

public enum Description {
    DESCRIPTION_ONE("Description for scenario one."),
    DESCRIPTION_TWO("Description for scenario two."),
    DESCRIPTION_THREE("Description for scenario three.");

    private final String description;

    Description(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}