package sandtechnology.utils;

public class SeenCounter {

    private int now = 0;


    public SeenCounter firstSeen() {
        now = 1;
        return this;
    }

    public SeenCounter reset() {
        now = 0;
        return this;
    }

    public SeenCounter seenAgain() {
        now++;
        return this;
    }

    public int now() {
        return now;
    }

    @Override
    public String toString() {
        return Integer.toString(now);
    }
}
