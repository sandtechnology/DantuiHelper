package sandtechnology.config.section;


import sandtechnology.config.ConfigLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LiveCheckerData {

    private final Map<Long, Long> liveStatus = new ConcurrentHashMap<>();

    public void updateLastLive(long roomID, long lastLive) {
        liveStatus.put(roomID, lastLive);
        ConfigLoader.save();
    }

    public long getLastLive(long roomID) {
        return liveStatus.getOrDefault(roomID, 0L);
    }

}
