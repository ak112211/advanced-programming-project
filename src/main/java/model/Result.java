package model;

public class Result {
    private final String message;
    private final boolean success;
    private final String header;

    public Result(boolean success, String header, String message) {
        this.message = message;
        this.success = success;
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getHeader() {
        return header;
    }
}
