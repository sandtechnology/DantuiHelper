package sandtechnology.config.section;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LiveCheckerData {

    private Map<Long, Long> liveStatus = new ConcurrentHashMap<>();

    public void addLastLive(long roomID, long lastLive) {
        liveStatus.put(roomID, lastLive);
    }

    public long getLastLive(long roomID) {
        return liveStatus.getOrDefault(roomID, 0L);
    }

}
