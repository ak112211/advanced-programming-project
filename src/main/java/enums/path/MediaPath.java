package enums.path;

public enum MediaPath {
    FINALE("/media/Ramin-Djawadi-Finale-128.mp3"),
    ;
    private final String path;

    MediaPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
