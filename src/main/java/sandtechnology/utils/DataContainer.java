package sandtechnology.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataContainer {
    private static final long master = 1294790523;
    private static final List<Long> targetGroup = new ArrayList<>();
    private static final List<Long> rukiTargetGroup = new ArrayList<>();
    private static final String version = "v2.2";

    public static String getVersion() {
        return version;
    }

    static {
        rukiTargetGroup.addAll(Arrays.asList(1035554886L, 739568838L, 752224664L, 1027385586L));
        targetGroup.add(532589427L);
    }

    public static List<Long> getRukiTargetGroup() {
        return rukiTargetGroup;
    }


    public static List<Long> getTargetGroup() {
        return targetGroup;
    }

    public static long getMaster() {
        return master;
    }
}
