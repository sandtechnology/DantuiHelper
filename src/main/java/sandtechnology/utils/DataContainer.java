package sandtechnology.utils;

import java.util.ArrayList;
import java.util.List;

public class DataContainer {
    private static final long master=1294790523;
    private static final List<Long> targetGroup=new ArrayList<>();
    private static final long bot=1700065177;

    static {
        targetGroup.add(532589427L);
    }

    public static long getBot() {
        return bot;
    }

    public static List<Long> getTargetGroup() {
        return targetGroup;
    }

    public static long getMaster() {
        return master;
    }
}
