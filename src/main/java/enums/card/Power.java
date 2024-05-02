package enums.card;

public enum Power {
    FOUR(4),
    SIX(6),
    EIGHT(8),
    TEN(10),
    ELEVEN(11),
    TWELVE(12),
    FOURTEEN(14),
    TWO(2),
    UNKNOWN;

    private final int power;

    // Constructor for enums with power values
    Power(int power) {
        this.power = power;
    }

    // Constructor for UNKNOWN without power
    Power() {
        this.power = -1; // or any other placeholder value
    }

    public int getPower() {
        return this.power;
    }
}

