package enums;

public enum SecurityQuestion {
    MOTHER_MAIDEN_NAME("What is your mother's maiden name?", 1),
    FIRST_PET_NAME("What was the name of your first pet?", 2),
    FAVORITE_BOOK("What is your favorite book?", 3),
    BIRTH_CITY("In what city were you born?", 4),
    FIRST_SCHOOL("What was the name of your first school?", 5);

    private final String question;
    private final int number;

    SecurityQuestion(String question, int num) {
        this.question = question;
        this.number = num;
    }

    public String getQuestion() {
        return question;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return question;
    }
}
