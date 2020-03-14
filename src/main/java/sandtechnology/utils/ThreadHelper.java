package sandtechnology.utils;

public class ThreadHelper {

    private ThreadHelper() {
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
