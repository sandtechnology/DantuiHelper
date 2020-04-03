package sandtechnology.utils;

public class Counter {

    private int now = 0;


    public void firstSeen() {
        now = 1;
    }

    public void reset() {
        now = 0;
    }

    public void seenAgain() {
        now++;
    }

    public int now() {
        return now;
    }
}
